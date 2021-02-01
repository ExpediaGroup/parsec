(ns parsec.test-datasets)

(def test-dataset1
  '({:col1 1, :col2 2, :col3 3, :col4 4}
     {:col1 2, :col2 2, :col3 3, :col4 3}
     {:col1 3, :col2 2, :col3 5, :col4 2}
     {:col1 4, :col2 5, :col3 5, :col4 1}
     {:col1 5, :col2 5, :col3 3, :col4 0}))

(def test-dataset2
  '({:col1 1, :col2 2, :col4 4}
     {:col1 2, :col2 nil, :col4 3}
     {:col1 nil, :col2 2, :col3 5}
     {:col1 nil, :col2 nil, :col3 5, :col4 1}
     {:col1 5, :col2 5, :col3 3}))

(def test-dataset3
  '({:name "alpha" :id 1 :type 10}
     {:name "beta" :id 2 :type 10}
     {:name "gamma" :id 3 :type 12}
     {:name "delta" :id 4 :type 15}
     {:name "epsilon" :id 5 :type 20}))

(def test-dataset4
  '({:id 10 :color "red" }
     {:id 11 :color "orange" }
     {:id 12 :color "yellow" }
     {:id 13 :color "green" }
     {:id 14 :color "blue" }
     {:id 15 :color "violet" }
     {:id 20 :color "black" }))
