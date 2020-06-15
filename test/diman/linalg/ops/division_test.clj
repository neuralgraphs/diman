(ns diman.linalg.ops.division-test
  (:require [clojure.test :refer :all]
            [diman.linalg.ops [division :refer [rdivide]]]
            ))
(def x [1 -3 2 4])
(def X [[1 6 8]
        [4 10 -2]
        [0 2 -20]])
(def y [2 3 4])
(def Y [[1 2 3]
        [4 5 6]
        [7 8 9]])
(def xdividedby2 [1/2 -3/2 1 2])
(def Xdividedbyminus3 [[-1/3 -2 -8/3]
                       [-4/3 -10/3 2/3]
                       [0 -2/3 20/3]])
(def xdividedbyx [1 1 1 1])
(def Xdividedbyy [[1/2 2 2]
                  [2 10/3 -1/2]
                  [0 2/3 -5]])
(def XdividedbyY [[1 3 8/3]
                  [1 2 -1/3]
                  [0 1/4 -20/9]])
(deftest test-rdivide
  (testing "comment"
    (is (= xdividedby2 (rdivide x 2)))
    (is (= Xdividedbyminus3 (rdivide X -3)))
    (is (= xdividedbyx (rdivide x x)))
    (is (= Xdividedbyy (rdivide X y)))
    (is (= XdividedbyY (rdivide X Y)))
    ))
;lein test :only diman.linalg.ops.division-test/test-rdivide
;lein test diman.linalg.ops.division-test