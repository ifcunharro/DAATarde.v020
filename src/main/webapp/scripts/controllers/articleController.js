define(['controllers/controllers'], function(controllers) {
    'use strict';

    var parseArticlePromise = function(articles, callback) {
        articles.$promise.then(function(articles) {
            articles.forEach(function(article) {
                article.date = article.date.join('/');
            });
            callback(articles);
        });
    };

    var parseCounterPromise = function(counter, callback) {
        counter.$promise.then(function(counter) {
            callback(counter.count);
        });
    };

    var parseArticlesPerPagePromise = function(articlesPerPage, callback) {
        articlesPerPage.$promise.then(function(counter) {
            callback(articlesPerPage.number);
        });
    };

    var setScopedNumPages = function($scope) {
        if ($scope.totalCount * $scope.articlesPerPage !== 0) {
            $scope.numPages = Math.ceil(
                $scope.totalCount / $scope.articlesPerPage
            );
        }
    };

    var articleList = [
        '$scope', '$routeParams', '$location', 'Util', 'service',
        function($scope, $routeParams, $location, Util, service) {
            var terms = $routeParams.search || '';

            $scope.currentPage     = parseInt($routeParams.page || '1', 10);
            $scope.articlesPerPage = 10;
            $scope.articles        = [ ];
            $scope.numPages        = 1;
            $scope.totalCount      = 0;

            parseArticlePromise(
                service.query({ search: terms, page: $scope.currentPage }),
                function(articles) { $scope.articles = articles; }
            );

            parseCounterPromise(
                service.count({ search: terms }),
                function(totalCount) { $scope.totalCount = totalCount; }
            );

            parseArticlesPerPagePromise(
                 Util.articlesPerPage(),function(articlesPerPage) {
                     $scope.articlesPerPage = articlesPerPage;
                 }
            );

            $scope.$watch('totalCount', function(newValue, oldValue) {
                setScopedNumPages($scope);
            });

            $scope.$watch('articles', function(newValue, oldValue) {
                setScopedNumPages($scope);
            });

            $scope.$watch('articlesPerPage', function(newValue, oldValue) {
                setScopedNumPages($scope);
            });

            $scope.changePage = function(pageNumber) {
                if (pageNumber >= 1 && pageNumber <= $scope.numPages)
                    $location.search('page', pageNumber);
            };
    }];

    var articleLatest = ['$scope', 'Article', function($scope, Article) {
        $scope.articles = [ ];
        parseArticlePromise(Article.latest(), function(articles) {
            $scope.articles = articles;
        });
    }];

    var articleAdd = [
        '$scope', '$location', 'Book', 'Comic', 'Movie', 'Music',
        function($scope, $location, Book, Comic, Movie, Music) {
            $scope.categories = [
                { name: 'Libros',    type: 'book',  service: Book  },
                { name: 'Cómics',    type: 'comic', service: Comic },
                { name: 'Películas', type: 'movie', service: Movie },
                { name: 'Música',    type: 'music', service: Music },
            ];

            $scope.add = function(category) {
                $scope.article.type = category.type;
                $scope.article.date = $scope.article.date.split('/').map(
                    function(num) { return parseInt(num, 10); }
                );

                category.service.save({ }, $scope.article);

                window.alert('Artículo ' + $scope.article.name + ' pendiente de moderación.');
                $location.path("/");
            };
        }
    ];

    controllers.controller('ArticleListController',       articleList  )
               .controller('ArticleLatestListController', articleLatest)
               .controller('ArticleAddController',        articleAdd   );

});
