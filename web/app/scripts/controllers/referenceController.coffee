Parsec.Controllers.controller 'ReferenceController',
    ($scope, $anchorScroll, $location, configService, documentationService, filterFilter) ->

        $scope.tokens = documentationService.tokens

        $scope.tokenTypes = _($scope.tokens).map((token) -> token.type).uniq().value()
        $scope.tokenTypeFilter = null

        $scope.menuSorter = (token) ->
            # Crazy prefix to ensure token is sorted at the top
            return '!"_' + token.name if token.name.indexOf($scope.tokenFilter) == 0
            token.name

        $scope.toggleTypeFilter = (tokenType) ->
            if $scope.tokenTypeFilter == tokenType
                $scope.tokenTypeFilter = null
                $scope.tokens = documentationService.tokens
            else
                $scope.tokenTypeFilter = tokenType
                $scope.tokens = _.filter(documentationService.tokens, { type: tokenType })

        $scope.isExcluded = (tokenType) ->
            return false if $scope.tokenTypeFilter == null
            $scope.tokenTypeFilter != tokenType

        $scope.getToken = (tokenName) ->
            _.find $scope.tokens, { name: tokenName }

        $scope.selectToken = (token) ->
            $scope.selectedToken = token

        $scope.search = (search) ->
            $scope.tokenFilter = search

        $scope.feelingLucky = ->
            # Filter and select first item
            $scope.selectedToken = _.first _.sortBy filterFilter($scope.tokens, $scope.tokenFilter), $scope.menuSorter

        $scope.scrollToTop = ->
            $anchorScroll('top')

        if $location.search().v?
            $scope.selectedToken = _.find documentationService.tokens, { key: $location.search().v }

        if $location.search().q?
            $scope.tokenFilter = $location.search().q
            $scope.feelingLucky()

        $scope.$on 'selectedToken', (event, token) ->
            $scope.selectedToken = token

        $scope.$watch 'selectedToken', (token) ->
            return unless token?
            $location.search 'v', token.key
            $scope.scrollToTop()

        $scope.$watch 'tokenFilter', (q) ->
            $location.search 'q', q
