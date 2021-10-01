(ns diman.dimensions-test
  (:require [clojure.test :refer :all]
            [diman [dimensions :refer [notation? grab-notation grab-name grab-sformula matched-notation-sformula]]]
            ))

;; ====================== TEST diman.dimensions/notation? =====================
(deftest test-notation?
  (testing "comment"
    (is (= true (notation? "[M]")))
    (is (= true (notation? "[L]")))
    (is (= true (notation? "[T]")))
    (is (= true (notation? "[A]")))
    (is (= true (notation? "[K]")))
    (is (= true (notation? "[cd]")))
    (is (= true (notation? "[mol]")))
    (is (= false (notation? "[B]")))
    (is (= false (notation? "this is not a notation")))
    ))
;lein test :only diman.dimensions-test/test-notation?
;; ============================================================================

;; ==================== TEST diman.dimensions/grab-notation ===================
(deftest test-grab-notation
  (testing "comment"
    (is (= "[M]" (grab-notation "mass")))
    (is (= "[L]" (grab-notation "length")))
    (is (= "[T]" (grab-notation "time")))
    (is (= "[A]" (grab-notation "electric current")))
    (is (= "[K]" (grab-notation "thermodynamic temperature")))
    (is (= "[cd]" (grab-notation "luminous intensity")))
    (is (= "[mol]" (grab-notation "amount of substance")))
    ))
;lein test :only diman.dimensions-test/test-grab-notation
;; ============================================================================

;; ==================== TEST diman.dimensions/grab-name =======================
(deftest test-grab-name
  (testing "comment"
    (is (= "mass" (grab-name "[M]")))
    (is (= "length" (grab-name "[L]")))
    (is (= "time" (grab-name "[T]")))
    (is (= "electric current" (grab-name "[A]")))
    (is (= "thermodynamic temperature" (grab-name "[K]")))
    (is (= "luminous intensity" (grab-name "[cd]")))
    (is (= "amount of substance" (grab-name "[mol]")))
    ))
;lein test :only diman.dimensions-test/test-grab-name
;; ============================================================================

;; ================== TEST diman.dimensions/grab-sformula =====================
(deftest test-grab-sformula
  (testing "comment"
    (is (= "[L^(3)]" (grab-sformula "volume")))
    (is (= "[M^(1)*L^(2)*T^(-2)]" (grab-sformula "work")))
    (is (= "[M^(1)*L^(2)*T^(-2)]" (grab-sformula "energy")))
    (is (= "[M^(-1)*L^(-2)*T^(4)*A^(2)]" (grab-sformula "capacitance")))
    (is (= "[M^(-1)*L^(-2)*T^(3)*A^(2)]" (grab-sformula "conductance")))
    (is (not= "[M^(-1)*L^(-2)*T^(3)*A^(1)]" (grab-sformula "conductance")))
    (is (= nil (grab-sformula "undefined quantity")))
    ))
;lein test :only diman.dimensions-test/test-grab-sformula
;; ============================================================================

;; ============ TEST diman.dimensions/matched-notation-sformula ===============
(def varpars [{:symbol "x", :quantity "length"}
              {:symbol "v", :quantity "velocity"}
              {:symbol "t", :quantity "time"}
              {:symbol "a", :quantity "acceleration"}])
(deftest test-matched-notation-sformula
  (testing "comment"
    (is (= "[L]" (matched-notation-sformula varpars "x")))
    (is (= "[L^(1)*T^(-2)]" (matched-notation-sformula varpars "a")))
    (is (not= "[L^(1)*T^(2)]" (matched-notation-sformula varpars "v")))
    (is (= nil (matched-notation-sformula varpars "y")))
    ))
;lein test :only diman.dimensions-test/test-matched-notation-sformula
;; ============================================================================

;lein test diman.dimensions-test
