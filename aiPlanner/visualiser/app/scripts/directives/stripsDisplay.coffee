'use strict'

angular.module('visualiserApp')
.directive 'stripsDisplay', () ->
		restrict: 'A'
		templateUrl: '/views/directives/stripsDisplay.html';
		replace: true
		scope:
			result: '='
			step: '='

		link: (scope, elem) ->
			createDOM = (stack) ->
				dom = $('<div></div>').addClass('stack')
				$.each stack, (k,v) ->
					dom.prepend $('<div></div>').append(v.label).addClass('stack-item').addClass(v.type)
				dom

			refreshStack = () ->
				scope.currentStepState = scope.result.stateHistory[scope.step]
				currentStepStackDOM = createDOM(scope.currentStepState.structure.stack)
				elem.find('#stack-container').empty().append currentStepStackDOM

			scope.$watch 'result', (res) ->
				refreshStack()

			scope.$watch 'step', refreshStack
