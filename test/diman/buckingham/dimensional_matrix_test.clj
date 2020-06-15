(ns diman.buckingham.dimensional-matrix-test
  (:require [clojure.test :refer :all]
            [diman.buckingham [dimensional-matrix :refer [generate-dimmat]]]
            ))
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
;;  TEST diman.buckingham.dimensional-matrix/get-notn-expt-of-expressed-symbol
(deftest test-get-notn-expt-of-expressed-symbol
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/get-notn-expt-of-expressed-symbol]
      (is (= '[("L") ("1")] (fun varpars "x")))
      (is (= '[("T" "L") ("-1" "1")] (fun varpars "v")))
      )))
(deftest test-get-notn-expt-of-varpars
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/get-notn-expt-of-varpars]
      (is (= (fun varpars)
             '({:symbol "x" :notn_list ["L"] :expt_list ["1"]}
                {:symbol "v" :notn_list ["T" "L"] :expt_list ["-1" "1"]}
                {:symbol "t" :notn_list ["T"] :expt_list ["1"]}
                {:symbol "a" :notn_list ["T" "L"] :expt_list ["-2" "1"]})))
      )))
(deftest test-notn-collection
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/notn-collection]
      (is (= (fun '({:symbol "x" :notn_list ["L"] :expt_list ["1"]}
                     {:symbol "v" :notn_list ["T" "L"] :expt_list ["-1" "1"]}
                     {:symbol "t" :notn_list ["T"] :expt_list ["1"]}
                     {:symbol "a" :notn_list ["T" "L"] :expt_list ["-2" "1"]}))
             '("L" "T" "L" "T" "T" "L")))
      )))
(deftest test-size-dimmat
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/size-dimmat]
      (is (= (fun '("L" "T" "L" "T" "T" "L") varpars))
          [2 4]))
    ))
(deftest test-get-element
  (testing "comment"
    (let [x_varpar_with_notn_expt {:symbol "x" :notn_list ["L"] :expt_list ["1"]}
          v_varpar_with_notn_expt {:symbol "v" :notn_list ["T" "L"] :expt_list ["-1" "1"]}
          t_varpar_with_notn_expt {:symbol "t" :notn_list ["T"] :expt_list ["1"]}
          a_varpar_with_notn_expt {:symbol "a" :notn_list ["T" "L"] :expt_list ["-2" "1"]}
          fun #'diman.buckingham.dimensional-matrix/get-element]
      (is (= [1 0] [(fun "L" x_varpar_with_notn_expt) (fun "T" x_varpar_with_notn_expt)]))
      (is (= [1 -1] [(fun "L" v_varpar_with_notn_expt) (fun "T" v_varpar_with_notn_expt)]))
      (is (= [0 1] [(fun "L" t_varpar_with_notn_expt) (fun "T" t_varpar_with_notn_expt)]))
      (is (= [1 -2] [(fun "L" a_varpar_with_notn_expt) (fun "T" a_varpar_with_notn_expt)]))
      )))
(deftest test-get-column-vector
  (testing "comment"
    (let [x_varpar_with_notn_expt {:symbol "x" :notn_list ["L"] :expt_list ["1"]}
          v_varpar_with_notn_expt {:symbol "v" :notn_list ["T" "L"] :expt_list ["-1" "1"]}
          t_varpar_with_notn_expt {:symbol "t" :notn_list ["T"] :expt_list ["1"]}
          a_varpar_with_notn_expt {:symbol "a" :notn_list ["T" "L"] :expt_list ["-2" "1"]}
          fun #'diman.buckingham.dimensional-matrix/get-column-vector]
      (is (= '[1 0]) (fun ["L" "T"] x_varpar_with_notn_expt))
      (is (= '[1 -1]) (fun ["L" "T"] v_varpar_with_notn_expt))
      (is (= '[0 1]) (fun ["L" "T"] t_varpar_with_notn_expt))
      (is (= '[1 -2]) (fun ["L" "T"] a_varpar_with_notn_expt))
      )))
(def varpars_with_notn_expt [{:symbol "x" :notn_list ["L"] :expt_list ["1"]}
                             {:symbol "v" :notn_list ["T" "L"] :expt_list ["-1" "1"]}
                             {:symbol "t" :notn_list ["T"] :expt_list ["1"]}
                             {:symbol "a" :notn_list ["T" "L"] :expt_list ["-2" "1"]}])
(deftest test-get-row-vector
  (testing "comment"
    (let [fun #'diman.buckingham.dimensional-matrix/get-row-vector]
      (is (= '[1 1 0 1]) (fun "L" varpars_with_notn_expt))
      (is (= '[0 -1 1 -2]) (fun "T" varpars_with_notn_expt))
      )))
(deftest test-fillup-dimmat
  (testing "comment"
    (let [dimmat [[0 0 0 0] [0 0 0 0]]
          fun #'diman.buckingham.dimensional-matrix/fillup-dimmat]
      (is (= (fun ["L" "T"] varpars_with_notn_expt dimmat)
             '[[1 1 0 1]                                    ; row-0
               [0 -1 1 -2]]))                               ; row-1
      )))
;lein test :only diman.buckingham.dimensional-matrix-test/test-get-notn-expt-of-expressed-symbol
;lein test :only diman.buckingham.dimensional-matrix-test/test-get-notn-expt-of-varpars
;lein test :only diman.buckingham.dimensional-matrix-test/test-notn-collection
;lein test :only diman.buckingham.dimensional-matrix-test/test-size-dimmat
;lein test :only diman.buckingham.dimensional-matrix-test/test-get-element
;lein test :only diman.buckingham.dimensional-matrix-test/test-get-column-vector
    ;lein test :only diman.buckingham.dimensional-matrix-test/test-get-row-vector
;lein test :only diman.buckingham.dimensional-matrix-test/test-fillup-dimmat
;; ============================================================================

(deftest test-generate-dimmat
  (testing "comment"
    (is (= (generate-dimmat varpars)
           '[[1 1 0 1]                                    ; row-0
             [0 -1 1 -2]]))                               ; row-1
    ))
;lein test :only diman.buckingham.dimensional-matrix-test/test-generate-dimmat

;lein test diman.buckingham.dimensional-matrix-test
