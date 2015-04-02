(function () {
    "use strict";

    app.controller("RepositoriesController", function ($scope, $state, $stateParams, PusherService) {

        $scope.repositories = PusherService.listRepositories();

        $scope.update = function() {
            $scope.repositories = PusherService.listRepositories();
        }

    });
})();