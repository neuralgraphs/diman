(ns diman.linalg.core-test
  (:require [clojure.test :refer :all]
            [diman.linalg [core :refer [zero-mat eye zero-vector?]]]
            ))

(deftest test-create-zero-matrix
  (testing "comment"
    (is (= '[[0 0 0] [0 0 0]] (zero-mat 2 3)))
    ))

(deftest test-eye
  (testing "comment"
    (is (= '[[1 0 0 0] [0 1 0 0] [0 0 1 0] [0 0 0 1]] (eye 4 4)))
    ))

(deftest test-zero-vector?
  (testing "comment"
    (is (true? (zero-vector? [0 0 0 0])))
    (is (false? (zero-vector? [0 0 1 0])))
    ))

;(def mat [[1 2 3] [1 2 3] [1 2 3] [1 2 3]])
;(deftest test-get-column-vector
;  (testing "comment"
;    (is (= '[1 1 1 1] (get-column-vector mat 0)))
;    (is (= '[2 2 2 2] (get-column-vector mat 1)))
;    (is (= '[3 3 3 3] (get-column-vector mat 2)))
;    ))

;(deftest test-abs-vector
;  (testing "comment"
;    (is (= '[1 2 3 4] (abs-vector [1 2 3 4])))
;    (is (= '[1 3 1 5] (abs-vector [1 -3 1 -5])))
;    ))

;(deftest test-max-index-vector
;  (testing "comment"
;    (is (= 3 (max-index-vector [1 2 3 4])))
;    (is (= 2 (max-index-vector [1 2 3 -4])))
;    (is (= 0 (max-index-vector [1 -3 1 -5])))
;    ))