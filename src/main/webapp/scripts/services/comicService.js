define(['services/services'], function(services) {
    'use strict';

    services.factory('Comic', ['$resource', function($resource) {
        return $resource('rest/articles/comics', { }, {
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
