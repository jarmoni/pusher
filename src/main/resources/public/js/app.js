var app;
(function () {
	'use strict';
	app = angular.module('pusher', ['ui.router', 'ngMockE2E']);
	app.urlPrefix = '/api/';
	
	app.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/repositories');


        $stateProvider
            .state('repositories', {
                url: "/repositories",
                templateUrl: '/js/repositories/repositories.html'
            }).state('repository', {
                parent: "repositories",
                url: "/:repositoryName",
                //params: {'repositoryName' : null, 'repositoryPath' : null},
                templateUrl: '/js/repository/repository.html'
            }).state('noRepository', {
                parent: "repositories",
                //params: {'repositoryName' : null, 'repositoryPath' : null},
                template: ''
            }).state('events', {
                url: "/events",
                templateUrl: '/js/events/events.html'
            }).state('help', {
                url: "/help",
                templateUrl: '/js/help/help.html'
			});
    });

    app.run(['$rootScope', '$state', '$stateParams', function ($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    }]);
})();