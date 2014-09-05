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

			scope.$watch 'result', (res) ->
				if not res then return
				scope.showResult = true
				scope.step = 0

			scope.nextStep = () ->
				return unless scope.step < scope.result.stateHistory.length - 1
				scope.step++

			scope.prevStep = () ->
				return unless scope.step > 0
				scope.step--