(function () {
    "use strict";

    app.controller("RepositoriesController", function ($scope, $state, $stateParams, PusherService) {

        PusherService.listRepositories(function(repositories) {
            $scope.repositories = repositories;
        });


        $scope.update = function() {

        }

    });
})();