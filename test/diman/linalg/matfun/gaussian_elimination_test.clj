(ns diman.linalg.matfun.gaussian-elimination-test
  (:require [clojure.test :refer :all]
            [diman.linalg.matfun [gaussian-elimination :refer [get-row-echelon reduce-up]]]
            ))
;; ============================================================================
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
(def aug1 [[0 0 -2 0 7 12]
           [2 4 -10 6 12 28]
           [2 4 -5 6 -5 -1]])
;   column 0^
(def aug2 [[0 0 -2 0 0 7 12]
           [0 0 5 0 -17 -29]])
;       column 2^
(def aug3 [[0 0 0 0 0.5 1]])
;            column 4^
(deftest test-leftmost-nonzero-columnindx
  (testing "TEST diman.linalg.matfun.gaussian-elimination/leftmost-nonzero-columnindx"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/leftmost-nonzero-columnindx]
      (is (= 0 (fun aug1)))
      (is (= 2 (fun aug2)))
      (is (= 4 (fun aug3))))
    ))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-leftmost-nonzero-columnindx
;; ============================================================================

;; ============================================================================
(def aug4 [[2 4 -10 6 12 28]                                ; <- row-0
           [0 0 -2 0 7 12]                                  ; <- row-1
           [2 4 -5 6 -5 -1]])
(def aug4changed [[0 0 -2 0 7 12]
                  [2 4 -10 6 12 28]
                  [2 4 -5 6 -5 -1]])
(def aug5 [[1 3 -2 0 2 0 0]
           [0 0 1 2 0 3 1]
           [0 0 0 0 0 0 0]                                  ; <- row-2
           [0 0 0 0 0 6 2]])                                ; <- row-3
(def aug5changed [[1 3 -2 0 2 0 0]
                  [0 0 1 2 0 3 1]
                  [0 0 0 0 0 6 2]
                  [0 0 0 0 0 0 0]])

(deftest test-row-interchange
  (testing "TEST diman.linalg.matfun.gaussian-elimination/leftmost-row-interchange"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/row-interchange]
      (is (= aug4changed (fun aug4 0 1)))
      (is (= aug5changed (fun aug5 2 3)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-row-interchange-by-pivot
;; ============================================================================

;; ============================================================================
(def aug6 [[3 2 -1 1]                                       ; <- row-0
           [6 6 2 12]                                       ; <- row-1
           [3 -2 1 11]])
; pivot is ^ (6) row-1
(def aug6changed [[6 6 2 12]
                  [3 2 -1 1]
                  [3 -2 1 11]])
(def aug7 [[-1 -2 -5]
           [-5 0 5]])
;  pivot is ^ (-5) row-1
(def aug7changed [[-5 0 5]
                  [-1 -2 -5]])

(deftest test-row-interchange-by-pivot
  (testing "TEST diman.linalg.matfun.gaussian-elimination/leftmost-row-interchange-by-pivot"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/row-interchange-by-pivot]
      (is (= aug6changed (fun aug6)))
      (is (= aug7changed (fun aug7)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-row-interchange-by-pivot
;; ============================================================================

;; ============================================================================
(def aug8 [[1 3 -2 0 2 0 0]                                 ; (1)
           [0 0 1 2 0 3 1]                                  ; (1)
           [0 0 0 0 0 0 0]                                  ; (0)
           [0 0 0 0 0 6 2]])                                ; (6)
(deftest test-first-nonzero-leading-entry
  (testing "TEST diman.linalg.matfun.gaussian-elimination/first-nonzero-leading-entry"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/first-nonzero-leading-entry]
      (is (= 1 (fun aug8 0)))
      (is (= 1 (fun aug8 1)))
      (is (= 0 (fun aug8 2)))
      (is (= 6 (fun aug8 3)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-first-nonzero-leading-entry
;; ============================================================================

;; ============================================================================
(def aug9 [[2 4 -10 6 12 28]                                ; <- (2) at (0,0)
           [0 0 -2 0 7 12]
           [2 4 -5 6 -5 -1]])
(def aug9divided [[1 2 -5 3 6 14]
                  [0 0 -2 0 7 12]
                  [2 4 -5 6 -5 -1]])
(def aug10 [[1 1 1/3 2]
            [0 -5 0 5]                                      ; <- (-5) at (1,1)
            [0 -1 -2 -5]])
(def aug10divided [[1 1 1/3 2]
                   [0 1 0 -1]
                   [0 -1 -2 -5]])
(deftest test-divide-row-by-leading-entry
  (testing "TEST diman.linalg.matfun.gaussian-elimination/divide-row-by-leading-entry"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/divide-row-by-leading-entry]
      (is (= aug9divided (fun aug9 0)))
      (is (= aug10divided (fun aug10 1)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-divide-row-by-leading-entry
;; ============================================================================

;; ============================================================================
(def aug11 [[1 2 -5 3 6 14]                                 ; <- subtrahend-row 0
            [0 0 -2 0 7 12]
            [2 4 -5 6 -5 -1]])                              ; <- minuend-row 2
(def aug11subtracted [[1 2 -5 3 6 14]
                      [0 0 -2 0 7 12]
                      [0 0 5 0 -17 -29]])
(def aug12 [[1 1/3 2]
            [1 0 -1]                                        ; <- subtrahend-row 1
            [-1 -2 -5]])                                    ; <- minuend-row 2
(def aug12subtracted [[1 1/3 2]
                      [1 0 -1]
                      [0 -2 -6]])
(deftest test-subtract-rows
  (testing "TEST diman.linalg.matfun.gaussian-elimination/subtract-rows"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/subtract-rows]
      (is (= aug11subtracted (fun aug11 2 0)))
      (is (= aug12subtracted (fun aug12 2 1)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-subtract-rows
;; ============================================================================

;; ============================================================================
(def aug13 [[0 -2 1 -2 1 -3 0]
            [2 1 2 -1 0 1 0]
            [1 -1 2 0 -1 0 -3]])
(def aug13down [[1 1/2 1   -1/2  0   1/2 0]
                [0 1  -1/2  1   -1/2 3/2 0]
                [0 0   1    8   -7   7 -12]])
(defn- submatrix
  ([X [from_i from_j] [m n]]
   (submatrix X [from_i from_j] [(+ m from_i) (+ n from_j)]
              (vec (repeat m (vec (repeat n 0)))) 0 m))
  ([X [from_i from_j] [to_i to_j] sub i m]
   (if (= i m)
     sub
     (recur X [(+ from_i 1) from_j] [to_i to_j]
            (assoc sub i (subvec (get X from_i) from_j to_j))
            (+ i 1) m)
     )))
(defn- submatrix-to-parent
  ([X sub from_i from_j] (submatrix-to-parent X sub from_i from_j (count sub)))
  ([X sub i j m]
    (if (empty? sub)
      X
      (let [parent_row (get X i)
            parent_row_elements (subvec parent_row 0 j)
            updated_parent_row (vec (concat parent_row_elements (first sub)))]
        (recur (assoc X i updated_parent_row) (rest sub) (+ i 1) j m)
        ))))
(deftest test-traverse-rows-down
  (testing "TEST diman.linalg.matfun.gaussian-elimination/traverse-rows-down"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/traverse-rows-down
          pivoted1 (#'diman.linalg.matfun.gaussian-elimination/row-interchange-by-pivot aug13)
          traversed1 (fun pivoted1)                         ; still shape of parent matrix
          submat1 (submatrix traversed1 [1 1] [2 6])
          pivoted2 (#'diman.linalg.matfun.gaussian-elimination/row-interchange-by-pivot submat1)
          traversed2 (submatrix-to-parent traversed1 (fun pivoted2) 1 1) ; shape of parent matrix
          submat2 (submatrix traversed2 [2 2] [1 5])
          ;pivoted3 (#'diman.buckingham.gaussian-elimination/row-interchange-by-pivot submat2) not needed
          traversed3 (submatrix-to-parent traversed2 (fun submat2) 2 2)
          ]
      (is (= aug13down traversed3))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-traverse-rows-down
;; ============================================================================

;; ============================================================================
(def aug13up [[1 0 0 -11 9 -9 15]
              [0 1 0 5 -4 5 -6]
              [0 0 1 8 -7 7 -12]])
(deftest test-traverse-rows-up
  (testing "TEST diman.linalg.matfun.gaussian-elimination/traverse-rows-up"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/traverse-rows-up
          submat1 (submatrix aug13down [0 2] [3 5])
          traversed1 (submatrix-to-parent aug13down (fun submat1) 0 2)
          submat2 (submatrix traversed1 [0 1] [2 6])
          traversed2 (submatrix-to-parent traversed1 (fun submat2) 0 1)]
      (is (= aug13up traversed2))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-traverse-rows-up
;; ============================================================================

;; ============================================================================
(def aug14 [[0 -2 1 -2 1 -3 0]
            [2 1 2 -1 0 1 0]
            [1 -1 2 0 -1 0 -3]])
(def aug14down [[1 1/2  1   -1/2  0   1/2 0]
                [0 1   -1/2  1   -1/2 3/2 0]
                [0 0    1    8   -7   7 -12]])
(deftest test-get-row-echelon
  (testing "TEST diman.linalg.matfun.gaussian-elimination/get-row-echelon"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/get-row-echelon]
      (is (= aug14down (fun aug14)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-get-row-echelon
;; ============================================================================

;; ============================================================================
(def aug14up [[1 0 0 -11 9 -9 15]
              [0 1 0  5 -4 5 -6]
              [0 0 1  8 -7 7 -12]])
(deftest test-reduce-up
  (testing "TEST diman.linalg.matfun.gaussian-elimination/reduce-up"
    (let [fun #'diman.linalg.matfun.gaussian-elimination/reduce-up]
      (is (= aug14up (fun aug14down)))
      )))
;lein test :only diman.linalg.matfun.gaussian-elimination-test/test-reduce-up
;; ============================================================================

;lein test diman.linalg.matfun.gaussian-elimination-test