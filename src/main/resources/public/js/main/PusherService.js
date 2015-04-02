(function () {
    "use strict";
    app.factory('PusherService', function ($http) {
        var repositories = [{"name" : "repos1", "path" : "/var/log/xyz", "autoCommit" : true, "autoSync" : false}, {"name" : "repos2", "path" : "/home/xyz/bla", "autoCommit" : true, "autoSync" : true}, {"name" : "repos3", "path" : "/root/temp", "autoCommit" : false, "autoSync" : false}];

        var PusherService = {};
        PusherService.listRepositories = function() {
			return _.cloneDeep(repositories);
		};

        PusherService.getRepository = function(name) {
            return _.find(PusherService.listRepositories(), function(repos) {
                return repos.name === name;
            });
        };

        PusherService.update = function(oldName, current) {
            var modified = false;
            repositories.forEach(function(repos) {
                if(oldName === repos.name) {
                    repos.name = current.name;
                    repos.path = current.path;
                    repos.autoCommit = current.autoCommit;
                    repos.autoSync = current.autoSync;
                    modified = true;
                }
            });
            if(!modified) {
                repositories.push(current);
            }
        }

        PusherService.delete = function(name) {
            var newRepositories = _.filter(repositories, function(repos) {
                return name !== repos.name;
            });
            repositories = newRepositories;
        }


		return PusherService;
	});
})();