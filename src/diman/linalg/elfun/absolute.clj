(ns diman.linalg.elfun.absolute)

(defn- abs-scalar [x] (if (neg? x) (- x) x) )

(defn- abs-vector
  ([x] (abs-vector x []))
  ([x ans]
   (if (empty? x)
     ans
     (recur (rest x) (conj ans (abs-scalar (first x))))
     )))

(defn- abs-matrix
  ([X] (abs-matrix X (- (count X) 1)))
  ([X i]
    (if (neg? i)
      X
      (recur (assoc X i (abs-vector (get X i))) (- i 1))
      )))

(defn abs [X]
  (if (number? X)
    (abs-scalar X)
    (if (vector? (first X))
      (abs-matrix X)
      (abs-vector X)
      )))