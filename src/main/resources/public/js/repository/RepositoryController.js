(function () {
    "use strict";

    app.controller("RepositoryController", function ($scope, $state, $stateParams, PusherService) {

        var repository = PusherService.getRepository($stateParams["repositoryName"]);

        $scope.repository = repository;
        $scope.reposOrigName = repository.name;

        $scope.formModified = function() {
            var repository = PusherService.getRepository($stateParams["repositoryName"]);
            var repos2 = { name: $scope.repository.name, path: $scope.repository.path, autoCommit: $scope.repository.autoCommit, autoSync: $scope.repository.autoSync};
            var modified = !_.isEqual(repository, repos2);
            return modified;
        }

        $scope.reposName = function() {
            return $scope.repository.name;
        }

        // just for debugging:
        $scope.currentRepos = function() {
            return $scope.repository;
        }

    });
})();