(ns diman.linalg.ops.subtraction)

(defn- minus-vector-by-scalar
  ([x s] (minus-vector-by-scalar x s (- (count x) 1)))
  ([x s j]
   (if (neg? j)
     x
     (recur (assoc x j (- (get x j) s)) s (- j 1))
     )))

(defn- minus-vector-by-vector
  ([x y] (minus-vector-by-vector x y (- (count x) 1)))
  ([x y j]
   (if (neg? j)
     x
     (recur (assoc x j (- (get x j) (get y j))) y (- j 1))
     )))

(defn- minus-matrix-by-scalar
  ([X s] (minus-matrix-by-scalar X s (- (count X) 1)))
  ([X s i]
   (if (neg? i)
     X
     (recur (assoc X i (minus-vector-by-scalar (get X i) s)) s (- i 1))
     )))

(defn- minus-matrix-by-vector
  ([X y] (minus-matrix-by-vector X y (- (count X) 1)))
  ([X y i]
   (if (neg? i)
     X
     (recur (assoc X i (minus-vector-by-vector (get X i) y)) y (- i 1))
     )))

(defn- minus-matrix-by-matrix
  ([X Y] (minus-matrix-by-matrix X Y (- (count X) 1)))
  ([X Y i]
   (if (neg? i)
     X
     (recur (assoc X i (minus-vector-by-vector (get X i) (get Y i))) Y (- i 1))
     )))

(defn minus [X subtrahend]
  (if (vector? (first X))
    (if (number? subtrahend)
      (minus-matrix-by-scalar X subtrahend)
      (if (number? (first subtrahend))
        (minus-matrix-by-vector X subtrahend)
        (minus-matrix-by-matrix X subtrahend)
        ))
    (if (number? subtrahend)
      (minus-vector-by-scalar X subtrahend)
      (minus-vector-by-vector X subtrahend)
      )
    ))
