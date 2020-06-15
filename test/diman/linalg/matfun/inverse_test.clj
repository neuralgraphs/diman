(ns diman.linalg.matfun.inverse-test
  (:require [clojure.test :refer :all]
            [diman.linalg.matfun [inverse :refer [inv]]]
            ))

;; ============================================================================
(def mat1 [[1 2 3] [2 5 3] [1 0 8]])
(def mat1_inv [[-40 16 9] [13 -5 -3] [5 -2 -1]])
(def mat2 [[1 6 4] [2 4 -1] [-1 2 5]]) (def mat2_inv nil)
(def mat3 [[3 4 -1] [1 0 3] [2 5 -4]])
(def mat3_inv [[3/2 -11/10 -6/5] [-1 1 1] [-1/2 7/10 2/5]])
(def mat4 [[3 1 5] [2 4 1] [-4 2 -9]]) (def mat4_inv nil)
(def mat5 [[1 0 1] [0 1 1] [1 1 0]])
(def mat5_inv [[1/2 -1/2 1/2] [-1/2 1/2 1/2] [1/2 1/2 -1/2]])
(def mat6 [[1/5 1/5 1/5] [1/5 1/5 -4/5] [-2/5 1/10 1/10]])
(def mat6_inv [[1 0 -2] [3 1 2] [1 -1 0]])

(deftest test-inv
  (testing "TEST diman.linalg.matfun.inverse/inv"
    (is (= mat1_inv (inv mat1)))
    (is (= mat2_inv (inv mat2)))
    (is (= mat3_inv (inv mat3)))
    (is (= mat4_inv (inv mat4)))
    (is (= mat5_inv (inv mat5)))
    (is (= mat6_inv (inv mat6)))
    ))
;lein test :only diman.linalg.matfun.inverse-test/test-inv
;; ============================================================================

;lein test diman.linalg.matfun.inverse-test