(ns diman.filter-test
  (:require [clojure.test :refer :all]
            [diman [filter :refer [list-varpar-expt notns-in-subformula expts-in-subformula names-in-subformula
                                   next-subformula-components-with-common-notation remove-zero-powers]]]
            ))

;; ===================== TEST diman.filter/list-varpar-expt ===================
(deftest test-list-varpar-expt
  (testing "comment"
    (is (= '[("a" "t") ("1" "2")] (list-varpar-expt "0.5*a^(1)*t^(2)")))
    ))
(deftest test-notns-in-subformula
  (testing "comment"
    (is (= '("L") (notns-in-subformula "[L^(1)]")))
    (is (= '("M" "L" "T" "A") (notns-in-subformula "[M^(-1)*L^(-2)*T^(3)*A^(2)]")))
    ))
(deftest test-expts-in-subformula
  (testing "comment"
    (is (= '("1") (expts-in-subformula "[L^(1)]")))
    (is (= '("-1" "-2" "3" "2") (expts-in-subformula "[M^(-1)*L^(-2)*T^(3)*A^(2)]")))
    (is (= '("0" "1/2" "-2/3") (expts-in-subformula "[M^(0)*L^(1/2)*T^(-2/3)]")))
    ))
(deftest test-names-in-subformula
  (testing "comment"
    (is (= '("length") (names-in-subformula "[L^(1)]")))
    (is (= '("mass" "length" "time" "electric current") (names-in-subformula "[M^(-1)*L^(-2)*T^(3)*A^(2)]")))
    ))
;lein test :only diman.filter-test/test-list-varpar-expt
;lein test :only diman.filter-test/test-notns-in-subformula
;lein test :only diman.filter-test/test-expts-in-subformula
;lein test :only diman.filter-test/test-names-in-subformula
;; ============================================================================

;; ==== TEST diman.filter/next-subformula-components-with-common-notation =====
(def subform '("[M^(0)*L^(1)*T^(-2)]" "[A^(1)*T^(2)]" "[cd]^(0)*[mol]^(-2)]"))
(deftest test-next-subformula-components-with-common-notation
  (testing "comment"
    (is (= "[T^(2)]" (next-subformula-components-with-common-notation
                       (first subform) (second subform)))) ; [T] is the common notation
    (is (= nil (next-subformula-components-with-common-notation
                 (first subform) (last subform)))) ; no common notation
    (is (= nil (next-subformula-components-with-common-notation
                 (first subform) (first subform))))
    ))
;lein test :only diman.filter-test/test-next-subformula-components-with-common-notation
;; ============================================================================

;; ================== TEST diman.filter/remove-zero-powers ====================
(deftest test-remove-zero-powers
  (testing "comment"
    (is (= "[L^(1)*T^(-2)]" (remove-zero-powers "[M^(0)*L^(1)*T^(-2)]")))
    (is (= "[L^(1)]" (remove-zero-powers "[M^(0)*L^(1)*T^(0)]")))
    (is (= "[p^(1.0)*t^(-11.0)*u^(5.0)*v^(8.0)]"
           (remove-zero-powers "[p^(1.0)*q^(0.0)*r^(0.0)*s^(0.0)*t^(-11.0)*u^(5.0)*v^(8.0)]")))
    (is (= "[q^(1/2)]" (remove-zero-powers "[p^(0)*q^(1/2)*r^(0.0)]")))
    ))
;lein test :only diman.filter-test/test-remove-zero-powers
;; ============================================================================

;lein test diman.filter-test
