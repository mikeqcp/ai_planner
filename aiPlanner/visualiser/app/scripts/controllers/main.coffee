'use strict'

angular.module('visualiserApp')
.controller 'MainCtrl', ($scope, $http) ->
		$http.get('/data/domain.txt').then (response) ->
			$scope.domain = response.data

		$http.get('/data/instance.txt').then (response) ->
			$scope.instance = response.data

		$scope.solve = () ->
			console.log 'Domain: ', $scope.domain
			console.log 'Instance: ', $scope.instance