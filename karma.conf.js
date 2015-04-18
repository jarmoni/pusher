module.exports = function(config) {
    config.set({
        basePath: 'src/main/resources/public',

        files: [
            'lib/angular/angular.js',
            'lib/angular-mocks/angular-mocks.js',
            'lib/angular-route/angular-route.js',
            'lib/angular-ui-router/release/angular-ui-router.js',
            'lib/lodash/lodash.js',
            'js/**/*.js',
            'test/**/*.js'
        ],

        autoWatch: true,

        frameworks: ['jasmine'],
        exclude: [
            'js/app-mocked.js'
        ],
        preprocessors: {},
        reporters: ['progress'],
        port: 9876,
        colors: true,
        logLevel: config.LOG_DEBUG,
        autoWatch: true,
        browsers: ['PhantomJS'],
        singleRun: true,
    })
}
