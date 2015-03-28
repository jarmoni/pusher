var app;
(function () {
	'use strict';
	app = angular.module('pusher', ['ui.router']);
	app.urlPrefix = '/api/';
	
	app.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/repositories');

        $stateProvider
            .state('repositories', {
                url: "/repositories",
                templateUrl: '/js/repositories/repositories.html'
            }).state('repository', {
                url: "/:repositoryName",
                parent: "repositories",
                templateUrl: '/js/repository/repository.html'
            }).state('events', {
                url: "/:events",
                templateUrl: '/js/events/events.html'
            }).state('help', {
                url: "/help",
                templateUrl: '/js/help/help.html'
			});
    });
})();