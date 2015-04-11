(function () {
    "use strict";
    app.factory('PusherService', function ($http) {

        var PusherService = {};
        PusherService.listRepositories = function(callbackFunction) {
            $http.get(app.urlPrefix + 'repositories/list').success(callbackFunction);
		};

        PusherService.getRepository = function(name, callbackFunction) {
            console.log(name);
            $http.get(app.urlPrefix + 'repository/' + name).success(callbackFunction);
        };

        //PusherService.getRepository = function(name, function(callback)) {
        //    $http.get(app.urlPrefix + "repository/get/" + name);
        //}
        //});

		return PusherService;
	});
})();