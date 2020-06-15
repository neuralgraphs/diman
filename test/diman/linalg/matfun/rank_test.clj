(ns diman.linalg.matfun.rank-test
  (:require [clojure.test :refer :all]
            [diman.linalg.matfun [rank :refer [rank]]]
            ))
;; ============================================================================
(def A1 [[ 0 0  1  1  1  0]
         [ 1 1  1 -3 -1  1]
         [-1 0 -2  0 -1 -2]])
(def A2 [[2 -1 3 0 0 -2 1]
         [1  0 -1 0 2 1 2]
         [0 1 0 3 1 -1 2]])
(def A3 [[ 2 1 3 4]
         [-1 6 -3 0]
         [1 20 -3 8]])
(def A4 [[3 2 4]
         [-1 1 2]
         [9 5 10]])
(def A5 [[10 0 0 0]
         [0 25 0 0]
         [0 0 34 0]
         [0 0 0 0]])
(deftest test-rank
  (testing "TEST diman.linalg.matfun.rank/rank"
    (is (= 3 (rank A1)))
    (is (= 3 (rank A2)))
    (is (= 2 (rank A3)))
    (is (= 2 (rank A4)))
    (is (= 3 (rank A5)))
    ))
;lein test :only diman.linalg.matfun.rank-test/test-rank
;; ============================================================================

;lein test diman.linalg.matfun.rank-test
