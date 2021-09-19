(ns parsec.statements.join-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [join-statement]]))

(deftest join-statement-parser-test
  (testing-parser
    "with natural join" :join-statement
    "natural join x" [:join-statement :NATURAL [:join-alias] [:join-reference-source :x] [:join-terms]])
  (testing-parser
    "with implied natural join" :join-statement
    "join x" [:join-statement :NATURAL [:join-alias] [:join-reference-source :x] [:join-terms]])
  (testing-parser
    "with implied natural join and inline dataset" :join-statement
    "join (input mock)" [:join-statement :NATURAL [:join-alias]
                         [:join-inline-source
                          [:query [:input-statement :mock '(:function :tolowercasemap)]]] [:join-terms]])
  (testing-parser
    "with implied natural join and inline named dataset" :join-statement
    "join (input mock) x" [:join-statement :NATURAL [:join-alias]
                           [:join-inline-source
                            [:query [:input-statement :mock '(:function :tolowercasemap)]]
                            :x] [:join-terms]])
  (testing-parser
    "with natural join and inline dataset" :join-statement
    "natural join (input mock) x" [:join-statement :NATURAL [:join-alias]
                                   [:join-inline-source
                                    [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                    :x] [:join-terms]])
  (testing-parser
    "with inner join and inline dataset" :join-statement
    "inner join y, (input mock) x on x.id == y.id" [:join-statement
                                                   :INNER [:join-alias :y]
                                                   [:join-inline-source
                                                    [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                    :x]
                                                   [:join-terms
                                                    [:equi-join-term
                                                     [:join-identifier :x :id]
                                                     [:join-identifier :y :id]]]])
  (testing-parser
    "with inner join and inline dataset and no LEFT join-alias" :join-statement
    "inner join (input mock) x on x.id == y.id" [:join-statement
                                                    :INNER [:join-alias]
                                                    [:join-inline-source
                                                     [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                     :x]
                                                    [:join-terms
                                                     [:equi-join-term
                                                      [:join-identifier :x :id]
                                                      [:join-identifier :y :id]]]])

  (testing-parser
    "with inner join and inline dataset and no LEFT or RIGHT join-alias" :join-statement
    "inner join (input mock) on x.id == y.id" [:join-statement
                                                 :INNER [:join-alias]
                                                 [:join-inline-source
                                                  [:query [:input-statement :mock '(:function :tolowercasemap)]]]
                                                 [:join-terms
                                                  [:equi-join-term
                                                   [:join-identifier :x :id]
                                                   [:join-identifier :y :id]]]])

  (testing-parser
    "with inner join and reference dataset and multiple join terms" :join-statement
    "inner join y, x on x.id == y.id, x.name == y.name" [:join-statement
                                                    :INNER [:join-alias :y] [:join-reference-source :x]
                                                    [:join-terms
                                                     [:equi-join-term
                                                      [:join-identifier :x :id]
                                                      [:join-identifier :y :id]]
                                                     [:equi-join-term
                                                      [:join-identifier :x :name]
                                                      [:join-identifier :y :name]]]])

  (testing-parser
    "with inner join and reference dataset and expression join term" :join-statement
    "inner join y, x on lowercase(x.name) == y.name" [:join-statement
                                                         :INNER [:join-alias :y] [:join-reference-source :x]
                                                         [:join-terms
                                                          [:theta-join-term
                                                           [:expression
                                                            [:equals-expression
                                                             [:function :lowercase [:expression [:join-identifier :x :name]]]
                                                             [:join-identifier :y :name]]]]]])
  (testing-parser
    "with left (outer) join and inline dataset" :join-statement
    "left join y, (input mock) x on x.id == y.id" [:join-statement
                                                  :LEFT [:join-alias :y]
                                                  [:join-inline-source
                                                   [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                   :x]
                                                  [:join-terms
                                                   [:equi-join-term
                                                    [:join-identifier :x :id]
                                                    [:join-identifier :y :id]]]])
  (testing-parser
    "with left outer join and inline dataset" :join-statement
    "left outer join y, (input mock) x on x.id == y.id" [:join-statement
                                                        :LEFT [:join-alias :y]
                                                        [:join-inline-source
                                                         [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                         :x]
                                                        [:join-terms
                                                         [:equi-join-term
                                                          [:join-identifier :x :id]
                                                          [:join-identifier :y :id]]]])
  (testing-parser
    "with right (outer) join and inline dataset" :join-statement
    "right join y, (input mock) x on x.id == y.id" [:join-statement
                                                   :RIGHT [:join-alias :y]
                                                   [:join-inline-source
                                                    [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                    :x]
                                                   [:join-terms
                                                    [:equi-join-term
                                                     [:join-identifier :x :id]
                                                     [:join-identifier :y :id]]]])
  (testing-parser
    "with right outer join and inline dataset" :join-statement
    "right outer join y, (input mock) x on x.id == y.id" [:join-statement
                                                         :RIGHT [:join-alias :y]
                                                         [:join-inline-source
                                                          [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                          :x]
                                                         [:join-terms
                                                          [:equi-join-term
                                                           [:join-identifier :x :id]
                                                           [:join-identifier :y :id]]]])
  (testing-parser
    "with full (outer) join and inline dataset" :join-statement
    "full join y, (input mock) x on x.id == y.id" [:join-statement
                                                  :FULL [:join-alias :y]
                                                  [:join-inline-source
                                                   [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                   :x]
                                                  [:join-terms
                                                   [:equi-join-term
                                                    [:join-identifier :x :id]
                                                    [:join-identifier :y :id]]]])
  (testing-parser
    "with full outer join and inline dataset" :join-statement
    "full outer join y, (input mock) x on x.id == y.id" [:join-statement
                                                        :FULL [:join-alias :y]
                                                        [:join-inline-source
                                                         [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                         :x]
                                                        [:join-terms
                                                         [:equi-join-term
                                                          [:join-identifier :x :id]
                                                          [:join-identifier :y :id]]]])
  (testing-parser
    "with (full) outer join and inline dataset" :join-statement
    "outer join y, (input mock) x on x.id == y.id" [:join-statement
                                                   :FULL [:join-alias :y]
                                                   [:join-inline-source
                                                    [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                                    :x]
                                                   [:join-terms
                                                    [:equi-join-term
                                                     [:join-identifier :x :id]
                                                     [:join-identifier :y :id]]]])
  (testing-parser
    "with cross join and inline dataset" :join-statement
    "cross join y, (input mock) x on x.id == y.id" [:join-statement
                                              :CROSS [:join-alias :y]
                                              [:join-inline-source
                                               [:query [:input-statement :mock '(:function :tolowercasemap)]]
                                               :x]
                                              [:join-terms
                                               [:equi-join-term
                                                [:join-identifier :x :id]
                                                [:join-identifier :y :id]]]]))

(deftest join-statement-test
  (testing-query-execution
    "basic inner join"
    "input datastore name='test-dataset3' | inner join x, (input datastore name='test-dataset4') types on x.type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } :test-dataset4 { :data test-dataset4 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "gamma" :id 3 :type 12 :color "yellow"}
       {:name "delta" :id 4 :type 15 :color "violet"}
       {:name "epsilon" :id 5 :type 20 :color "black"}))

  (testing-query-execution
    "basic inner join with multiple join-terms"
    "input datastore name='test-dataset3' | inner join x, (input datastore name='test-dataset4') types on x.type == types.id, x.id + 9 == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } :test-dataset4 { :data test-dataset4 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:id 3, :color "yellow", :name "gamma", :type 12}))

  (testing-query-execution
    "basic inner join without LEFT aliased join-term"
    "input datastore name='test-dataset3' | inner join x, (input datastore name='test-dataset4') types on type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } :test-dataset4 { :data test-dataset4 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "gamma" :id 3 :type 12 :color "yellow"}
       {:name "delta" :id 4 :type 15 :color "violet"}
       {:name "epsilon" :id 5 :type 20 :color "black"}))

  (testing-query-execution
    "basic inner join without LEFT alias"
    "input datastore name='test-dataset3' | inner join (input datastore name='test-dataset4') types on type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } :test-dataset4 { :data test-dataset4 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "gamma" :id 3 :type 12 :color "yellow"}
       {:name "delta" :id 4 :type 15 :color "violet"}
       {:name "epsilon" :id 5 :type 20 :color "black"}))

  (testing-query-execution
    "basic inner join with unmatched left rows"
    "input datastore name='test-dataset3' | inner join (project [{id: 10, color: \"red\"}] ) types on type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}))

  (testing-query-execution
    "inner join with filtering join-term"
    "project [{id: 10, color: \"red\", enabled: false}, {id: 10, color: \"silver\", enabled: true}] | inner join types, (input datastore name='test-dataset3') td3 on td3.type == types.id, types.enabled == true| sort name asc"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 10 :type 10 :color "silver" :enabled true}
       {:name "beta" :id 10 :type 10 :color "silver" :enabled true}))

  (testing-query-execution
    "left outer join with unmatched left rows"
    "input datastore name='test-dataset3' | left outer join (project [{id: 10, color: \"red\"}] ) types on type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "gamma" :id 3 :type 12}
       {:name "delta" :id 4 :type 15}
       {:name "epsilon" :id 5 :type 20}))

  (testing-query-execution
    "right outer join with unmatched right rows"
    "project [{id: 10, color: \"red\"}] | right outer join types, (input datastore name='test-dataset3') on type == types.id"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 10 :type 10 :color "red"}
       {:name "beta" :id 10 :type 10 :color "red"}
       {:name "gamma" :id 3 :type 12}
       {:name "delta" :id 4 :type 15}
       {:name "epsilon" :id 5 :type 20}))

  (testing-query-execution
    "full outer join with unmatched left rows"
    "input datastore name='test-dataset3' | full outer join td3, (project [{id: 10, color: \"red\"}] ) types on td3.type == types.id | sort name asc"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "delta" :id 4 :type 15}
       {:name "epsilon" :id 5 :type 20}
       {:name "gamma" :id 3 :type 12}))

  (testing-query-execution
    "full outer join with unmatched right rows"
    "project [{id: 10, color: \"red\"}] | full outer join types, (input datastore name='test-dataset3') td3 on td3.type == types.id | sort name asc"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 10 :type 10 :color "red"}
       {:name "beta" :id 10 :type 10 :color "red"}
       {:name "delta" :id 4 :type 15}
       {:name "epsilon" :id 5 :type 20}
       {:name "gamma" :id 3 :type 12}))

  (testing-query-execution
    "full outer join with unmatched left and right rows"
    "project [{id: 10, color: \"red\"}, {id: 100, color: \"silver\"}] | full outer join types, (input datastore name='test-dataset3') td3 on td3.type == types.id | sort name asc"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:id 100 :color "silver"}
       {:name "alpha" :id 10 :type 10 :color "red"}
       {:name "beta" :id 10 :type 10 :color "red"}
       {:name "delta" :id 4 :type 15}
       {:name "epsilon" :id 5 :type 20}
       {:name "gamma" :id 3 :type 12}))

  (testing-query-execution
    "basic cross join"
    "input datastore name='test-dataset3' | cross join (project [{color: \"red\", enabled: true},{color: \"green\", enabled: false}] )"
    { :datastore { :test-dataset3 { :data test-dataset3 } } }
    '({:name "alpha" :id 1 :type 10 :color "red" :enabled true}
       {:name "alpha" :id 1 :type 10 :color "green" :enabled false}
       {:name "beta" :id 2 :type 10 :color "red" :enabled true}
       {:name "beta" :id 2 :type 10 :color "green" :enabled false}
       {:name "gamma" :id 3 :type 12 :color "red" :enabled true}
       {:name "gamma" :id 3 :type 12 :color "green" :enabled false}
       {:name "delta" :id 4 :type 15 :color "red" :enabled true}
       {:name "delta" :id 4 :type 15 :color "green" :enabled false}
       {:name "epsilon" :id 5 :type 20 :color "red" :enabled true}
       {:name "epsilon" :id 5 :type 20 :color "green" :enabled false}))

  (testing-query-execution
    "larger cross join"
    "input datastore name='test-dataset3' | cross join (input datastore name='test-dataset4')"
    { :datastore { :test-dataset3 { :data test-dataset3 } :test-dataset4 { :data test-dataset4 } } }
    '({:name "alpha" :id 1 :type 10 :color "red"}
       {:name "alpha" :id 1 :type 10 :color "orange"}
       {:name "alpha" :id 1 :type 10 :color "yellow"}
       {:name "alpha" :id 1 :type 10 :color "green"}
       {:name "alpha" :id 1 :type 10 :color "blue"}
       {:name "alpha" :id 1 :type 10 :color "violet"}
       {:name "alpha" :id 1 :type 10 :color "black"}
       {:name "beta" :id 2 :type 10 :color "red"}
       {:name "beta" :id 2 :type 10 :color "orange"}
       {:name "beta" :id 2 :type 10 :color "yellow"}
       {:name "beta" :id 2 :type 10 :color "green"}
       {:name "beta" :id 2 :type 10 :color "blue"}
       {:name "beta" :id 2 :type 10 :color "violet"}
       {:name "beta" :id 2 :type 10 :color "black"}
       {:name "gamma" :id 3 :type 12 :color "red"}
       {:name "gamma" :id 3 :type 12 :color "orange"}
       {:name "gamma" :id 3 :type 12 :color "yellow"}
       {:name "gamma" :id 3 :type 12 :color "green"}
       {:name "gamma" :id 3 :type 12 :color "blue"}
       {:name "gamma" :id 3 :type 12 :color "violet"}
       {:name "gamma" :id 3 :type 12 :color "black"}
       {:name "delta" :id 4 :type 15 :color "red"}
       {:name "delta" :id 4 :type 15 :color "orange"}
       {:name "delta" :id 4 :type 15 :color "yellow"}
       {:name "delta" :id 4 :type 15 :color "green"}
       {:name "delta" :id 4 :type 15 :color "blue"}
       {:name "delta" :id 4 :type 15 :color "violet"}
       {:name "delta" :id 4 :type 15 :color "black"}
       {:name "epsilon" :id 5 :type 20 :color "red"}
       {:name "epsilon" :id 5 :type 20 :color "orange"}
       {:name "epsilon" :id 5 :type 20 :color "yellow"}
       {:name "epsilon" :id 5 :type 20 :color "green"}
       {:name "epsilon" :id 5 :type 20 :color "blue"}
       {:name "epsilon" :id 5 :type 20 :color "violet"}
       {:name "epsilon" :id 5 :type 20 :color "black"})))
