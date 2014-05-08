define(['services/services'], function(services) {
    'use strict';

    services.factory('Music', ['$resource', function($resource) {
        return $resource('rest/articles/music', { }, {
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
