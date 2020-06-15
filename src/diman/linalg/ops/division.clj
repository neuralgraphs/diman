(ns diman.linalg.ops.division)

(defn- rdivide-vector-by-scalar
  ([x d] (rdivide-vector-by-scalar x d (- (count x) 1)))
  ([x d j]
    (if (neg? j)
      x
      (recur (assoc x j (/ (get x j) d)) d (- j 1))
      )))

(defn- rdivide-vector-by-vector
  ([x y] (rdivide-vector-by-vector x y (- (count x) 1)))
  ([x y j]
    (if (neg? j)
      x
      (recur (assoc x j (/ (get x j) (get y j))) y (- j 1))
      )))

(defn- rdivide-matrix-by-scalar
  ([X d] (rdivide-matrix-by-scalar X d (- (count X) 1)))
  ([X d i]
   (if (neg? i)
     X
     (recur (assoc X i (rdivide-vector-by-scalar (get X i) d)) d (- i 1))
     )))

(defn- rdivide-matrix-by-matrix
  ([X Y] (rdivide-matrix-by-matrix X Y (- (count X) 1)))
  ([X Y i]
    (if (neg? i)
      X
      (recur (assoc X i (rdivide-vector-by-vector (get X i) (get Y i))) Y (- i 1))
      )))

(defn- rdivide-matrix-by-vector
  ([X x] (rdivide-matrix-by-vector X x (- (count X) 1)))
  ([X x i]
   (if (neg? i)
     X
     (recur (assoc X i (rdivide-vector-by-vector (get X i) x)) x (- i 1))
     )))

(defn rdivide [X divisor]
  (if (vector? (first X))
    (if (number? divisor)
      (rdivide-matrix-by-scalar X divisor)
      (if (number? (first divisor))
        (rdivide-matrix-by-vector X divisor)
        (rdivide-matrix-by-matrix X divisor)
        ))
    (if (number? divisor)
      (rdivide-vector-by-scalar X divisor)
      (rdivide-vector-by-vector X divisor)
      )
    ))
