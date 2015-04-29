(function() {
	"use strict";
	app.factory('PusherService', function($http, $q) {

		// public API
		var PusherService = {
			listRepositories: listRepositories,
			getRepository: getRepository,
			deleteRepository: deleteRepository,
			updateRepository: updateRepository
		};

		return PusherService;

		// private impl
		function listRepositories() {
			var request = $http.get(app.urlPrefix + 'repositories/list');
			return (request.then(handleSuccess, handleError));
		}

		function getRepository(name) {
			var request = $http.get(app.urlPrefix + "repository/get/" + name);
			return (request.then(handleSuccess, handleError));
		}

		function deleteRepository(name) {
			var request = $http.delete(app.urlPrefix + "repository/delete/" + name);
			return (request.then(handleSuccess, handleError));
		}

		function updateRepository(name, repository) {
			var request = $http.put(app.urlPrefix + "repository/update/" + name, repository);
			return (request.then(handleSuccess, handleError));
		}

		function handleError(response) {
			if (!angular.isObject(response.data) || !response.data.message) {
				return ($q.reject("An unknown error occurred."));
			}
			return ($q.reject(response.data.message));
		}

		function handleSuccess(response) {
			return (response.data);
		}
	});
})();
