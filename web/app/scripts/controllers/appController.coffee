Parsec.Controllers.controller 'AppController',
    ($scope, $window, $localForage, configService) ->

        $scope.serviceList = configService.parsec.serviceList

        $scope.selectParsecService = (service) ->
            $scope.selectedParsecService = service

        $localForage.bind $scope, {
            key: 'selectedParsecService'
            defaultValue: configService.parsec.serviceList[0].name
        }
