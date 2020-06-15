(ns diman.linalg.ops.addition)

(defn- plus-vector-by-scalar
  ([x s] (plus-vector-by-scalar x s (- (count x) 1)))
  ([x s j]
   (if (neg? j)
     x
     (recur (assoc x j (+ (get x j) s)) s (- j 1))
     )))

(defn- plus-vector-by-vector
  ([x y] (plus-vector-by-vector x y (- (count x) 1)))
  ([x y j]
   (if (neg? j)
     x
     (recur (assoc x j (+ (get x j) (get y j))) y (- j 1))
     )))

(defn- plus-matrix-by-scalar
  ([X s] (plus-matrix-by-scalar X s (- (count X) 1)))
  ([X s i]
   (if (neg? i)
     X
     (recur (assoc X i (plus-vector-by-scalar (get X i) s)) s (- i 1))
     )))

(defn- plus-matrix-by-vector
  ([X y] (plus-matrix-by-vector X y (- (count X) 1)))
  ([X y i]
   (if (neg? i)
     X
     (recur (assoc X i (plus-vector-by-vector (get X i) y)) y (- i 1))
     )))

(defn- plus-matrix-by-matrix
  ([X Y] (plus-matrix-by-matrix X Y (- (count X) 1)))
  ([X Y i]
   (if (neg? i)
     X
     (recur (assoc X i (plus-vector-by-vector (get X i) (get Y i))) Y (- i 1))
     )))

(defn plus [X addend]
  (if (vector? (first X))
    (if (number? addend)
      (plus-matrix-by-scalar X addend)
      (if (number? (first addend))
        (plus-matrix-by-vector X addend)
        (plus-matrix-by-matrix X addend)
        ))
    (if (number? addend)
      (plus-vector-by-scalar X addend)
      (plus-vector-by-vector X addend)
      )
    ))