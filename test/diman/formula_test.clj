(ns diman.formula-test
  (:require [clojure.test :refer :all]
            [diman [formula :refer [formula-term formula-eqn-side]]]
            ))
(def varpars [{:symbol "x", :quantity "length"}
              {:symbol "v", :quantity "velocity"}
              {:symbol "t", :quantity "time"}
              {:symbol "a", :quantity "acceleration"}])
(def lhs "x^(1)")
(def rhs {:term1 "x^(1)", :term2 "v^(2)", :term3 "t^(1)", :term4 "0.5*a^(1)*t^(2)"})
(def eqn {:lhs lhs, :rhs rhs})
;; ================= TEST diman.formula/formula-term =================
(deftest test-formula-single-varpar
  (testing "comment"
    (let [fun #'diman.formula/formula-single-varpar]
      (is (= "[L^(1)" (fun varpars "x" "1")))
      (is (= "[L^(1)*T^(-1)]" (fun varpars "v" "1")))
      (is (= "[T^(1)" (fun varpars "t" "1")))
      (is (= "[L^(1)*T^(-2)]" (fun varpars "a" "1")))
      )))
(deftest test-formula-all-varpar-for-term
  (testing "comment"
    (let [fun #'diman.formula/formula-all-varpar-for-term]
      (is (= (list "[L^(1)") (fun varpars lhs)))
      (is (= (list "[L^(1)") (fun varpars (:term1 rhs))))
      (is (= (list "[L^(2)*T^(-2)]") (fun varpars (:term2 rhs))))
      (is (= (list "[T^(1)") (fun varpars (:term3 rhs))))
      (is (= (list "[L^(1)*T^(-2)]" "[T^(2)") (fun varpars (:term4 rhs))))
      )))

(deftest test-formula-term
  (testing "comment"
    (is (= "[L^(1)]" (formula-term varpars lhs)))
    (is (= "[L^(1)]" (formula-term varpars (:term1 rhs))))
    (is (= "[T^(-2)*L^(2)]" (formula-term varpars (:term2 rhs))))
    (is (= "[T^(1)]" (formula-term varpars (:term3 rhs))))
    (is (= "[T^(0)*L^(1)]" (formula-term varpars (:term4 rhs))))
    ))
;lein test :only diman.formula/formula-single-varpar
;; ============================================================================

;lein test diman.formula-test
