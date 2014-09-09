'use strict'

angular.module('visualiserApp')
.directive 'regressionDisplay', () ->
		restrict: 'A'
		templateUrl: '/views/directives/regressionDisplay.html';
		replace: true
		scope:
			result: '='
			step: '='

		link: (scope, elem) ->
#			containerWidth = $('body > .container').width()
			containerWidth = $('body').width()
			$('#tree-container').css 'left', -($('body').width() - $('body > .container').width()) / 2
			plumbInstance = null
			nextNodePos = {x: 0, y:0}
			nodeMargin = {x:150, y: 150}
			rowHeight = 0

			createDOM = (node) ->
				dom = $('<div></div>').addClass('node')

				if not node.valid then dom.addClass 'invalid'
				if ($.inArray node.id, scope.result.resultPlan.planIds) >= 0
					dom.addClass 'plan-node'
				else
					dom.removeClass 'plan-node'

				$.each node.items, (k,v) ->
					dom.append $('<div></div>').append(v.label).addClass('node-state')

				dom.css {left: nextNodePos.x, top: nextNodePos.y}
				dom

			calcNextNodePosition = (currentNode) ->
				nodeHeight = currentNode.height()
				nodeWidth = currentNode.width()

				newY = nextNodePos.y
				newX = nextNodePos.x + nodeWidth + nodeMargin.x

				if newX > (containerWidth - nodeWidth)
					newX = 0
					newY += rowHeight + nodeMargin.y
					rowHeight = nodeHeight

				if nodeHeight > rowHeight then rowHeight = nodeHeight

				nextNodePos.x = newX
				nextNodePos.y = newY

			drawNode = (node) ->
				nodeDomElem = createDOM node
				elem.find('#tree-container').append nodeDomElem
				calcNextNodePosition(nodeDomElem)

				node.items.forEach (i) ->
					i.children.forEach (c) ->
						childNode = drawNode c
						drawLink nodeDomElem, childNode, c.parentAction
				return nodeDomElem

			initLinks = () ->
				plumbInstance = jsPlumb.getInstance({
					Endpoint : ["Dot", {radius:2}],
					HoverPaintStyle : {strokeStyle:"#1e8151", lineWidth:2 },
					ConnectionOverlays : [
						[ "Arrow", {
							location:1,
							id:"arrow",
							length:14,
							foldback:0.8,
						}]
						],
					Container:"tree-container"
				});

			refreshLinks = () ->
				boxes = jsPlumb.getSelector("#tree-container .node");
				plumbInstance.draggable(boxes);

				$(boxes).one 'mousedown', () ->
					$(this).css 'position', 'absolute'

			drawLink = (nodeParent, nodeChild, label) ->
				link = plumbInstance.connect {source: nodeParent, target: nodeChild}
				link.setLabel label



			refreshTree = () ->
				$('._jsPlumb_endpoint').remove()
				$('._jsPlumb_connector').remove()
				$('._jsPlumb_overlay').remove()
				$('._jsPlumb_endpoint').remove()

				nextNodePos = {x: 0, y:0}

				elem.find('#tree-container').empty()
				scope.currentStepState = scope.result.stateHistory[scope.step]
				drawNode scope.currentStepState.structure.root
				refreshLinks()

			scope.$watch 'result', () ->
				refreshTree()

			scope.$watch 'step', refreshTree
			initLinks()
