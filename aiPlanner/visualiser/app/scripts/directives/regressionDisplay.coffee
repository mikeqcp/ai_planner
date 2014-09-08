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
#			createDOM = (stack) ->
#				dom = $('<div></div>').addClass('stack')
#				$.each stack, (k,v) ->
#					dom.prepend $('<div></div>').append(v.label).addClass('stack-item')
#				dom
#
			refreshTree = () ->
#				scope.currentStepState = scope.result.stateHistory[scope.step]
#				currentStepStackDOM = createDOM(scope.currentStepState.structure.stack)
#				elem.find('#stack-container').empty().append currentStepStackDOM

			scope.$watch 'result', (res) ->
				refreshTree()

			scope.$watch 'step', refreshTree
