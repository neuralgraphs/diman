(ns diman.formula-test
  (:require [clojure.test :refer :all]
            [diman [formula :refer [formula-term formula-eqn-side]]]
            ))
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
(def lhs "x^(1)")
(def rhs {:term1 "x^(1)", :term2 "v^(2)", :term3 "t^(1)", :term4 "0.5*a^(1)*t^(2)"})
(def eqn {:lhs lhs, :rhs rhs})
;; ==================== TEST diman.attach/tie-notn-expt =======================
(deftest test-get-notn-expt-of-expressed-symbol
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/get-notn-expt-of-expressed-symbol]
      (is (= '[("L") ("1")] (fun varpars "x")))
      (is (= '[("T" "L") ("-1" "1")] (fun varpars "v")))
      )))
;(deftest test-tie-notn-expt
;  (testing "comment"
;    (is (= "T^(2)" (tie-notn-expt "T" "2")))
;    ))
;lein test :only diman.attach-test/test-tie-notn-expt
;; ============================================================================

;lein test diman.formula-test
