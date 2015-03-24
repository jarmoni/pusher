(function () {
    "use strict";
    app.factory('PusherService', function ($http) {
		var PusherService = {};
		PusherService.listRepositories = function listRepositories() {
			return $http.get(app.urlPrefix + 'repositories/list').then(function(result) {
				return result;
			});
		};
		return PusherService;
	});
})();