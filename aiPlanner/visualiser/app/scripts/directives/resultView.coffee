'use strict'

angular.module('visualiserApp')
.directive 'resultView', () ->
		restrict: 'A'
		templateUrl: '/views/directives/resultView.html';
		replace: true
		scope:
			result: '='

		link: (scope, elem) ->
			scope.showResult = false

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
				if not res then return
				scope.showResult = true
				scope.step = 0
				refreshStack()

			scope.$watch 'step', refreshStack

			scope.nextStep = () ->
				return unless scope.step < scope.result.stateHistory.length - 1
				scope.step++

			scope.prevStep = () ->
				return unless scope.step > 0
				scope.step--