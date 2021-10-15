
(ns diman.buckingham.homogeneous-equation-test
  (:require [clojure.test :refer :all]
            [diman.buckingham
             [homogeneous-equation :refer [get-augmented-matrix solve get-solution-matrix]]]
            ))
;; ============================================================================
;;         Standard case: rank >= number of rows of dimensional matrix.
;; ============================================================================
(def dimmat_stnd [[2 -1 3 0 0 -2 1]
             [1 0 -1 0 2 1 2]
             [0 1 0 3 1 -1 2]])
(def augmat_stnd [[0 -2 1 -2  1 -3 0]
             [2  1 2 -1  0  1 0]
             [1 -1 2  0 -1  0 -3]])
(deftest test-get-augmented-matrix
  (testing "Standard case: rank >= number of rows of dimensional matrix."
    ;(println (get-augmented-matrix dimmat_stnd))
    (is (= augmat_stnd (get-augmented-matrix dimmat_stnd)))
    ))
;lein test :only diman.buckingham.homogeneous-equation-test/test-get-augmented-matrix
;; ============================================================================

;; ============================================================================
(def augmat_solved_stnd [[1 0 0 -11 9 -9 15]
                         [0 1 0  5 -4 5 -6]
                         [0 0 1  8 -7 7 -12]])
(deftest test-solve
  (testing "Standard case: rank >= number of rows of dimensional matrix."
    (is (= augmat_solved_stnd (solve augmat_stnd)))))
;lein test :only diman.buckingham.homogeneous-equation-test/test-solve
;; ============================================================================

;; ============================================================================
(def solved_matrix_stnd [[1 0 0 0 -11 5 8]
                         [0 1 0 0 9 -4 -7]
                         [0 0 1 0 -9 5 7]
                         [0 0 0 1 15 -6 -12]])
(deftest test-get-solved-matrix
  (testing "Standard case: rank >= number of rows of dimensional matrix."
    ;(println (get-solved-matrix augsolved))
    (is (= solved_matrix_stnd (get-solution-matrix augmat_solved_stnd)))
    ))
;lein test :only diman.buckingham.homogeneous-equation-test/test-get-solved-matrix
;; ============================================================================

;; ============================================================================
;;     Singular matrix, i.e, rank < number of rows of dimensional matrix.
;; ============================================================================
(def dimmat_sing [[2  1 3 4]
                  [-1 6 -3 0]
                  [1 20 -3 8]])
(def augmat_sing [[3  4 -2 -1]
                  [-3 0 1 -6]
                  [-3 8 -1 -20]])
(deftest test-get-augmented-matrix-singular
  (testing "Case: Singular matrix, i.e, rank < number of rows of dimensional matrix."
    ;(println (get-augmented-matrix dimmat2))
    (is (= augmat_sing (get-augmented-matrix dimmat_sing)))
    ))
;lein test :only diman.buckingham.homogeneous-equation-test/test-get-augmented-matrix-singular
;; ============================================================================

;; ============================================================================
(def augmat_solved_sing [[1 0 -1/3 2]
                         [0 1 -1/4 -7/4]
                         [0 0  0   0]])
(deftest test-solve-singular
  (testing "Standard case: rank >= number of rows of dimensional matrix."
    (is (= augmat_solved_sing (solve augmat_sing)))))
;lein test :only diman.buckingham.homogeneous-equation-test/test-solve
;; ============================================================================

;; ============================================================================
(def solved_matrix_sing [[1 0 -1/3 -1/4]
                         [0 1   2  -7/4]])
(deftest test-get-solved-matrix-singular
  (testing "Case: Singular matrix, i.e, rank < number of rows of dimensional matrix."
    ;(println (get-soln-matrix augsolved))
    (is (= solved_matrix_sing (get-solution-matrix augmat_solved_sing)))
    ))
;lein test :only diman.buckingham.homogeneous-equation-test/test-get-solved-matrix-singular
;; ============================================================================

;lein test diman.buckingham.homogeneous-equation-test
