define(['services/services'], function(services) {
    'use strict';

    services.factory('Movie', ['$resource', function($resource) {
        return $resource('rest/articles/movies', { }, {
            count: {
                method: 'GET',
                params: { count: true },
                transformResponse: [function(data, headers) {
                    return { count: parseInt(data, 10) };
                }]
            },
        });
    }]);

});
