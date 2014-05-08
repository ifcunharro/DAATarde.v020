define(['services/services'], function(services) {
    'use strict';

    services.factory('Article', ['$resource', function($resource) {
        return $resource('rest/articles', { }, {
            count: {
                method: 'GET',
                params: { count: true },
                transformResponse: [function(data, headers) {
                    return { count: parseInt(data, 10) };
                }]
            },
            latest: {
                method:  'GET',
                url:     'rest/articles/latest',
                isArray: true,
            },
        });
    }]);

});
