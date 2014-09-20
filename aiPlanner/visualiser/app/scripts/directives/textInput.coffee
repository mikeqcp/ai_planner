'use strict'

angular.module('visualiserApp')
.directive 'textInput', ($http) ->
		restrict: 'A'
		templateUrl: '/views/directives/textInput.html';
		replace: true
		scope:
			title: '@'
			sourceFiles: '='
			content: '='

		link: (scope) ->
			scope.content = null
			scope.examples = []
			scope.sourceFiles.forEach (f) ->
					$http.get(f.src).then (response) ->
						scope.examples.push {label: f.label, data: response.data}
						if not scope.content?
							scope.contentItem = scope.examples[0]

			scope.$watch 'contentItem', (i) ->
				scope.content = i?.data