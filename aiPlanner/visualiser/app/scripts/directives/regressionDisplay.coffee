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
			plumbInstance = null

			createDOM = (node) ->
				dom = $('<div></div>').addClass('node')

				if not node.valid then dom.addClass 'invalid'

				$.each node.items, (k,v) ->
					dom.append $('<div></div>').append(v.label).addClass('node-state')
				dom

			drawNode = (node) ->
				currentStepNode = createDOM node
				elem.find('#tree-container').append currentStepNode

				node.items.forEach (i) ->
					i.children.forEach (c) ->
						childNode = drawNode c
						drawLink currentStepNode, childNode, c.parentAction
				return currentStepNode

			initLinks = () ->
				plumbInstance = jsPlumb.getInstance({
					Endpoint : ["Dot", {radius:2}],
					HoverPaintStyle : {strokeStyle:"#1e8151", lineWidth:2 },
					ConnectionOverlays : [
						[ "Arrow", {
							location:1,
							id:"arrow",
							length:14,
							foldback:0.8
						} ],
						[ "Label", { id:"label", cssClass:"aLabel" }]
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
				elem.find('#tree-container').empty()
				scope.currentStepState = scope.result.stateHistory[scope.step]
				drawNode scope.currentStepState.structure.root
				refreshLinks()

			scope.$watch 'result', () ->
				refreshTree()

			scope.$watch 'step', refreshTree
			initLinks()
