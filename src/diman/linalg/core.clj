(ns diman.linalg.core
  (:require [diman.linalg.datafun [access :refer [get-column-vector]]]
            ))

(defn zero-vec [n] (vec (repeat n 0)))

(defn zero-mat [m n] (vec (repeat m (zero-vec n)))) ; elmat

(defn eye                                                   ; elmat
  ([m n] (eye (- m 1) n (zero-mat m n)))
  ([i n X] (if (neg? i) X
             (recur (- i 1) n (assoc-in X [i i] 1))
             )))

(defn zero-vector? [x] (if (= x (zero-vec (count x))) true false)) ; core

(defn transpose                                             ; ops
  ([mat] (transpose mat (- (count (first mat)) 1)
                    (zero-mat (count (first mat)) (count mat))))
  ([mat mat_j trans] (if (neg? mat_j)
                       trans
                       (recur mat (- mat_j 1)
                              (assoc trans mat_j (get-column-vector mat mat_j))
            ))))

(defn size [X]                                              ; elmat
  (if (vector? (first X))
    [(count X) (count (first X))]
    (count X)))

(defn diag                                                  ; elmat
  ([x] (diag x (zero-mat (count x) (count x)) (- (count x) 1)))
  ([x D i] (if (neg? i)
             D
             (recur (drop-last x) (assoc-in D [i i] (last x)) (- i 1))
      )))
