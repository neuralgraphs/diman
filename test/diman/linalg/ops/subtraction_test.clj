(ns diman.linalg.ops.subtraction-test
  (:require [clojure.test :refer :all]
            [diman.linalg.ops [subtraction :refer [minus]]]
            ))
(def x [1 -3 2 4])
(def X [[1 6 8]
        [4 10 -2]
        [0 2 -20]])
(def y [2 3 4])
(def Y [[1 2 3]
        [4 5 6]
        [7 8 9]])
(def xminusminus3 [4 0 5 7])
(def Xminus3 [[-2 3 5]
             [1 7 -5]
             [-3 -1 -23]])
(def xminusx [0 0 0 0])
(def Xminusy [[-1 3 4]
             [2 7 -6]
             [-2 -1 -24]])
(def XminusY [[0 4 5]
             [0 5 -8]
             [-7 -6 -29]])
(deftest test-minus
  (testing "comment"
    (is (= xminusminus3 (minus x -3)))
    (is (= Xminus3 (minus X 3)))
    (is (= xminusx (minus x x)))
    (is (= Xminusy (minus X y)))
    (is (= XminusY (minus X Y)))
    ))
;lein test :only diman.linalg.ops.subtraction-test/minus-plus
;lein test diman.linalg.ops.subtraction-test
