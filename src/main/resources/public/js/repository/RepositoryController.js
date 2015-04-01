(function () {
    "use strict";

    app.controller("RepositoryController", function ($scope, $state, $stateParams, PusherService) {

        var repository = PusherService.getRepository($stateParams["repositoryName"]);
        $scope.repository = repository;
        //$scope.repositoryName = repository.name;
        //$scope.repositoryPath = repository.path;

        $scope.formModified = function(name, path, autoCommit, autoSync) {
            var repository = PusherService.getRepository($stateParams["repositoryName"]);
            var repos2 = { name: name, path: path, autoCommit: autoCommit, autoSync: autoSync};
            return !_.isEqual(repository, repos2);
        }

    });
})();