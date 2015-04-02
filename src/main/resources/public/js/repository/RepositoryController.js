(function () {
    "use strict";

    app.controller("RepositoryController", function ($scope, $state, $stateParams, PusherService) {

        var repository = PusherService.getRepository($stateParams["repositoryName"]);

        //var repository = null;
        //var reposOrigName = "New Repository";
        //var currentReposName = $stateParams["repositoryName"];
        //
        //if(currentReposName) {
        //    repository = PusherService.getRepository($stateParams["repositoryName"]);
        //    reposOrigName = repository.name;
        //}

        $scope.repository = repository;
        $scope.reposOrigName = repository.name;

        $scope.formModified = function() {
            var repository = PusherService.getRepository($scope.repository.name);
            var repos2 = { name: $scope.repository.name, path: $scope.repository.path, autoCommit: $scope.repository.autoCommit, autoSync: $scope.repository.autoSync};
            console.log("repository=" + JSON.stringify(repository));
            console.log("repos2=" + JSON.stringify(repos2));
            var modified = !_.isEqual(repository, repos2);
            console.log("modified=" + modified);
            return modified;
        }

        $scope.reposName = function() {
            return $scope.repository.name;
        }

        $scope.update = function() {
            var reposOrigName = $scope.repository.name;
            PusherService.update($scope.reposOrigName, $scope.repository);
            $scope.$parent.update();
            $scope.reposOrigName = reposOrigName;
        }

        // just for debugging:
        $scope.currentRepos = function() {
            return $scope.repository;
        }

    });
})();