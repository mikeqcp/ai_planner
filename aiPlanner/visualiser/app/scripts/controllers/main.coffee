'use strict'

angular.module('visualiserApp')
.controller 'MainCtrl', ($scope, $http, $log) ->
		$scope.algorithms = [
			{id:'strips', label:'STRIPS'},
			{id:'regression', label:'Regression'},
			{id:'pop', label:'POP'}
		]
		$scope.selectedAlgorithm = $scope.algorithms[0]
		$scope.limit = 2

		$scope.domains = [
			{label:'blocks world', src: '/data/domain.txt'}
		]

		$scope.instances = [
			{label:'2 blocks', src:'/data/instance2.txt'},
			{label:'2 blocks v 2', src:'/data/instance2-2.txt'},
			{label:'3 blocks', src:'/data/instance.txt'},
			{label:'3 blocks hard', src:'/data/instance3.txt'},
		]

		$http.defaults.headers.post =
			'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'

		$scope.toggleProblem = () ->
			$('.problem').slideToggle(400)
			$('.toggle-problem').toggleClass('glyphicon-minus glyphicon-plus')
			return

		$scope.solve = () ->
			data =
				type: $scope.selectedAlgorithm.id
				domain: $scope.domain
				instance: $scope.instance
				limit: $scope.limit

			$http.post("http://127.0.0.1:8085/planner/solve", $.param data)
			.then (d) ->
				console.log 'Algorithm result', d.data
				$scope.result = d.data
				$scope.toggleProblem()
			.catch (e) ->
					$log.error 'error: ', e