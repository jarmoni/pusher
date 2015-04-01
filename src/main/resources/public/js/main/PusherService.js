(function () {
    "use strict";
    app.factory('PusherService', function ($http) {
        var PusherService = {};



        PusherService.listRepositories = function() {
            var repositories = [{"name" : "repos1", "path" : "/var/log/xyz", "autoCommit" : true, "autoSync" : false}, {"name" : "repos2", "path" : "/home/xyz/bla", "autoCommit" : true, "autoSync" : true}, {"name" : "repos3", "path" : "/root/temp", "autoCommit" : false, "autoSync" : false}];
			return repositories;
		};

        PusherService.getRepository = function(name) {
            return _.find(PusherService.listRepositories(), function(repos) {
                return repos.name === name;
            });
        }


		return PusherService;
	});
})();