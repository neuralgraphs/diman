(ns diman.linalg.datafun.access)

(defn get-column-vector
  ([X j] (get-column-vector X j (vec (repeat (count X) 0)) (- (count X) 1)))
  ([X j ans i]
   (if (neg? i)
     ans
     (recur X j (assoc ans i (get-in X [i j])) (- i 1))
     )))

(defn get-diag-vector
  ([X] (get-diag-vector X (vec (repeat (count X) 0)) (- (count X) 1)))
  ([X d i]
    (if (neg? i)
      d
      (recur X (assoc d i (get-in X [i i])) (- i 1))
      )))

(defn submatrix
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

(defn- update-parent-row [parent_row from_j sub_row]
  (if (zero? from_j)
    (vec (concat sub_row (subvec parent_row (count sub_row))))
    (vec (concat (subvec parent_row 0 from_j) sub_row))
    ))

(defn insert-submatrix
  ([X sub from_i from_j] (insert-submatrix X sub from_i from_j (count sub)))
  ([X sub i j m]
   (if (empty? sub)
     X
     (let [parent_row (get X i)
           updated_parent_row (update-parent-row parent_row j (first sub))]
       (recur (assoc X i updated_parent_row) (rest sub) (+ i 1) j m)
       ))))
