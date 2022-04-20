ace.define(
  'ace/theme/parsec_dark',
  ['require', 'exports', 'module', 'ace/lib/dom'],
  function (require, exports, module) {
    exports.isDark = false;
    exports.cssClass = 'ace-parsec-dark';
    exports.cssText =
      ".ace-parsec-dark .ace_gutter {\
        background: #25242e;\
        color: #CCC\
        }\
        .ace-parsec-dark .ace_print-margin {\
        width: 1px;\
        background: #25242e\
        }\
        .ace-parsec-dark {\
        background-color: #25242e;\
        color: #CCCCCC\
        }\
        .ace-parsec-dark .ace_constant.ace_other,\
        .ace-parsec-dark .ace_cursor {\
        color: #CCCCCC\
        }\
        .ace-parsec-dark .ace_marker-layer .ace_selection {\
        background: #515151\
        }\
        .ace-parsec-dark.ace_multiselect .ace_selection.ace_start {\
        box-shadow: 0 0 3px 0px #2D2D2D;\
        }\
        .ace-parsec-dark .ace_marker-layer .ace_step {\
        background: rgb(102, 82, 0)\
        }\
        .ace-parsec-dark .ace_marker-layer .ace_bracket {\
        margin: -1px 0 0 -1px;\
        border: 1px solid #6A6A6A\
        }\
        .ace-tomorrow-night-bright .ace_stack {\
        background: rgb(66, 90, 44)\
        }\
        .ace-parsec-dark .ace_marker-layer .ace_active-line {\
        background: #393939\
        }\
        .ace-parsec-dark .ace_gutter-active-line {\
        background-color: #393939\
        }\
        .ace-parsec-dark .ace_marker-layer .ace_selected-word {\
        border: 1px solid #515151\
        }\
        .ace-parsec-dark .ace_invisible {\
        color: #6A6A6A\
        }\
        .ace-parsec-dark .ace_keyword,\
        .ace-parsec-dark .ace_meta,\
        .ace-parsec-dark .ace_storage,\
        .ace-parsec-dark .ace_storage.ace_type,\
        .ace-parsec-dark .ace_support.ace_type {\
        color: #CC99CC\
        }\
        .ace-parsec-dark .ace_keyword.ace_operator {\
        color: #66CCCC\
        }\
        .ace-parsec-dark .ace_constant.ace_character,\
        .ace-parsec-dark .ace_constant.ace_language,\
        .ace-parsec-dark .ace_constant.ace_numeric,\
        .ace-parsec-dark .ace_keyword.ace_other.ace_unit,\
        .ace-parsec-dark .ace_support.ace_constant,\
        .ace-parsec-dark .ace_variable.ace_parameter {\
        color: #F99157\
        }\
        .ace-parsec-dark .ace_invalid {\
        color: #CDCDCD;\
        background-color: #F2777A\
        }\
        .ace-parsec-dark .ace_invalid.ace_deprecated {\
        color: #CDCDCD;\
        background-color: #CC99CC\
        }\
        .ace-parsec-dark .ace_fold {\
        background-color: #6699CC;\
        border-color: #CCCCCC\
        }\
        .ace-parsec-dark .ace_entity.ace_name.ace_function,\
        .ace-parsec-dark .ace_support.ace_function,\
        .ace-parsec-dark .ace_variable {\
        color: #6699CC\
        }\
        .ace-parsec-dark .ace_support.ace_class,\
        .ace-parsec-dark .ace_support.ace_type {\
        color: #FFCC66\
        }\
        .ace-parsec-dark .ace_heading,\
        .ace-parsec-dark .ace_markup.ace_heading,\
        .ace-parsec-dark .ace_string {\
        color: #99CC99\
        }\
        .ace-parsec-dark .ace_comment {\
        color: #999999\
        }\
        .ace-parsec-dark .ace_entity.ace_name.ace_tag,\
        .ace-parsec-dark .ace_entity.ace_other.ace_attribute-name,\
        .ace-parsec-dark .ace_meta.ace_tag,\
        .ace-parsec-dark .ace_variable {\
        color: #F2777A\
        }'";
    var dom = require('../lib/dom');
    dom.importCssString(exports.cssText, exports.cssClass, false);
  }
);
(function () {
  ace.require(['ace/theme/parsec_dark'], function (m) {
    if (typeof module == 'object' && typeof exports == 'object' && module) {
      module.exports = m;
    }
  });
})();
