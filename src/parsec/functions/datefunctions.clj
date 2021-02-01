;; Copyright 2020 Expedia, Inc.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     https://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns parsec.functions.datefunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [clojure.math.numeric-tower :as math]
            [clj-time.core :as time]
            [clj-time.coerce :as timec])
  (:import (java.lang Math)
           (org.joda.time.base AbstractDateTime)
           (org.joda.time DateTime ReadablePeriod)
           ))

(defn isbetween-transform
  "Implementation for isbetween(test, start, end)"
  [args]
  (if (not= 3 (count args))
    (throw (Exception. "isBetween() requires 3 arguments: test, start, end"))
    ;(apply cond args)
    (let [[test start end] args]
      (fn [row context]
        (let [test' (eval-expression test row context)
              start' (eval-expression start row context)
              end' (eval-expression end row context)]
          (time/within? start' end' test'))))))

(defn isbetween2-transform
  "Implementation for isbetween2(test, start, end). Start is inclusive and end is exclusive."
  [args]
  (if (not= 3 (count args))
    (throw (Exception. "isBetween2() requires 3 arguments: test, start, end"))
    ;(apply cond args)
    (let [[test start end] args]
      (fn [row context]
        (let [test' (eval-expression test row context)
              start' (eval-expression start row context)
              end' (eval-expression end row context)]
          (and (time/within? start' end' test')
               (time/before? test' end')))))))

(defn period-transform
  "Implementation for period(value, name)"
  [args]
  (if (not= 2 (count args))
    (throw (Exception. "period() requires 2 arguments: value, name."))
    (let [[value name] args]
      (fn [row context]
        (let [value' (eval-expression value row context)
              name' (eval-expression name row context)]
          (if (or (nil? name') (nil? value'))
            (throw (Exception. "period() cannot take null arguments."))
            (case name'
              "milliseconds" (time/millis value')
              "millisecond" (time/millis value')
              "millis" (time/millis value')
              "milli" (time/millis value')

              "seconds" (time/seconds value')
              "second" (time/seconds value')
              "secs" (time/seconds value')
              "sec" (time/seconds value')

              "minutes" (time/minutes value')
              "minute" (time/minutes value')
              "mins" (time/minutes value')
              "min" (time/minutes value')

              "hours" (time/hours value')
              "hour" (time/hours value')
              "hrs" (time/hours value')
              "hr" (time/hours value')

              "days" (time/days value')
              "day" (time/days value')

              "weeks" (time/weeks value')
              "week" (time/weeks value')

              "months" (time/months value')
              "month" (time/months value')

              "years" (time/years value')
              "year" (time/years value')
              (throw (Exception. (str "Unknown period name: " name'))))))))))

(defn bucket-transform
  "Implementation for bucket(value, bucketer)"
  [args]
  (if (not= 2 (count args))
    (throw (Exception. "bucket() requires 2 arguments: value, bucketer."))
    (let [[value bucketer] args]
      (fn [row context]
        (let [value' (eval-expression value row context)
              bucketer' (eval-expression bucketer row context)]
          (if (or (nil? bucketer') (nil? value'))
            (throw (Exception. "bucket() cannot take null arguments."))
            (cond
              (number? bucketer')
              (* bucketer' (Math/floor (/ value' bucketer')))

              (instance? ReadablePeriod bucketer')
              (let [^long period-in-millis (time/in-millis bucketer')
                    ^long value-in-millis (timec/to-long value')
                    num-of-periods (Math/floorDiv value-in-millis period-in-millis)
                    bucket-in-millis (* period-in-millis num-of-periods)]
                (timec/from-long bucket-in-millis))

              :else
              (throw (Exception. (str "Unsupported bucket type: " (type bucketer')))))))))))

;; Relative Date Functions
(defmethod function-transform
  :now [& _] (fn [_ _] (time/to-time-zone (time/now) (time/default-time-zone))))
(defmethod function-transform
  :nowutc [& _] (fn [_ _] (time/now)))
(defmethod function-transform
  :today [& _] (fn [_ _] (timec/to-date-time (time/today-at-midnight (time/default-time-zone)))))
(defmethod function-transform
  :todayutc [& _] (fn [_ _] (timec/to-date-time (time/today-at-midnight time/utc))))
(defmethod function-transform
  :yesterday [& _] (fn [_ _] (timec/to-date-time (time/minus (time/today-at-midnight (time/default-time-zone)) (time/days 1)))))
(defmethod function-transform
  :yesterdayutc [& _] (fn [_ _] (timec/to-date-time (time/minus (time/today-at-midnight time/utc) (time/days 1)))))
(defmethod function-transform
  :tomorrow [& _] (fn [_ _] (timec/to-date-time (time/plus (time/today-at-midnight (time/default-time-zone)) (time/days 1)))))
(defmethod function-transform
  :tomorrowutc [& _] (fn [_ _] (timec/to-date-time (time/plus (time/today-at-midnight time/utc) (time/days 1)))))


;; Date Conversion functions
(defmethod function-transform
  :tolocaltime [_ & args] (fn-with-one-arg-handles-nil #(time/to-time-zone (timec/to-date-time %) (time/default-time-zone)) args))
(defmethod function-transform
  :toutctime [_ & args]
  (fn-with-one-arg-handles-nil #(time/to-time-zone (timec/to-date-time %) time/utc) args))
(defmethod function-transform
  :totimezone [_ & args]
  (fn-with-two-args-handles-nil #(time/to-time-zone (timec/to-date-time %) (time/time-zone-for-id %2)) args))
(defmethod function-transform
  :timezones [& _] (time/available-ids))
(defmethod function-transform
  :toepoch [_ & args] (fn-with-one-arg-handles-nil #(math/floor (timec/to-epoch (timec/to-date-time %))) args))
(defmethod function-transform
  :toepochmillis [_ & args]
  (fn-with-one-arg-handles-nil
    #(if (string? %)
      (throw (IllegalArgumentException. "No implementation of method: :toEpochMillis found for class: java.lang.String"))
      (timec/to-long (timec/to-date-time %))) args))


;; Start of Date functions
(defmethod function-transform
  :startofminute [_ & args]
  (fn-with-one-arg-handles-nil #(-> ^DateTime %
                                    (.withMillisOfSecond 0)
                                    (.withSecondOfMinute 0)) args))
(defmethod function-transform
  :startofhour [_ & args]
  (fn-with-one-arg-handles-nil #(-> ^DateTime %
                                    (.withMillisOfSecond 0)
                                    (.withSecondOfMinute 0)
                                    (.withMinuteOfHour 0)) args))
(defmethod function-transform
  :startofday [_ & args]
  (fn-with-one-arg-handles-nil #(.withTimeAtStartOfDay ^DateTime %) args))

(defmethod function-transform
  :startofweek [_ & args]
  (fn-with-one-arg-handles-nil #(-> ^DateTime %
                                    (.withTimeAtStartOfDay)
                                    (.withDayOfWeek 1)) args))
(defmethod function-transform
  :startofmonth [_ & args]
  (fn-with-one-arg-handles-nil #(-> ^DateTime %
                                    (.withTimeAtStartOfDay)
                                    (.withDayOfMonth 1)) args))
(defmethod function-transform
  :startofyear [_ & args]
  (fn-with-one-arg-handles-nil #(-> ^DateTime %
                                    (.withTimeAtStartOfDay)
                                    (.withDayOfYear 1)) args))

;; Date Part functions
(defmethod function-transform
  :millisecond [_ & args] (fn-with-one-arg-handles-nil #(time/milli %) args))
(defmethod function-transform
  :millisecondofday [_ & args] (fn-with-one-arg-handles-nil #(.getMillisOfDay ^AbstractDateTime %) args))
(defmethod function-transform
  :second [_ & args] (fn-with-one-arg-handles-nil #(time/second %) args))
(defmethod function-transform
  :secondofday [_ & args] (fn-with-one-arg-handles-nil #(.getSecondOfDay ^AbstractDateTime %) args))
(defmethod function-transform
  :minute [_ & args] (fn-with-one-arg-handles-nil #(time/minute %) args))
(defmethod function-transform
  :minuteofday [_ & args] (fn-with-one-arg-handles-nil #(.getMinuteOfDay ^AbstractDateTime %) args))
(defmethod function-transform
  :hour [_ & args] (fn-with-one-arg-handles-nil #(time/hour %) args))
(defmethod function-transform
  :day [_ & args] (fn-with-one-arg-handles-nil #(time/day %) args))
(defmethod function-transform
  :dayofweek [_ & args] (fn-with-one-arg-handles-nil #(time/day-of-week %) args))
(defmethod function-transform
  :dayofyear [_ & args] (fn-with-one-arg-handles-nil #(.getDayOfYear ^AbstractDateTime %) args))
(defmethod function-transform
  :weekyear [_ & args] (fn-with-one-arg-handles-nil #(.getWeekyear ^AbstractDateTime %) args))
(defmethod function-transform
  :weekofweekyear [_ & args] (fn-with-one-arg-handles-nil #(.getWeekOfWeekyear ^AbstractDateTime %) args))
(defmethod function-transform
  :month [_ & args] (fn-with-one-arg-handles-nil #(time/month %) args))
(defmethod function-transform
  :year [_ & args] (fn-with-one-arg-handles-nil #(time/year %) args))


;; Add / Subtract Date functions
(defmethod function-transform
  :adddays [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/days %2)) args))
(defmethod function-transform
  :addhours [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/hours %2)) args))
(defmethod function-transform
  :addmilliseconds [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/millis %2)) args))
(defmethod function-transform
  :addminutes [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/minutes %2)) args))
(defmethod function-transform
  :addmonths [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/months %2)) args))
(defmethod function-transform
  :addseconds [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/seconds %2)) args))
(defmethod function-transform
  :addweeks [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/weeks %2)) args))
(defmethod function-transform
  :addyears [_ & args] (fn-with-two-args-handles-nil #(time/plus % (time/years %2)) args))

(defmethod function-transform
  :minusdays [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/days %2)) args))
(defmethod function-transform
  :minushours [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/hours %2)) args))
(defmethod function-transform
  :minusmilliseconds [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/millis %2)) args))
(defmethod function-transform
  :minusminutes [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/minutes %2)) args))
(defmethod function-transform
  :minusmonths [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/months %2)) args))
(defmethod function-transform
  :minusseconds [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/seconds %2)) args))
(defmethod function-transform
  :minusweeks [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/weeks %2)) args))
(defmethod function-transform
  :minusyears [_ & args] (fn-with-two-args-handles-nil #(time/minus % (time/years %2)) args))

;; Interval functions
(defmethod function-transform
  :interval [_ & args] (fn-with-two-args-handles-nil #(time/interval % %2) args))

(defmethod function-transform
  :inmillis [_ & args] (fn-with-one-arg-handles-nil #(time/in-millis %) args))
(defmethod function-transform
  :inseconds [_ & args] (fn-with-one-arg-handles-nil #(time/in-seconds %) args))
(defmethod function-transform
  :inminutes [_ & args] (fn-with-one-arg-handles-nil #(time/in-minutes %) args))
(defmethod function-transform
  :inhours [_ & args] (fn-with-one-arg-handles-nil #(time/in-hours %) args))
(defmethod function-transform
  :indays [_ & args] (fn-with-one-arg-handles-nil #(time/in-days %) args))
(defmethod function-transform
  :inweeks [_ & args] (fn-with-one-arg-handles-nil #(time/in-weeks %) args))
(defmethod function-transform
  :inmonths [_ & args] (fn-with-one-arg-handles-nil #(time/in-months %) args))
(defmethod function-transform
  :inyears [_ & args] (fn-with-one-arg-handles-nil #(time/in-years %) args))

(defmethod function-transform
  :period [_ & args] (period-transform args))
(defmethod function-transform
  :bucket [_ & args] (bucket-transform args))

(defmethod function-transform
  :earliest [_ & args] (fn-with-two-args-handles-nil #(try (time/earliest % %2) (catch Exception _ nil)) args))
(defmethod function-transform
  :latest [_ & args] (fn-with-two-args-handles-nil #(try (time/latest % %2) (catch Exception _ nil)) args))

(defmethod function-transform
  :isbetween [_ & args] (isbetween-transform args))
(defmethod function-transform
  :isbetween2 [_ & args] (isbetween2-transform args))
