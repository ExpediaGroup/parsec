#
# This directive generates an HTML table from a Parsec dataset
#
Parsec.Directives.directive 'parsecDataTable',
    ($compile, configService) ->
        getDisplayValue = (value) ->
            switch
                when _.isNull(value) then 'null'
                when _.isUndefined(value) then ''
                when _.isObject(value) then JSON.stringify(value)
                when _.isArray(value) then '[' + _.map(value, (i) -> getDisplayValue(i)).join(', ') + ']'
                else value

        {
            restrict: 'EA'
            scope: {
                dataset: '='
            }
            link: (scope, element, attrs) ->

                # Generate the table HTML from the dataset
                scope.$watch 'dataset', (dataSet) ->
                    return unless dataSet?

                    # Headers
                    template = '<table class="parsec-data-table table table-hover"><thead><tr>'

                    _.each dataSet.columns, (column) ->
                        template += '<th>' + column + '</th>'

                    template += '</tr></thead><tbody>'

                    # Data Rows
                    _.each dataSet.data, (row) ->
                        template += '<tr>'
                        _.each dataSet.columns, (column) ->
                            template += '<td>'
                            value = row[column]
                            template += getDisplayValue value
                            template += '</td>'
                        template += '</tr>'

                    template += '</tbody></table>'

                    # Rows
                    compiledValue = $compile(template)(scope)

                    # Replace the current contents with the newly compiled element
                    element.contents().remove()
                    element.append(compiledValue)

                    return
        }
