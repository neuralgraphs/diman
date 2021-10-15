(ns diman.analyze-test
  (:require [clojure.test :refer :all]
            [diman [analyze :refer [dimnames consistent?]]]
            ))

(def varpars [{:symbol "x", :quantity "length"}
              {:symbol "v", :quantity "velocity"}
              {:symbol "t", :quantity "time"}
              {:symbol "a", :quantity "acceleration"}])

;; =================== Case1: x = x0 + v2 + t + (1/2)at2 ==================
(def eqn1 {:lhs "x^(1)",
          :rhs {:term1 "x^(1)", :term2 "v^(2)", :term3 "t^(1)", :term4 "0.5*a^(1)*t^(2)"}})
(def formula_lhs1 "[L^(1)]")
(def formula_rhs1 "[L^(1)] + [T^(-2)*L^(2)] + [T^(1)] + [T^(0)*L^(1)]")

;; ===================== Case2: x = x0 + vt + (1/2)at2 ====================
(def eqn2 {:lhs "x^(1)",
           :rhs {:term1 "x^(1)", :term2 "v^(2)*t^(1)", :term3 "0.5*a^(1)*t^(2)"}})
(def formula_lhs2 "[L^(1)]")
(def formula_rhs2 "[L^(1)] + [T^(-1)*L^(2)] + [T^(0)*L^(1)]")

;; ==================== TEST diman.analyze/dimnames =======================
(deftest test-dimnames
  (testing "comment"
    (is (= "length^(1)" (dimnames formula_lhs1)))
    (is (= "length^(1) + time^(-2)*length^(2) + time^(1) + length^(1)" (dimnames formula_rhs1)))
    (is (= "length^(1) + time^(-1)*length^(2) + length^(1)" (dimnames formula_rhs2)))
    ))
;lein test :only diman.analyze-test/test-dimnames
;; ============================================================================

;; ================= TEST diman.analyze/consistent? ====================
(def dnames_rhs1 "length^(1) + time^(-2)*length^(2) + time^(1) + length^(1)")
(def dnames_rhs2 "length^(1) + time^(-1)*length^(2) + length^(1)")

(deftest test-replace-plus-by-empty-string
  (testing "comment"
    (is (= ["length^(1)" "" "time^(-2)*length^(2)" "" "time^(1)" "" "length^(1)"]
           (#'diman.analyze/replace-plus-by-empty-string dnames_rhs1)))
    (is (= ["length^(1)" "" "time^(-1)*length^(2)" "" "length^(1)"]
           (#'diman.analyze/replace-plus-by-empty-string dnames_rhs2)))
    ))

(deftest test-clean-dimnames
  (testing "comment"
    (is (= (list "length^(1)" "time^(-2)*length^(2)" "time^(1)" "length^(1)")
           (#'diman.analyze/clean-dimnames dnames_rhs1)))
    (is (= (list "length^(1)" "time^(-1)*length^(2)" "length^(1)")
           (#'diman.analyze/clean-dimnames dnames_rhs2)))
    ))

(deftest test-consistent
  (testing "comment"
    (false? (consistent? varpars eqn1))
    (true? (consistent? varpars eqn2))
    ))
;lein test :only diman.analyze-test/test-replace-plus-by-empty-string
;lein test :only diman.analyze-test/test-clean-dimnames
;lein test :only diman.analyze-test/test-consistent
;; ============================================================================

;lein test diman.analyze-test
