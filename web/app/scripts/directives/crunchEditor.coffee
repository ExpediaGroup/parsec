#
# This directive generates a Crunch editor and result output
#
Parsec.Directives.directive 'crunchEditor',
    ($compile, crunchService, configService) ->

        {
            restrict: 'EA'
            scope: {
                query: '='
                showPerformance: '@'
            }
            templateUrl: '/partials/crunchEditor.html'

            controller: ($scope, $localForage, configService) ->

                $scope.serviceList = configService.crunch.serviceList

                $localForage.removeItem $scope, 'selectedCrunchService'
                $localForage.bind $scope, {
                    key: 'selectedCrunchService'
                    defaultValue: configService.crunch.serviceList[0]
                }

                $scope._showPerformance = true unless $scope.showPerformance == 'false'

                # Settings for the Query Editor
                $scope.aceOptions =
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

                        editor.commands.addCommand {
                            name: 'executeQuery',
                            bindKey:
                                win: 'Ctrl-E',
                                mac: 'Command-E',
                                sender: 'editor|cli'
                            exec: (env, args, request) ->
                                $scope.execute()
                        }

                $scope.result = null

                $scope.execute = ->

                    # Update tab settings
                    $scope.query.isExecuting = true
                    $scope.result = null

                    p = crunchService.executeQuery $scope.query.text, $scope.selectedCrunchService.url
                    p.then (result) ->
                        $scope.query.isExecuting = false
                        $scope.query.isFresh = true
                        $scope.result = result

                    p.catch (error) ->
                        $scope.query.isExecuting = false
                        console.log(error)

                $scope.$on 'executeCrunchNow', -> $scope.execute()

        }
