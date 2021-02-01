#
# Ace - Crunch Mode and Syntax Highlighting
#
ace.define('ace/mode/crunch', [
        'require', 'exports', 'module', 'ace/lib/oop',
        'ace/mode/text', 'ace/tokenizer', 'ace/mode/crunch_highlight_rules',
        'ace/range'],

    (require, exports, module) ->
        oop = require("../lib/oop")
        TextMode = require("./text").Mode
        Tokenizer = require("../tokenizer").Tokenizer
        CrunchHighlightRules = require("./crunch_highlight_rules").CrunchHighlightRules
        Range = require("../range").Range

        Mode = ->
            this.HighlightRules = CrunchHighlightRules
            return

        oop.inherits(Mode, TextMode)

        (-> this.lineCommentStart = '/*').call(Mode.prototype)

        exports.Mode = Mode
        return
)

ace.define('ace/mode/crunch_highlight_rules', [
        'require', 'exports', 'module',
        'ace/lib/oop', 'ace/mode/text_highlight_rules'],

    (require, exports, module) ->
        oop = require("../lib/oop")
        TextHighlightRules = require("./text_highlight_rules").TextHighlightRules

        CrunchHighlightRules = ->
            keywords = (

                "source|option|search|sort|asc|desc|select|stats|by" +
                    "|timeseries|span|per_day|per_hour|per_min|per_second" +
                    "|union|join|left|right|outer|inner|on" +
                    "|cache|id|ttl|maxage" +
                    "|head|tail|page" +
                    "|year|month|day|hour|second|millis" +
                    "|pivot|per|format|name|long|short|simple|output|temp|partition|row_number"
            )

            builtinConstants = (
                "true|false|null"
            )

            builtinFunctions = (
                #Stat Functions */
                "avg|count|distinct_count|earliest|first|geomean|last" +
                    "|latest|max|mean|median|min|mode|exactperc|product|range" +
                    "|stdev|stdevp|sum|sumsq|sumlog|var|varp" +

                #String Functions */
                "|ltrim|md5|lower|len|replace|rtrim|substr|tostring|trim|upper" +

                #Date Functions */
                "|todate|dateadd|current|getcenturyofera|getdateofmonth|getdateofweek" +
                    "|getdateofyear|getera|gethourofdat|getmillisofday|getmillisofsecond" +
                    "|getminuteofday|getminuteofhour|getmonthofyear|getsecondofday|getsecondofminute" +
                    "|getweekofweekyear|getweekyear|getyear|getyearofcentury|getyearofera" +
                    "|startofmonth|startofweek|startofquarter|startofyear" +

                #Conditional Functions */
                "|case|if" +

                #Math Functions */
                "|abs|ceil|cbrt|exact|exp|floor|ln|log|pi|pow|random" +
                    "|round|rounddown|sigfig|sqrt|tonumber" +
                    "|cos|cosh|sin|sinh|tan|tanh" +

                    "|isexist|isnull|isempty|isemptyornull|isdatetime" +
                    "|isdouble|isinteger|isnumber|isstring" +
                    "|iscurrentweek|isnextweek|islastweek"
            )

            keywordMapper = this.createKeywordMapper({
                "support.function": builtinFunctions,
                "keyword": keywords,
                "constant.language": builtinConstants
            }, "identifier", true)

            this.$rules = {
                "start": [{
                    token: "comment",
                    regex: "/\\*.*\\*/"
                }, {
                    token: "string",           # " string
                    regex: '".*?"'
                }, {
                    token: "string",           # ' string
                    regex: "'.*?'"
                }, {
                    token: "constant.numeric", # float
                    regex: "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
                }, {
                    token: keywordMapper,
                    regex: "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
                }, {
                    token: "keyword.operator",
                    regex: "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|=|and|or|not"
                }, {
                    token: "paren.lparen",
                    regex: "[\\(]"
                }, {
                    token: "paren.rparen",
                    regex: "[\\)]"
                }, {
                    token: "keyword.control",
                    regex: "\\|"
                }, {
                    token: "text",
                    regex: "\\s+"
                } ]
            }
            return

        oop.inherits(CrunchHighlightRules, TextHighlightRules)

        exports.CrunchHighlightRules = CrunchHighlightRules
        return
)

