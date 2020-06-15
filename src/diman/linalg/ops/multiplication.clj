(ns diman.linalg.ops.multiplication)

(defn- times-vector-by-scalar
  ([a x] (times-vector-by-scalar a x (- (count x) 1)))
  ([a x j]
   (if (neg? j)
     x
     (recur a (assoc x j (* a (get x j))) (- j 1))
     )))

(defn- times-vector-by-vector
  ([y x] (times-vector-by-vector y x (- (count x) 1)))
  ([y x j]
   (if (neg? j)
     x
     (recur y (assoc x j (* (get y j) (get x j)))(- j 1))
     )))

(defn- times-matrix-by-scalar
  ([a X] (times-matrix-by-scalar a X (- (count X) 1)))
  ([a X i]
   (if (neg? i)
     X
     (recur a (assoc X i (times-vector-by-scalar a (get X i))) (- i 1))
     )))

(defn- times-matrix-by-matrix
  ([Y X] (times-matrix-by-matrix Y X (- (count X) 1)))
  ([Y X i]
   (if (neg? i)
     X
     (recur Y (assoc X i (times-vector-by-vector (get Y i) (get X i))) (- i 1))
     )))

(defn- times-matrix-by-vector
  ([y X] (times-matrix-by-vector y X (- (count X) 1)))
  ([y X i]
   (if (neg? i)
     X
     (recur y (assoc X i (times-vector-by-vector y (get X i))) (- i 1))
     )))

(defn times [multiplier X]
  (if (vector? (first X))
    (if (number? multiplier)
      (times-matrix-by-scalar multiplier X)
      (if (number? (first multiplier))
        (times-matrix-by-vector multiplier X)
        (times-matrix-by-matrix multiplier X)
        ))
    (if (number? multiplier)
      (times-vector-by-scalar multiplier X)
      (times-vector-by-vector multiplier X)
      )
    ))