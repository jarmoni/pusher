app.run(function($httpBackend) {
    var repositories = [{name : "myrepos1", path: "/usr/mypath", autoCommit:true, autoSync:false}, {name : "repos2", path : "/var/xyz", autoCommit: true, autoSync:true}, {name:"another_one", path:"/bla/bla", autoCommit:false, autoSync:false}];

    $httpBackend.whenGET(app.urlPrefix + "repositories/list").respond(repositories);

    $httpBackend.whenGET(/^\/api\/repository\/get\/.+/).respond(function(method, url, data) {
        var repository = getRepository(url);
        if(repository) {
            return [200, repository, {}];
        }
        return [204, null, {}];
    });

    $httpBackend.whenDELETE(/^\/api\/repository\/delete\/.+/).respond(function(method, url, data) {
        var repository = getRepository(url);
        if(repository) {
            _.remove(repositories, function(repos) {
                var isEqual = _.isEqual(repository, repos);
                return isEqual;
            });
            return [204, null, {}];
        }
        return [400, null, {}];
    });

    $httpBackend.whenPUT(/^\/api\/repository\/update\/.+/).respond(function(method, url, data) {
        var newRepos = JSON.parse(data);
        var repository = getRepository(url);
        if(repository) {
            repository.name = newRepos.name;
            repository.path = newRepos.path;
            repository.autoCommit = newRepos.autoCommit;
            repository.autoSync = newRepos.autoSync;
            // we do NOT return 'newRepos' because in real life we also get a string returned from server
            return [200, data, {}];
        }
        else {
            repositories.push(newRepos);
            return [204, data, {}];
        }
    });

    //$httpBackend.whenGET("/^\/api\/repository\/.+/").respond(function(method, url, data) {
    //   return [200, repositories[0], {}];
    //});

    $httpBackend.whenGET(/^\/js\//).passThrough();

    function getRepository(url) {
        console.log("url=" + url);
        var splitted = url.split("/");
        var reposName = splitted[splitted.length-1];
        return _.find(repositories, function(repository) {
            return repository.name === reposName;
        });
    }
});

