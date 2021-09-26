ace.define('ace/theme/parsec', ['require', 'exports', 'module', 'ace/lib/dom'], function (require, exports, module) {
  exports.isDark = false;
  exports.cssClass = 'ace-parsec';
  exports.cssText =
    '.ace-parsec .ace_gutter {\
        background: #f8f8f8;\
        color: rgb(168, 128, 194);\
      }\
      \
      .ace-parsec .ace_print-margin {\
        width: 1px;\
        background: #e8e8e8;\
      }\
      \
      .ace-parsec {\
        background-color: #f8f8f8;\
        color: #58088c;\
      }\
      \
      .ace-parsec .ace_cursor {\
        color: #8ba7a7;\
      }\
      \
      .ace-parsec .ace_marker-layer .ace_selection {\
        background: rgba(197, 223, 233, 0.5);\
      }\
      \
      .ace-parsec.ace_multiselect .ace_selection.ace_start {\
        box-shadow: 0 0 3px 0px #f8f8f8;\
        border-radius: 2px;\
      }\
      \
      .ace-parsec .ace_marker-layer .ace_step {\
        background: rgb(198, 219, 174);\
      }\
      \
      .ace-parsec .ace_marker-layer .ace_bracket {\
        margin: -1px 0 0 -1px;\
        border: 1px solid rgba(243, 255, 181, 0.1);\
      }\
      \
      .ace-parsec .ace_marker-layer .ace_active-line {\
        background: rgba(255, 255, 255, 0.051);\
      }\
      \
      .ace-parsec .ace_gutter-active-line {\
        background-color: rgba(255, 255, 255, 0.051);\
      }\
      \
      .ace-parsec .ace_marker-layer .ace_selected-word {\
        border: 1px solid rgba(197, 223, 233, 0.5);\
      }\
      \
      .ace-parsec .ace_fold {\
        background-color: #ff0f9f;\
        border-color: #58088c;\
      }\
      \
      .ace-parsec .ace_keyword {\
        color: #ff0f9f;\
      }\
      \
      .ace-parsec .ace_constant {\
        color: #c70ea4;\
      }\
      \
      .ace-parsec .ace_support {\
        color: #78b000;\
      }\
      \
      .ace-parsec .ace_support.ace_function {\
        color: #ff92ae;\
      }\
      \
      .ace-parsec .ace_support.ace_constant {\
        color: #eb939a;\
      }\
      \
      .ace-parsec .ace_storage {\
        color: #f95b80;\
      }\
      \
      .ace-parsec .ace_invalid {\
        color: #f8f8f8;\
        background-color: rgba(216, 41, 13, 0.75);\
      }\
      \
      .ace-parsec .ace_string {\
        color: #0090e3;\
      }\
      \
      .ace-parsec .ace_string.ace_regexp {\
        color: #55820d;\
      }\
      \
      .ace-parsec .ace_comment {\
        font-style: italic;\
        color: #78b000;\
      }\
      \
      .ace-parsec .ace_variable {\
        color: #1300c2;\
      }\
      \
      .ace-parsec .ace_meta.ace_tag {\
        color: #51cccc;\
      }';
  var dom = require('../lib/dom');
  dom.importCssString(exports.cssText, exports.cssClass, false);
});
(function () {
  ace.require(['ace/theme/parsec'], function (m) {
    if (typeof module == 'object' && typeof exports == 'object' && module) {
      module.exports = m;
    }
  });
})();
