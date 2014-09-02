'use strict'

angular.module('visualiserApp')
.directive 'resultView', () ->
		restrict: 'A'
		templateUrl: '/views/directives/resultView.html';
		replace: true
		scope:
			result: '='

		link: (scope, elem) ->
			createDOM = (stack) ->
				dom = $('<div></div>').addClass('stack')
				$.each stack, (k,v) ->
					dom.prepend $('<div></div>').append(v.label).addClass('stack-item')
				dom

			refreshStack = () ->
				scope.currentStepState = scope.result.stateHistory[scope.step]
				currentStepStackDOM = createDOM(scope.currentStepState.structure.stack)
				elem.find('#stack-container').empty().append currentStepStackDOM

			scope.$watch 'result', (res) ->
				scope.step = 0
				refreshStack()
				console.log 'current result: ', res
				scope.parsedResult = JSON.stringify res

			scope.$watch 'step', refreshStack


			scope.nextStep = () ->
				scope.step++

			scope.prevStep = () ->
				scope.step--