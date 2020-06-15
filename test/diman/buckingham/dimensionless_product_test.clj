(ns diman.buckingham.dimensionless-product-test
  (:require [clojure.test :refer :all]
            [diman.buckingham
             [dimensionless-product :refer [get-dimensionless-products get-pi-expression]]]
            ))
;; ============================================================================
(def solved_matrix1 [[1 0 0 0 -11 5 8]
                    [0 1 0 0 9 -4 -7]
                    [0 0 1 0 -9 5 7]
                    [0 0 0 1 15 -6 -12]])
(def varpars1 [{:symbol "p", :dimension "some dim 1"}
              {:symbol "q", :dimension "some dim 2"}
              {:symbol "r", :dimension "some dim 3"}
              {:symbol "s", :dimension "some dim 4"}
              {:symbol "t", :dimension "some dim 5"}
              {:symbol "u", :dimension "some dim 6"}
              {:symbol "v", :dimension "some dim 7"}])

(deftest test-get-dimensionless-products1
  (testing "comment"
    (let [all_pi (get-dimensionless-products solved_matrix1 varpars1)]
      (is (= "p^(1)*t^(-11)*u^(5)*v^(8)" (get-pi-expression all_pi "pi0")))
      (is (= "q^(1)*t^(9)*u^(-4)*v^(-7)" (get-pi-expression all_pi "pi1")))
      (is (= "r^(1)*t^(-9)*u^(5)*v^(7)" (get-pi-expression all_pi "pi2")))
      (is (= "s^(1)*t^(15)*u^(-6)*v^(-12)" (get-pi-expression all_pi "pi3")))
      )))
;lein test :only diman.buckingham.dimensionless-product-test/test-get-dimensionless-products1
;; ============================================================================

;; ============================================================================
(def solved_matrix2 [[1 0 0 0 -13/21 -92/63 1]
                     [0 1 0 0 -6/7 -12/7 3/2]
                     [0 0 1 0 5/21 37/63 -1/2]
                     [0 0 0 1 -1/7 -13/21 0]])
(deftest test-get-dimensionless-products2
  (testing "comment"
    (let [all_pi (get-dimensionless-products solved_matrix2 varpars1)]
      (is (= "p^(1)*t^(-13/21)*u^(-92/63)*v^(1)" (get-pi-expression all_pi "pi0")))
      (is (= "q^(1)*t^(-6/7)*u^(-12/7)*v^(3/2)" (get-pi-expression all_pi "pi1")))
      (is (= "r^(1)*t^(5/21)*u^(37/63)*v^(-1/2)" (get-pi-expression all_pi "pi2")))
      (is (= "s^(1)*t^(-1/7)*u^(-13/21)" (get-pi-expression all_pi "pi3")))
      )))
;lein test :only diman.buckingham.dimensionless-product-test/test-get-dimensionless-products2
;; ============================================================================

;lein test diman.buckingham.dimensionless-product-test
