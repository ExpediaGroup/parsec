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

(ns parsec.helpers.strings
  (:import (cern.jet.stat.tdouble DoubleDescriptive)
           (cern.colt.list.tdouble DoubleArrayList)))

(defn subs2
  "Like subs, but without end bound checking"
  ([s start] (subs s start))
  ([s start end]
    (if (> end (count s))
      (subs s start)
      (subs s start end))))

(defn substr
  "Like subs, but with a length instead of end index"
  ([s start] (subs s start))
  ([s start length]
   (subs2 s start (+ start length))))
