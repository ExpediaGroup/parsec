Parsec.Services.factory 'crunchService',
    ($http, $q, configService) ->

        # Post-processes the Crunch result into more usable objects
        handleSuccess = (result) ->
            result = result.data;

            newResult = {
                raw: result
                summary: _.omit(result.summary, 'statistical')
            }

            if result.summary?.isSuccess
                # Array of all data sets in the result
                dataSetNames = _.keys result.data

                # Transform the data sets
                newResult.dataSets = _.map dataSetNames, (name, index) ->
                    stats = result.summary.statistical[name]
                    {
                        name: if name == '0' then 'Default Result' else name
                        count: stats.count
                        columns: stats.columns
                        statistics: stats.dataStatistics
                        data: result.data[dataSetNames[index]]
                        rawJSON: js_beautify(JSON.stringify(result.data[dataSetNames[index]]), {})
                    }
            else
                newResult.errors = result.summary.errors

            return newResult

        handleError = (error, status) ->
            error = switch status
                when 0 then "Unknown Crunch Service error occured.  No error message available due to missing CORS headers."
                when 404 then "Not Found: The requested resource is not available."
                else error

            return {
                errors: [
                    { key: status, value: error }
                ]
            }

        # Conversion functions for converting a Crunch query to a Parsec query
        crunchToParsecConversions = [
            (q) -> q.trim()

            # Fix Bracketed strings
            (q) ->
                str = []
                inSingleQuotedString = false
                inDoubleQuotedString = false
                inBracketedString = false
                _.each q, (char, index) ->
                    if char == "'"
                        if inBracketedString or inDoubleQuotedString
                            str.push char
                        else
                            inSingleQuotedString = !inSingleQuotedString
                            str.push char
                    else if char == '"'
                        if inBracketedString
                            str.push '\\'
                            str.push char
                        else if inSingleQuotedString
                            str.push char
                        else
                            inDoubleQuotedString = !inDoubleQuotedString
                            str.push char
                    else if char == '[' and !inSingleQuotedString and !inDoubleQuotedString
                        inBracketedString = true
                        str.push '"'
                    else if char == ']' and inBracketedString
                        inBracketedString = false
                        str.push '"'
                    else
                        str.push char
                return str.join('')

            # Mocklarge Data Source
            (q) -> q.replace /source(?:\s*)=(?:\s*)mocklarge([^\|]*)/ig, (match, options) ->
                options = options.replace /\s+count=/ig, ' n='

                'input mocklarge' + options

            (q) -> q.replace /source(?:\s*)=(?:\s*)mock/ig, 'input mock'

            # NET Data Source
            (q) -> q.replace /source(?:\s*)=(?:\s*)net([^\|]*)/ig, (match, options) ->
                options = options.replace /\s+u=/ig, ' user='
                options = options.replace /\s+pw=/ig, ' password='

                # Mode option
                modeMatch = options.match /\s+mode(?:\s*)=(?:\s*)["'](.*?)["']/i
                mode = if modeMatch.length == 2 then modeMatch[1].toLowerCase() else null
                options = options.replace /\s+mode=['"](.*?)['"]/i, ' parser="' + mode + '"'

                # Format option
                options = options.replace /\s+format(?:\s*)=(?:\s*)["'](.*?)["']/ig, (match, format) ->
                    if mode== 'xml'
                        ' xpath="' + format + '"'
                    else if mode== 'json'
                        ' jsonpath="' + format + '"'
                    else
                        ' format="' + format + '"'

                options = options.replace /\s+postdata=/ig, ' body='

                'input http' + options

            # JDBC Data Source
            (q) -> q.replace /source(?:\s*)=(?:\s*)jdbc([^\|]*)/ig, (match, options) ->
                options = options.replace /\s+driver=/ig, ' uri='
                options = options.replace /\s+u=/ig, ' user='
                options = options.replace /\s+pw=/ig, ' password='

                jdbcType = options.match /jdbc:(.*?):\/\//
                if jdbcType?.length == 2
                    # DB2 JDBC Driver
                    if jdbcType[1].toLowerCase() == 'db2'
                        user = options.match /\s+(?:user|username)=['"](.*?)['"]/i
                        password = options.match /\s+password=['"](.*?)['"]/i
                        return unless user.length == 2 and password.length == 2

                        options = options.replace /\s+uri=['"](.*?)['"]/i, (match, uri) ->
                            ' uri="' + uri + ':user=' + user[1] + ';password=' + password[1] + ';"'

                        # Remove user/password options since they are included in the URI
                        options = options.replace /\s+(?:user|username)=['"](.*?)['"]/i, ' '
                        options = options.replace /\s+password=['"](.*?)['"]/i, ' '

                'input jdbc' + options

            # SEARCH statement
            (q) -> q.replace /\|(\s*)search\s/ig, '| filter '

            # OUTPUT statement
            (q) -> q.replace /\|(?:\s*)output(?:\s*)=(?:\s*)(\S+)/ig, '| output name="$1"'

            # JOIN statements
            (q) -> q.replace /\|(.*?)join(?:\s*)\(?(.*?)\)?(?:\s*)on(?:\s*)(\S+)(?:\s*)=(?:\s*)(\S+)/ig, '|$1join left, ($2) right on left.$3 == right.$4'

            # Date functions
            (q) -> q.replace /current\(\)/ig, 'now()'
        ]

        service = {
            executeQuery: (query, crunchServiceUrl = configService.selectedCrunchService.url) ->
                url = crunchServiceUrl + 'json/json'

                query = query.replace /\\u[0-9]{4}/g, (unicode) ->
                    eval('("' + unicode + '")')

                requestData = {
                    decode: false
                    query: query
                    validate: false
                }

                # Perform crunch POST and add callback handlers to promise
                $http.post(url, requestData)
                .then handleSuccess
                .catch handleError


            convertQuery: (query) ->
                _.reduce crunchToParsecConversions, (result, fn) ->
                    fn result
                , query
        }

        return service
