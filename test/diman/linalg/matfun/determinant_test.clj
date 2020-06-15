(ns diman.linalg.matfun.determinant-test
  (:require [clojure.test :refer :all]
            [diman.linalg.matfun [determinant :refer [det]]]
            ))
;; ============================================================================
(def mat1 [[-2 2 -3]
           [-1 1 3]
           [2 0 -1]])
(def mat1_reduced [[-2 2 -3]
                   [0 2  -4]
                   [0 0  9/2]])
(def mat1_det 18)
(deftest test-get-row-echelon
  (testing "TEST diman.linalg.matfun.determinant/get-row-echelon"
    (let [fun #'diman.linalg.matfun.determinant/get-row-echelon
          [reduced_mat signs] (fun mat1)]
      (is (= mat1_reduced reduced_mat))
      )))
;lein test :only diman.linalg.matfun.determinant-test/test-get-row-echelon
;; ============================================================================

;; ============================================================================
(def mat2 [[3 1 -4] [2 5 6] [1 4 8]]) (def mat2_det -26)
(def mat3 [[3 1 0] [-2 -4 3] [5 4 -2]]) (def mat3_det -1)
(def mat4 [[3 5 -2 6] [1 2 -1 1] [2 4 1 5] [3 7 5 3]]) (def mat4_det -18)
(def mat5 [[3 5 -2 6] [1 2 -1 1] [2 4 1 5]]) (def mat5_det nil)

(deftest test-det
  (testing "TEST diman.linalg.matfun.determinant/det"
    (is (= mat1_det (det mat1)))
    (is (= mat2_det (det mat2)))
    (is (= mat3_det (det mat3)))
    (is (= mat4_det (det mat4)))
    (is (= mat5_det (det mat5)))
    ))
;lein test :only diman.linalg.matfun.determinant-test/test-det
;; ============================================================================

;lein test diman.linalg.matfun.determinant-test
