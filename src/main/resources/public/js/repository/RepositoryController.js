(function () {
    "use strict";

    app.controller("RepositoryController", function ($scope, $state, $stateParams, PusherService) {

        var newReposName = "New Repository";

        $scope.reposOrigName = newReposName;
        $scope.repository = null;
        $scope.deleteAllowed = false;
        $scope.saveAllowed = false;
        $scope.cancelAllowed = false;

        var cachedObject = null;

        if($stateParams["repositoryName"]) {
            loadRemoteData($stateParams["repositoryName"], applyRemoteData)
        }

        $scope.updateState = function() {
            $scope.saveAllowed = isValid() && isModified();
            $scope.cancelAllowed = isModified();
        }

        $scope.delete = function() {
            PusherService.deleteRepository($scope.reposOrigName).then(function() {
                PusherService.listRepositories().then(function (repositories) {
                    $scope.repositories.repositories = repositories;
                    $state.go("noRepository");
                });
            });
        }

        $scope.cancel = function() {
            if(cachedObject) {
                applyRemoteData(cachedObject);
            }
            else {
                $state.go("noRepository");
            }
        }

        $scope.update = function() {
            PusherService.updateRepository($scope.reposOrigName, $scope.repository).then(function(newRepos) {
                applyRemoteData(newRepos);
                PusherService.listRepositories().then(function (repositories) {
                    $scope.repositories.repositories = repositories;
                });
            });
        }

        function applyRemoteData(repository) {
            if(repository) {
                $scope.repository = repository;
                $scope.reposOrigName = repository.name;
                $scope.deleteAllowed = true;
                cachedObject = _.cloneDeep(repository);
                $scope.updateState();
            }
        }

        function loadRemoteData(name, delegateFunction) {
            PusherService.getRepository(name).then(function (repository) {
                delegateFunction(repository);
            });
        }

        function isValid() {
            var valid = false;
            if($scope.repository && $scope.repository.name && $scope.repository.path) {
                valid = true;
            }
            return valid;
        }

         function isModified() {
             var modified = false;
             if(cachedObject != null) {
                 modified = !_.isEqual(cachedObject, $scope.repository);
             }
             else {
                 modified = $scope.reposOrigName !== newReposName || $scope.repository.name || $scope.repository.path;
             }
             return modified;
        }
    });
})()