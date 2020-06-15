(ns diman.attach-test
  (:require [clojure.test :refer :all]
            [diman [attach :refer [tie-notn-expt tie-subformula-expt tie-notnlist-exptlist tie-ref-with-distinct-next
                                   tie-subformulae-in-term tie-subformulae-in-eqn-side tie-names-in-subformula]]]
            ))
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
;; ==================== TEST diman.attach/tie-notn-expt =======================
(deftest test-tie-notn-expt
  (testing "comment"
    (is (= "T^(2)" (tie-notn-expt "T" "2")))
    ))
;lein test :only diman.attach-test/test-tie-notn-expt
;; ============================================================================

;; ================= TEST diman.attach/tie-subformula-expt ====================
(deftest test-tie-subformula-expt
  (testing "comment"
    (is (= "[M^(0)*L^(2)*T^(-2)]" (tie-subformula-expt "[M^(0)*L^(1)*T^(-1)]" "2")))
    ))
;lein test :only diman.attach-test/test-tie-subformula-expt
;; ============================================================================

;; ================ TEST diman.attach/tie-notnlist-exptlist ===================
(deftest test-tie-notnlist-exptlist
  (testing "comment"
    (is (= "[M^(0)*L^(1)*T^(-2)]" (tie-notnlist-exptlist '[("M" "L" "T") ("0" "1" "-2")])))
    ))
;lein test :only diman.attach-test/test-tie-notnlist-exptlist
;; ============================================================================

;; ============== TEST diman.attach/tie-ref-with-distinct-next ================
(def subform '("[M^(0)*L^(1)*T^(-2)]" "[A^(1)*T^(2)]" "[cd]^(0)*[mol]^(-2)]"))
(deftest test-tie-ref-with-distinct-next
  (testing "comment"
    (is (= "[T^(-2)*M^(0)*L^(1)*A^(1)]" (tie-ref-with-distinct-next ; [T] in common
                                          (first subform) (second subform) "[T^(2)]")))
    (is (= "[T^(-2)*mol^(-2)*M^(0)*cd^(0)*L^(1)]" (tie-ref-with-distinct-next ; nil common
                                                    (first subform) (last subform) nil)))
    (is (= "[T^(-2)*M^(0)*L^(1)]" (tie-ref-with-distinct-next ; with itself
                                    (first subform) (first subform) nil)))
    ))
;lein test :only diman.attach-test/test-tie-ref-with-distinct-next
;; ============================================================================tie-subformulae-in-term

;; ============== TEST diman.attach/tie-subformulae-in-term ================
;(def subform '("[M^(0)*L^(1)*T^(-2)]" "[A^(1)*T^(2)]" "[cd]^(0)*[mol]^(-2)]"))
(def refsub "[M^(0)*L^(1)*T^(0)]")
(deftest test-tie-subformulae-in-term
  (testing "comment"
    (is (= "[mol^(-2)*T^(0)*M^(0)*cd^(0)*L^(1)*A^(1)]" (tie-subformulae-in-term subform refsub)))
    ))
;lein test :only diman.attach-test/test-tie-subformulae-in-term
;; ============================================================================

;; ============== TEST diman.attach/tie-subformulae-in-eqn-side ===============
(def rhs_eqn '("[L^(1)]" "[T^(-2)*M^(0)*L^(2)]" "[T^(1)]" "[T^(0)*M^(0)*L^(1)]"))
(deftest test-tie-subformulae-in-eqn-side
  (testing "comment"
    (is (= "[L^(1)] + [T^(-2)*M^(0)*L^(2)] + [T^(1)] + [T^(0)*M^(0)*L^(1)]"
           (tie-subformulae-in-eqn-side rhs_eqn)))
    ))
;lein test :only diman.attach-test/test-tie-subformulae-in-eqn-side
;; ============================================================================

;; ================ TEST diman.attach/tie-names-in-subformula =================
(deftest test-tie-names-in-subformula
  (testing "comment"
    (is (= "amount of substance^(-2)*length^(1)*electric current^(1)"
           (tie-names-in-subformula "[mol^(-2)*T^(0)*M^(0)*cd^(0)*L^(1)*A^(1)]")))
    ))
;lein test :only diman.attach-test/test-tie-names-in-subformula
;; ============================================================================

;lein test diman.attach-test
