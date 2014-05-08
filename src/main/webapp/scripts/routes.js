define([ 'app' ], function(app) {
    'use strict';

    app.config(['$routeProvider', function($routeProvider) {

        $routeProvider.when('/', {
            templateUrl: 'partials/home.html',
            controller:  'ArticleLatestListController'
        });

        $routeProvider.when('/articles/add', {
            templateUrl: 'partials/articleAdd.html',
            controller:  'ArticleAddController'
        });

        $routeProvider.when('/articles/books', {
            templateUrl: 'partials/articleList.html',
            controller:  'ArticleListController',
            resolve:     { service: function(Book) { return Book; } }
        });
        $routeProvider.when('/articles/comics', {
            templateUrl: 'partials/articleList.html',
            controller:  'ArticleListController',
            resolve:     { service: function(Comic) { return Comic; } }
        });
        $routeProvider.when('/articles/movies', {
            templateUrl: 'partials/articleList.html',
            controller:  'ArticleListController',
            resolve:     { service: function(Movie) { return Movie; } }
        });
        $routeProvider.when('/articles/music', {
            templateUrl: 'partials/articleList.html',
            controller:  'ArticleListController',
            resolve:     { service : function(Music) { return Music; } }
        });
        $routeProvider.when('/articles', {
            templateUrl: 'partials/articleList.html',
            controller:  'ArticleListController',
            resolve :    { service: function(Article) { return Article; } }
        });

        $routeProvider.otherwise({ redirectTo : '/' });

    } ]);

});
