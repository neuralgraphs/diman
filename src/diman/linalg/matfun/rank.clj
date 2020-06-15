(ns diman.linalg.matfun.rank
  (:require [diman.linalg [core :refer [zero-vector?]]]
            [diman.linalg.matfun [gaussian-elimination :refer [get-row-echelon]]]
            ))

(defn- count-nonzero-rows
  ([mat] (count-nonzero-rows mat 0))
  ([mat r]
    (if (empty? mat)
      r
      (recur (drop-last mat)
             (+ r ((fn [x] (if (zero-vector? x) 0 1)) (last mat)))
             ))))

(defn rank [mat]
  "Rank of a matrix is the number of nonzero rows in its row echelon form."
  (let [row_echelon_mat (get-row-echelon mat)]
    (count-nonzero-rows row_echelon_mat)))