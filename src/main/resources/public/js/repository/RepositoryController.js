(function () {
    "use strict";

    app.controller("RepositoryController", function ($scope, $state, $stateParams, PusherService) {

        var NEW_REPOS = "New Repository";

        var repository = null;
        var reposOrigName = NEW_REPOS;

        var currentReposName = $stateParams["repositoryName"];

        if(currentReposName) {
            PusherService.getRepository(currentReposName, function(callback) {
                repository = callback;
                reposOrigName = repository.name;
                $scope.repository = repository;
                $scope.reposOrigName = reposOrigName;
            });

        }


        $scope.saveAllowed = function() {
            return valid() && modified();
        }

        $scope.cancelAllowed = function() {
            return $scope.reposOrigName === NEW_REPOS || modified();
        }

        $scope.reposName = function() {
            return $scope.repository.name;
        }

        $scope.update = function() {
            PusherService.update($scope.reposOrigName, $scope.repository);
            $scope.$parent.update();
            $scope.reposOrigName = $scope.repository.name;
            //$state.go("repository", $scope.repository.name);
        }

        $scope.delete = function() {
            PusherService.delete($scope.repository.name);
            $scope.$parent.update();
            $state.go("noRepository");
        }

        $scope.cancel = function() {
            var repos = PusherService.getRepository($scope.reposOrigName);
            if(repos) {
                $scope.repository = repos;
            }
            else {
                $state.go("noRepository");
            }
        }

        function valid() {
            if($scope.repository != null && $scope.repository.name != null && $scope.repository.path != null) {
                return true;
            }
            return false;
        }

        function modified() {
            var repository = PusherService.getRepository($scope.reposOrigName);

            var repos2 = { name: $scope.repository.name, path: $scope.repository.path, autoCommit: $scope.repository.autoCommit, autoSync: $scope.repository.autoSync};
            console.log("repository=" + JSON.stringify(repository));
            console.log("repos2=" + JSON.stringify(repos2));
            var modified = !_.isEqual(repository, repos2);
            console.log("modified=" + modified);
            return modified;
        }

    });
})();