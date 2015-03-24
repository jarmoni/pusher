(function () {
	"use strict";

	app.controller("MainController", function ($scope, PusherService) {
	
		$scope.containers = PusherService.listRepositories();

	});
})();