define(['services/services'], function(services) {
    'use strict';

    services.service('Util', ['$resource', function($resource) {
        return $resource('rest/util', { }, {
            articlesPerPage: {
                method: 'GET',
                url:    'rest/util/articles_per_page',
                transformResponse: [function(data, headers) {
                    return { number: parseInt(data, 10) };
                }]
            },
        });
    }]);

});
