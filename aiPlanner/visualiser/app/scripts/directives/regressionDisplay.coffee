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
			rowWidth = 0
			scope.nodesCount = 0
			nodesToDraw = []

			createDOM = (node) ->
				dom = $('<div></div>').addClass('node')

				if not node.valid then dom.addClass 'invalid'
				if node.visited then dom.addClass 'visited'
				if ($.inArray node.id, scope.result.resultPlan.planIds) >= 0
					dom.addClass 'plan-node'
				else
					dom.removeClass 'plan-node'

				node.items = _.sortBy node.items, (i) -> i.label
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
					rowWidth = nodeWidth

				if nodeHeight > rowHeight then rowHeight = nodeHeight
				rowWidth += nodeWidth

				nextNodePos.x = newX
				nextNodePos.y = newY

			clearRow = () ->
				nextNodePos.x = 0
				nextNodePos.y += rowHeight + nodeMargin.y


			drawTree = () ->
				while nodesToDraw.length > 0
					lvl = nodesToDraw.shift()
					drawTreeLvl lvl
					clearRow()

			drawTreeLvl = (lvl) ->
				lvl.nodes = _.sortBy lvl.nodes, (n) -> n.id
				lvl.nodes.forEach (node) ->
					scope.nodesCount++

					nodeDomElem = createDOM node
					elem.find('#tree-container').append nodeDomElem
					if lvl.parent?
						drawLink lvl.parent, nodeDomElem, node.parentAction
					calcNextNodePosition(nodeDomElem)

					nextItems = []
					node.items.forEach (i) ->
						i.children.forEach (c) ->
							nextItems.push c
					if nextItems.length > 0
						nodesToDraw.push {parent:nodeDomElem, nodes:nextItems}

			#breadth-first-search all nodes
			foreachNode = (callback) ->
				console.log 'Nothing so far..'

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
				link = plumbInstance.connect {source: nodeParent, target: nodeChild, anchors: [["Left", "Right"], ["Top", "Bottom"]]}
				link.setLabel label



			refreshTree = () ->
				scope.nodesCount = 0
				jsPlumb.detachEveryConnection()

				nextNodePos = {x: 0, y:0}

				elem.find('#tree-container').empty()
				scope.currentStepState = scope.result.stateHistory[scope.step]

				nodesToDraw.push {parent: null, nodes: [scope.currentStepState.structure.root]}
				drawTree()
				refreshLinks()

			scope.$watch 'result', () ->
				refreshTree()

			scope.$watch 'step', refreshTree
			initLinks()
