(ns diman.linalg.datafun.maximum)

(defn- max-value-vector [x] (apply max x))

(defn- max-value-matrix
  ([X] (max-value-matrix X (- (count X) 1) []))
  ([X i temp_max]
    (if (neg? i)
      (max-value-vector temp_max)
      (recur X (- i 1) (conj temp_max (max-value-vector (get X i))))
      )))

(defn max-value [X]
  (if (vector? (first X))
    (max-value-matrix X)
    (max-value-vector X)
    ))

(defn- max-index-vector [x] (.indexOf x (apply max x)))      ; (map first (filter #(= (second %) (apply max x)) (map-indexed vector x)))

(defn- max-index-matrix
  ([X] (max-index-matrix X (- (count X) 1) [] [] []))
  ([X i temp_max temp_i temp_j]
    (if (neg? i)
      (let [indx (max-index-vector temp_max)]
        [(nth temp_i indx) (nth temp_j indx)])
      (recur X (- i 1)
             (conj temp_max (max-value-vector (get X i)))
             (conj temp_i i)
             (conj temp_j (max-index-vector (get X i))))
      )))

(defn max-index [X]
  (if (vector? (first X))
    (max-index-matrix X)
    (max-index-vector X)
    ))