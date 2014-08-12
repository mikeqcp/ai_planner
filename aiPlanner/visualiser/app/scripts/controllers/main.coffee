'use strict'

angular.module('visualiserApp')
.controller 'MainCtrl', ($scope) ->
		$scope.solve = () ->
			console.log 'Domain: ', $scope.domain
			console.log 'Instance: ', $scope.instance
