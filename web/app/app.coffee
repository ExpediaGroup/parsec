Parsec = angular.module('parsec', [
    'ui.router'
    'ui.bootstrap'
    'parsec.controllers'
    'parsec.directives'
    'parsec.services'
    'headroom'
    'ui.ace'
    'ui.keypress'
    'ui.select'
    'truncate'
    'ngNumeraljs'
    'LocalForageModule'
])

Parsec.Controllers = angular.module('parsec.controllers', ['LocalForageModule'])
Parsec.Services = angular.module('parsec.services', ['LocalForageModule'])
Parsec.Directives = angular.module('parsec.directives', ['parsec.services', 'LocalForageModule'])

Parsec.config ($locationProvider, $stateProvider, $urlRouterProvider, uiSelectConfig) ->
    uiSelectConfig.theme = 'selectize'

    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: '/partials/home.html',
            controller: 'HomeController',
            data:
                title: 'Parsec'
        })
        .state('quickstart', {
            url: '/quickstart',
            templateUrl: '/partials/quickstart.html',
            controller: 'HomeController',
            data:
                title: 'Parsec'
        })
        .state('editor', {
            url: '/editor',
            templateUrl: '/partials/editor.html',
            controller: 'EditorController',
            data:
                title: 'Parsec Editor'
        })
        .state('reference', {
            url: '/reference',
            templateUrl: '/partials/reference.html',
            controller: 'ReferenceController',
            data:
                title: 'Parsec Reference'
        })
        .state('crunch', {
            url: '/crunch',
            templateUrl: '/partials/crunch.html',
            controller: 'CrunchController',
            data:
                title: 'Parsec for Crunch Users'
        })
        .state('javainterop', {
            url: '/javainterop',
            templateUrl: '/partials/javainterop.html',
            data:
                title: 'Java Interop'
        })

    # Catch all
    $urlRouterProvider.otherwise('/home')

    $locationProvider.html5Mode(true)

Parsec.run ($anchorScroll, $rootScope, $stateParams) ->

    # Initialize state at home to avoid a FOUC
    $rootScope.$state = { name: 'home' }

    scrollToTop = ->
        $anchorScroll()

    # Set the page title based on the route.
    $rootScope.page_title = 'Parsec'
    $rootScope.$on '$stateChangeSuccess', (event, toState, fromState) ->
        if toState?
            $rootScope.$state = toState
            $rootScope.$stateParams = $stateParams
            $rootScope.page_title = toState.data.title
            scrollToTop()



