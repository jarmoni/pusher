app.run(function($httpBackend) {
    var repositories = [{name : "myrepos1", path: "/usr/mypath", autoCommit:true, autoSync:false}, {name : "repos2", path : "/var/xyz", autoCommit: true, autoSync:true}, {name:"another_one", path:"/bla/bla", autoCommit:false, autoSync:false}];

    $httpBackend.whenGET(app.urlPrefix + "repositories/list").respond(repositories);

    //$httpBackend.whenGET(function(url){
    //    return url.indexOf(app.urlPrefix + "/repository/") === 0;
    //}).respond(function(method, url, data) {
    //
    //    return [200, repositories[0], {}];
    //});

    $httpBackend.whenGET(app.urlPrefix + "repository/myrepos1").respond(repositories[0]);

    $httpBackend.whenGET("/^\/api\/repository\/.+/").respond(function(method, url, data) {
       return [200, repositories[0], {}];
    });

    $httpBackend.whenGET(/^\/js\//).passThrough();
});

