require.config({
    paths: {
        jquery:              '../webjars/jquery/1.10.2/jquery.min',
        bootstrap:           '../webjars/bootstrap/3.1.1/js/bootstrap.min',
        bootstrapDatePicker: '../webjars/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker',
        angular:             '../webjars/angularjs/1.2.15/angular.min',
        angularRoute:        '../webjars/angularjs/1.2.15/angular-route.min',
        angularResource:     '../webjars/angularjs/1.2.15/angular-resource.min',
        angularUpload:       '../webjars/angular-file-upload/1.2.8/angular-file-upload.min',
        domReady:            '../webjars/requirejs-domready/2.0.1/domReady',
    },
    shim: {
        bootstrap: {
            deps: ['jquery']
        },
        bootstrapDatePicker: {
            deps: ['bootstrap', 'jquery']
        },
        angular: {
            deps: ['jquery'],
            exports: 'angular'
        },
        angularRoute: {
            deps: ['angular']
        },
        angularResource: {
            deps: ['angular']
        },
        angularUpload: {
            deps: ['angular']
        }
    },
    priority: ['angular']
});

require([
    'angular',
    'app',
    'routes',
    'bootstrap',
    'bootstrapDatePicker'
], function(angular) {
    'use strict';

    require(['domReady!'], function(document) {
        angular.bootstrap(document, ['daaTarde']);
    });

});
