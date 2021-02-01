#
# This directive generates an inline example editor and results
#
Parsec.Directives.directive 'exampleQuery',
    ($compile, parsecService, configService) ->

        {
            restrict: 'EA'
            scope: {
                query: '@'
            }
            templateUrl: '/partials/exampleQuery.html'

            controller: ($scope) ->

                $scope.aceOptions =
                    useWrapMode: false
                    showGutter: false
                    showPrintMargin: false
                    mode: 'parsec'
                    theme: 'dawn'
                    onLoad: (editor) ->
                        editor.setOption 'minLines', 1
                        editor.setOption 'maxLines', 5
                        editor.setOption 'highlightActiveLine', false
                        editor.$blockScrolling = Infinity

                $scope.execute = ->
                    p = parsecService.executeQuery($scope.query, configService.selectedParsecService.url)
                    $scope.error = null
                    $scope.executing = true
                    p.then (result) ->
                        $scope.executing = false
                        $scope.result = result.dataSets['0']
                        if result.errors?.length > 0
                            $scope.error = result.errors[0]

                        if result.variables?
                            $scope.variables = result.variables
                        else
                            $scope.variables = null

                        $scope.complete = true

                $scope.hideResult = ->
                    $scope.complete = false

        }
