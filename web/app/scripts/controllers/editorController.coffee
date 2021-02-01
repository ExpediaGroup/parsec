Parsec.Controllers.controller 'EditorController',
    ($scope, $location, $localForage, configService) ->

        $scope.tabid = 0
        $scope.tabs = []
        $scope.selectedTab = null

        $scope.sidebarCollapsed = false

        $scope.newTab = (query = 'input mock', name = null) ->
            newTab =
                name: name
                id: $scope.tabid++
                active: true
                query:
                    text: query

            $scope.tabs.push newTab

        $scope.removeTab = (tab) ->
            return unless $scope.tabs.length > 1

            _.remove($scope.tabs, tab)

        $scope.selectTab = (tab) ->
            $scope.selectedTab = tab

        $scope.getActiveTab = ->
            _.find($scope.tabs, { active: true })

        $scope.selectQueryTemplate = (item) ->
            $scope.newTab item.query, item.name

        $scope.queryTemplates = configService.parsec.queryTemplates

        #
        # Initialization
        #
        $localForage.bind($scope, {
            key: 'editorController-tabs'
            defaultValue: []
            scopeKey: 'tabs'
        }).then (tabs) ->
            if tabs?.length > 0
                $scope.tabs = tabs

                # Initialize tabid from what was loaded
                $scope.tabid = _.max(_.pluck(tabs, 'id')) + 1
            else
                # Nothing loaded from storage, so create a new tab
                console.log 'Creating a tab'
                $scope.newTab()


        # Select current active tab to load history
        #$scope.selectTab(_.find($scope.tabs, { active: true }))
