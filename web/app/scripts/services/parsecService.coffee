Parsec.Services.factory 'parsecService',
    ($http, $q, configService) ->

        cleanQuery = (query) ->
            query.replace /\\u[0-9]{4}/g, (unicode) ->
                eval('("' + unicode + '")')

        humanize = (milliseconds) ->
            switch
                when milliseconds < 1000 then milliseconds + ' ms'
                when milliseconds < 60000 then (milliseconds / 1000).toFixed(2) + ' sec'
                else (milliseconds / 60000).toFixed(2) + ' min'

        service = {
            validateQuery: (query, url = configService.selectedParsecService.url) ->
                deferred = $q.defer()

                $http.post(url + '/validate', { query: cleanQuery(query) })
                .success (result) ->
                    result.valid = (result.errors.length == 0)

                    if result.performance?
                        result.performance2 =
                            parse: humanize(result.performance.parse)

                    deferred.resolve(result)
                .error (error, status) ->
                    deferred.reject(error)

                return deferred.promise

            executeQuery: (query, url = configService.selectedParsecService.url) ->
                deferred = $q.defer()

                $http.post(url + '/execute', { query: cleanQuery(query) })
                    .success (result) ->
                        if result.performance?
                            result.performance2 =
                                total: humanize(result.performance.total)
                                parse: humanize(result.performance.parse)
                                compile: humanize(result.performance.compile)
                                execute: humanize(result.performance.execute)

                        _.each result.dataSets, (dataSet) ->
                            dataSet.rawJSON = js_beautify(JSON.stringify(dataSet.data), {})

                        console.log result
                        deferred.resolve(result)
                    .error (error, status) ->
                        deferred.reject(error)

                return deferred.promise
        }

        return service
