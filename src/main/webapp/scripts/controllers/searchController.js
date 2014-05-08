define(['controllers/controllers'], function(controllers) {
    'use strict';

    controllers.controller('SearchController', [
        '$scope', '$location', function($scope, $location) {
            $scope.terms = '';

            $scope.categories = [
                { name: 'Todo',      path: '/articles'          },
                { name: 'Libros',    path: '/articles/books'    },
                { name: 'Cómics',    path: '/articles/comics'   },
                { name: 'Películas', path: '/articles/movies'   },
                { name: 'Música',    path: '/articles/music'    },
            ];

            $scope.selectedCategory = $scope.categories.filter(function(c) {
                return c.path === $location.path();
            })[0] || $scope.categories[0];

            $scope.search = function(category) {
                $scope.selectedCategory = category;
                $location.path(category.path)
                    .search('search', $scope.terms)
                    .search('page', 1);
            };

            $scope.enter = function(ev) {
                if (ev.which == 13) $scope.search($scope.selectedCategory);
            };
        }
    ]);

});
