(ns diman.linalg.ops.addition-test
  (:require [clojure.test :refer :all]
            [diman.linalg.ops [addition :refer [plus]]]
            ))
(def x [1 -3 2 4])
(def X [[1 6 8]
        [4 10 -2]
        [0 2 -20]])
(def y [2 3 4])
(def Y [[1 2 3]
        [4 5 6]
        [7 8 9]])
(def xplusminus3 [-2 -6 -1 1])
(def Xplus3 [[4 9 11]
             [7 13 1]
             [3 5 -17]])
(def xplusx [2 -6 4 8])
(def Xplusy [[3 9 12]
             [6 13 2]
             [2 5 -16]])
(def XplusY [[2 8 11]
             [8 15 4]
             [7 10 -11]])
(deftest test-plus
  (testing "comment"
    (is (= xplusminus3 (plus x -3)))
    (is (= Xplus3 (plus X 3)))
    (is (= xplusx (plus x x)))
    (is (= Xplusy (plus X y)))
    (is (= XplusY (plus X Y)))
    ))
;lein test :only diman.linalg.ops.addition-test/test-plus
;lein test diman.linalg.ops.addition-test
