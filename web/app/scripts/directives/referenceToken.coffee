#
# This directive generates a language reference token by name
#
Parsec.Directives.directive 'referenceToken',
    ($compile, parsecService, documentationService) ->

        {
            restrict: 'EA'
            scope: {
                key: '@'
                token: '=?'
            }
            templateUrl: '/partials/referenceToken.html'

            controller: ($scope) ->

                if not $scope.token?
                    $scope.token = _.find documentationService.tokens, { key: $scope.key }

                $scope.selectToken = ->
                    $scope.$emit 'selectedToken', $scope.token

        }
