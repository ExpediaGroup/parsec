(ns parsec-web.theme
  (:require ["@chakra-ui/react" :refer [extendTheme]]
            ["@chakra-ui/theme-tools" :refer [createBreakpoints mode]]))

(def header-text-size "3rem")
(def header-subtext-size "1.25rem")

(def parsec-theme
  (let [breakpoints (createBreakpoints #js {:sm "30em"
                                            :md "48em"
                                            :lg "62em"
                                            :xl "75em"
                                            :xxl "100em"})]
    (extendTheme  (clj->js {:initialColorMode "dark"
                            :useSystemColorMode false
                            :breakpoints breakpoints
                            :colors {:parsec {:blue "#3fa9d7"}
                                     :synthwave {:50 "#0d0221"
                                                 :100 "#241734"
                                                 :200 "#261447"
                                                 :300 "#2e2157"
                                                 :400 "#541388"
                                                 :500 "#ff3864"
                                                 :600 "#2de2e6"
                                                 :700 "#ff6c11"
                                                 :800 "#fd3777"
                                                 :900 "#f706cf"
                                                 :A00 "#fd1d53"
                                                 :A10 "#f9c80e"
                                                 :A20 "#ff4365"
                                                 :A30 "#f6019d"
                                                 :A40 "#650d89"

                                                 :A60 "#791e94"}}
                            :fonts {:heading "'Megrim', sans-serif"
                                    :body "'Lato', sans-serif"}
                            :fontSizes {:xs "0.75rem"
                                        :sm "0.875rem"
                                        :md "1rem"
                                        :lg "1.125rem"
                                        :xl "1.25rem"
                                        :2xl "1.5rem"
                                        :3xl "1.7rem"
                                        :4xl "1.95rem"
                                        :5xl "2.25rem"
                                        :6xl "2.6rem"}
                            :styles {:global {"html, body" {:fontSize "md"}}}
                            :textStyles {:heading {:font-family "'Megrim', sans-serif"}}}))))