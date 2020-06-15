(ns diman.linalg.ops.multiplication-test
  (:require [clojure.test :refer :all]
            [diman.linalg.ops [multiplication :refer [times]]]
            ))
(def x [1 -3 2 4])
(def X [[1 6 8]
        [4 10 -2]
        [0 2 -20]])
(def y [2 3 4])
(def Y [[1 2 3]
        [4 5 6]
        [7 8 9]])
(def xtimes2 [2 -6 4 8])
(def Xtimesminus3 [[-3 -18 -24]
                   [-12 -30 6]
                   [0 -6 60]])
(def xtimesx [1 9 4 16])
(def Xtimesy [[2 18 32]
              [8 30 -8]
              [0 6 -80]])
(def XtimesY [[1 12 24]
              [16 50 -12]
              [0 16 -180]])
(deftest test-times
  (testing "comment"
    (is (= xtimes2 (times 2 x)))
    (is (= Xtimesminus3 (times -3 X)))
    (is (= xtimesx (times x x)))
    (is (= Xtimesy (times y X)))
    (is (= XtimesY (times Y X)))
    ))
;lein test :only diman.linalg.ops.multiplication-test/test-times
;lein test diman.linalg.ops.multiplication-test
