#
# Ace - Parsec Mode and Syntax Highlighting
#
ace.define('ace/mode/parsec', [
        'require', 'exports', 'module', 'ace/lib/oop',
        'ace/mode/text', 'ace/tokenizer', 'ace/mode/parsec_highlight_rules',
        'ace/range'],

    (require, exports, module) ->
        oop = require('../lib/oop')
        TextMode = require('./text').Mode
        Tokenizer = require('../tokenizer').Tokenizer
        ParsecHighlightRules = require('./parsec_highlight_rules').ParsecHighlightRules
        Range = require('../range').Range

        Mode = ->
            this.HighlightRules = ParsecHighlightRules
            return

        oop.inherits(Mode, TextMode)

        (-> this.lineCommentStart = '/*').call(Mode.prototype)

        exports.Mode = Mode
        return
)

ace.define('ace/mode/parsec_highlight_rules', [
        'require', 'exports', 'module',
        'ace/lib/oop', 'ace/mode/text_highlight_rules'],

    (require, exports, module) ->
        oop = require('../lib/oop')
        TextHighlightRules = require('./text_highlight_rules').TextHighlightRules

        ParsecHighlightRules = ->
            storage = [
                '|set|def'
            ].join(',')

            keywords = [
                # Statements
                'input|output|temp'
                '|filter|remove|rename|reverse|rownumber|sample|select|sleep|unselect'
                '|head|tail|sort|asc|desc'
                '|stats|by|where'
                '|timeseries|span|per_day|per_hour|per_min|per_second'
                '|union|distinct|all'
                '|join|natural|left|right|outer|inner|cross|full|on'
                '|project'
                '|year|month|day|hour|second|millis'
                '|pivot|per|unpivot'
            ].join('')

            dataSources = 'datastore|graphite|http|influxdb|jdbc|mock|mocklarge|mongodb|s3|smb'

            constants = 'null'

            booleans = 'true|false'

            functions = [
                # Aggregation Functions
                'avg|count|cumulativeavg|cumulativemean|distinctcount|every|first|geometricmean'
                '|last|max|mean|median|min|percentile|pluck|some|stddev|stddev_pop|stddev_samp'
                '|stddevp|sum'

                # Conditional Functions
                '|case|coalesce|if'

                # Constant functions
                '|e|infinity|nan|neginfinity|pi'

                # Conversion Functions
                '|toboolean|tostring|tonumber|tointeger|todouble|todate|tolist|tomap'

                # Date/Time Functions
                '|adddays|addhours|addmilliseconds|addminutes|addmonths|addseconds|addweeks'
                '|addyears|day|dayofweek|dayofyear|earliest|hour|indays|inhours|inmillis'
                '|inminutes|inmonths|inseconds|interval|inweeks|inyears|isbetween|latest'
                '|millisecond|millisecondofday|minusdays|minushours|minusmilliseconds'
                '|minusminutes|minusmonths|minusseconds|minusweeks|minusyears|minute'
                '|minuteofday|month|now|nowutc|period|second|secondofday|startofday'
                '|startofhour|startofminute|startofmonth|startofweek|startofyear'
                '|timezones|today|todayutc|toepoch|toepochmillis|tolocaltime|tomorrow'
                '|tomorrowutc|totimezone|toutctime|year|yesterday|yesterdayutc'

                # Digest Functions
                '|adler32|crc32|gost|hmac_gost|hmac_md5|hmac_ripemd128|hmac_ripemd256'
                '|hmac_ripemd320|hmac_sha1|hmac_sha256|hmac_sha3_224|hmac_sha3_256|hmac_sha3_384'
                '|hmac_sha3_512|hmac_sha512|hmac_tiger|hmac_whirlpool|md5|ripemd128|ripemd256'
                '|ripemd320|sha1|sha256|sha3_224|sha3_256|sha3_384|sha3_512|sha512|siphash'
                '|siphash48|tiger|whirlpool'

                # Execution Functions
                '|exec'

                # Functional Functions
                '|apply|filter|map|mapcat|mapvalues'

                # Is Functions
                '|isempty|isexist|isfinite|isinfinite|isnan|isnull'

                # List Functions
                '|concat|contains|distinct|flatten|flattendeep|index|length'
                '|listmax|listmean|listmin|liststddev|liststddevp|lmax|lmean|lmin'
                '|peek|peeklast|pop|push|range|reverse'

                # Map Functions
                '|delete|get|keys|merge|set|values'

                # Math Functions
                '|abs|ceil|floor|gcd|greatest|lcm|least|ln|log|pow|random|round|sign|sqrt|within|within%'

                # Parse Functions
                '|parsecsv|parsejson|parsexml'

                # Probability Functions
                '|cdfnormal|cdfpoisson|cdfuniform|pdfnormal|pdfpoisson|pdfuniform'

                # String Functions
                '|base64decode|base64encode|endswith|indexof|join|lastindexof|len|lowercase|ltrim'
                '|replace|replaceall|rtrim|split|startswith|substr|substring|trim|uppercase|urldecode|urlencode'

                # Trig Functions
                '|acos|asin|atan|atan2|cos|cosh|degrees|radians|sin|sinh|tan|tanh'

                # Type Functions
                '|isboolean|isdate|isdouble|isinteger|islist|ismap|isnumber|isstring|type'

                # Etc
                '|bucket|rank'

            ].join('')

            keywordMapper = this.createKeywordMapper({
                'support.type': dataSources
                'support.function': functions
                'keyword': keywords
                'storage.modifier': storage
                'constant.language': constants
                'constant.language.boolean': booleans
            }, 'identifier', true)

            this.$rules = {
                'start': [{
                    token: 'comment.line.double-slash',
                    regex: '//.*$'
                }, {
                    token: 'comment.block',
                    regex: '/\\*.*\\*/'
                }, {
                    token: "string.quoted.single", # ' string
                    regex: "'.*?'"
                }, {
                    token: "string.quoted.double", # " string
                    regex: '".*?"'
                }, {
                    token: "string.quoted.triple", # ''' string
                    regex: "'''.*?'''"
                }, {
                    token: 'variable.parameter', # @ parameters
                    regex: "@[a-zA-Z0-9_]+[\']?"
                }, {
                    token: 'constant.numeric', # float
                    regex: '[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b'
                }, {
                    token: 'keyword.operator',
                    regex: '\\+|\\-|\\*|\\/|mod|\\^|<|>|<=|=>|==|!=|=|and|&&|or|\\|\\||xor|\\^\\^|not|\!|:|%|->'
                }, {
                    token: keywordMapper,
                    regex: '[a-zA-Z_$][a-zA-Z0-9_$]*\\b'
                }, {
                    token: keywordMapper,
                    regex: '`[^`]+`'
                }, {
                    token: 'paren.lparen',
                    regex : '[\\[({]'
                }, {
                    token: 'paren.rparen',
                    regex : '[\\])}]'
                }, {
                    token: 'keyword.control',
                    regex: '\\|'
                }, {
                    token: 'text',
                    regex: '\\s+'
                }]
            }
            return

        oop.inherits(ParsecHighlightRules, TextHighlightRules)

        exports.ParsecHighlightRules = ParsecHighlightRules
        return
)

