'use strict'

angular.module('visualiserApp')
.directive 'textInput', () ->
		restrict: 'A'
		templateUrl: '/views/directives/textInput.html';
		replace: true
		scope:
			content: '='
			title: '@'