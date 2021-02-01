#
# This directive generates an Parsec editor and result output
#
Parsec.Directives.directive 'parsecEditor',
    ($compile, $window, parsecService, configService) ->

        {
            restrict: 'EA'
            scope: {
                query: '='
                showPerformance: '@'
            }
            templateUrl: '/partials/parsecEditor.html'

            controller: ($scope) ->

                $scope.tabs =
                    table:
                        active: true
                    variables:
                        active: false
                    raw:
                        active: false
                    query:
                        active: false

                $scope._showPerformance = true unless $scope.showPerformance == 'false'

                # Settings for the Query Editor
                $scope.aceOptions =
                    useWrapMode : true
                    showGutter: true
                    showPrintMargin: false
                    mode: 'parsec'
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

                        editor.getSession().selection.on 'changeSelection', (e) ->
                            $scope.$apply ->
                                range = editor.getSelectionRange()
                                $scope.editorSelectedText = editor.session.getTextRange(range)

                                # Determine if the selection is a function
                                range.start = _.clone range.end
                                range.end.column++
                                $scope.editorSelectedTextFunction = editor.session.getTextRange(range) == '('


                $scope.result = null

                $scope.openHelp = ->
                    q = $scope.editorSelectedText

                    # Hint for functions
                    if $scope.editorSelectedTextFunction
                        q = 'function:' + q

                    $window.open('/reference?q=' + q, 'parsecHelp')
                    return

                $scope.validate = ->
                    $scope.query.isExecuting = true
                    $scope.result = null

                    p = parsecService.validateQuery($scope.query.text)
                    p.then (result) ->
                        $scope.query.isExecuting = false
                        $scope.query.isFresh = true
                        $scope.result = result
                        $scope.tabs.query.active = true

                    p.catch (error) ->
                        $scope.isExecuting = false
                        console.log(error)

                $scope.execute = ->

                    # Update tab settings
                    $scope.query.isExecuting = true
                    $scope.result = null

                    p = parsecService.executeQuery($scope.query.text)
                    p.then (result) ->
                        $scope.query.isExecuting = false
                        $scope.query.isFresh = true
                        $scope.result = result

                        if result.dataSets.length == 0 or result.dataSets[0].count == 0
                            $scope.tabs.variables.active = true
                        else
                            $scope.tabs.table.active = true

                    p.catch (error) ->
                        $scope.query.isExecuting = false
                        console.log(error)

                $scope.$on 'executeParsecNow', -> $scope.execute()

                $scope.$watch 'query.text', ->
                    $scope.query.isSuccess = false

        }
