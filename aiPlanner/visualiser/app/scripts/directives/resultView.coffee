'use strict'

angular.module('visualiserApp')
.directive 'resultView', () ->
		restrict: 'A'
		templateUrl: '/views/directives/resultView.html';
		replace: true
		scope:
			result: '='

		link: (scope) ->
			scope.$watch 'result', (res) ->
				console.log 'current result: ', res
				scope.parsedResult = JSON.stringify res