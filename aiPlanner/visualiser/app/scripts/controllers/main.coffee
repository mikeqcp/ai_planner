'use strict'

angular.module('visualiserApp')
.controller 'MainCtrl', ($scope, $http, $log) ->
		$scope.algorithms = [
			{id:'strips', label:'STRIPS'},
			{id:'opRegression', label:'Operator Regression'},
			{id:'pop', label:'POP'}
		]
		$scope.selectedAlgorithm = $scope.algorithms[0]


		$http.get('/data/domain.txt').then (response) ->
			$scope.domain = response.data

		$http.get('/data/instance.txt').then (response) ->
			$scope.instance = response.data

		$http.defaults.headers.post =
			'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'

		$scope.solve = () ->
			data =
				type: $scope.selectedAlgorithm.id
				domain: $scope.domain
				instance: $scope.instance

			$http.post("http://127.0.0.1:8085/planner/solve", $.param data)
			.then (d) ->
				$scope.result = d.data
			.catch (e) ->
					$log.error 'error: ', e