Parsec.Controllers.controller 'CrunchController',
    ($scope, $anchorScroll, $location, $localForage, configService, crunchService) ->

        $scope.showPerformance = false

        # Settings for the Crunch Query Editor
        $scope.crunchAceOptions =
            useWrapMode : true
            showGutter: true
            showPrintMargin: false
            mode: 'crunch'
            theme: 'dawn'
            onLoad: (editor) ->
                editor.navigateFileEnd()
                editor.focus()

                editor.setOption("maxLines", 48)
                editor.setOption("minLines", 10)
                editor.$blockScrolling = Infinity

        $localForage.bind $scope, {
            key: 'crunchController-crunchQuery'
            defaultValue: { text: 'SOURCE=mock' }
            scopeKey: 'crunchQuery'
        }

        $localForage.bind $scope, {
            key: 'crunchController-parsecQuery'
            defaultValue: { text: 'input mock' }
            scopeKey: 'parsecQuery'
        }

        $scope.convert = ->
            $scope.parsecQuery.text = crunchService.convertQuery $scope.crunchQuery.text
            $scope.$broadcast 'executeParsecNow'
