'use strict'

angular.module('visualiserApp')
.directive 'popDisplay', () ->
		restrict: 'A'
		templateUrl: '/views/directives/popDisplay.html';
		replace: true
		scope:
			result: '='
			step: '='

		link: (scope, elem) ->
			containerWidth = $('.container').width()
			plumbInstance = null
			nextNodePos = {x: 0, y:0}
			nodeMargin = {x:150, y: 300}
			scope.nodesCount = 0
			odd = true

			createDOM = (node) ->
				dom = $('<div></div>').addClass('node')

				dom.attr 'id', 'node-' + node.id

				dom.append $('<div></div>').append(node.id).addClass('node-id')
				dom.append $('<div></div>').append(node.label).addClass('node-label')

				if odd
					dom.css {left:0, top: nextNodePos.y}
				else
					dom.css {left: containerWidth - 200, top: nextNodePos.y}

				odd = not odd
				nextNodePos.y += nodeMargin.y
				dom


			drawGraph = () ->
				nodes = scope.currentStepState.structure.nodes
				links = scope.currentStepState.structure.links

				nodes = _.sortBy nodes, (n) -> n.id

				scope.minId = nodes[0].id
				nodes = _.map nodes, (n) ->
					n.id -= scope.minId
					n

				links = _.map links, (l) ->
					l.idFrom -= scope.minId
					l.idTo -= scope.minId
					l

				nodes.forEach (n) ->
					drawNode n

				links.forEach (l) ->
					nodeFrom = $('#node-' + l.idFrom)
					nodeTo = $('#node-' + l.idTo)
					drawLink nodeFrom, nodeTo, l.param


			drawNode = (node) ->
				scope.nodesCount++

				nodeDomElem = createDOM node
				elem.find('#graph-container').append nodeDomElem



			initLinks = () ->
				plumbInstance = jsPlumb.getInstance({
					Endpoint : ["Dot", {radius:2}],
					HoverPaintStyle : {strokeStyle:"#1e8151", lineWidth:2 },
					ConnectionOverlays : [
						[ "Arrow", {
							location:0.9,
							id:"arrow",
							length:14,
							foldback:0.1,
						}]
						],
					Container:"graph-container"
				});

			refreshLinks = () ->
				boxes = jsPlumb.getSelector("#graph-container .node");
				plumbInstance.draggable(boxes);

				$(boxes).one 'mousedown', () ->
					$(this).css 'position', 'absolute'

			drawLink = (nodeFrom, nodeTo, label) ->
				link = plumbInstance.connect {
					source: nodeFrom,
					target: nodeTo,
					anchors: [["Left", "Right", "Top", "Bottom"], ["Left", "Right", "Top", "Bottom"]],
				}
				link.setLabel label

			refreshGraph = () ->
				odd = true
				scope.nodesCount = 0
				scope.validNodesCount = 0
#				jsPlumb.detachEveryConnection()
				jsPlumb.reset()

				nextNodePos = {x: 0, y:0}

				elem.find('#graph-container').empty()
				scope.currentStepState = scope.result.stateHistory[scope.step]

				drawGraph()
				refreshLinks()

			scope.$watch 'result', () ->
				refreshGraph()

			scope.$watch 'step', refreshGraph
			initLinks()
