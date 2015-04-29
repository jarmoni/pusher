(function() {
	"use strict";

	app.controller("RepositoriesController", function($scope, $state, $stateParams, PusherService) {

		$scope.repositories = [];

		loadRemoteData();

		function applyRemoteData(repositories) {
			$scope.repositories.repositories = repositories;
		}

		function loadRemoteData() {
			PusherService.listRepositories().then(function(repositories) {
				applyRemoteData(repositories);
			});
		}

	});
})();
