define(['services/services'], function(services) {
    'use strict';

    services.factory('Book', ['$resource', function($resource) {
        return $resource('rest/articles/books', { }, {
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
