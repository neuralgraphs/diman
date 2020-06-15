(ns diman.linalg.matfun.determinant
  (:require [diman.linalg.datafun [access :refer [get-column-vector submatrix insert-submatrix]]]
            [diman.linalg.datafun [maximum :refer [max-index]]]
            [diman.linalg [core :refer [zero-vector? size]]]
            [diman.linalg.elfun [absolute :refer [abs]]]
            [diman.linalg.matfun [gaussian-elimination :refer [reduce-up]]]
            ))
;; ========================================================================================
(def leftmost-nonzero-columnindx #'diman.linalg.matfun.gaussian-elimination/leftmost-nonzero-columnindx)
(def row-interchange #'diman.linalg.matfun.gaussian-elimination/row-interchange)
;;
(def divide-row-by-leading-entry #'diman.linalg.matfun.gaussian-elimination/divide-row-by-leading-entry)
(def subtract-rows #'diman.linalg.matfun.gaussian-elimination/subtract-rows)
;; ========================================================================================

(defn- row-interchange-by-pivot-with-sign [mat]
  "Given a matrix, this function **changes the matrix** such that
  the row with *pivot entry* interchanges with top row.
  It also returns the sign (+1 or -1) indicating interchange."
  (let [nonzero_clmn (get-column-vector mat (leftmost-nonzero-columnindx mat))
        pivotindx (max-index (abs nonzero_clmn))]
    [(row-interchange mat 0 pivotindx) ((fn [x] (if (zero? x) 1) -1) pivotindx)]
    ))

(defn- traverse-rows-down
  "Given a matrix, this function returns the matrix after
  going down the matrix during which the rows are subtracted.
  The subtrahend row in the returned matrix has the original elements.
  Due to this subtraction, for performing elimination the matrix passed here
  is after invoking `row-interchange-by-pivot`."
  ([mat] (traverse-rows-down mat (count mat) 1))
  ([mat m minuend_indx]
    (if (= minuend_indx m)
      mat
      (let [orig_top_row (get mat 0)
            setup_matrix (divide-row-by-leading-entry mat 0)
            subtracted_rows (subtract-rows setup_matrix minuend_indx 0)]
        (recur (assoc subtracted_rows 0 orig_top_row) m (+ minuend_indx 1)))
      )))

(defn- get-row-echelon
  "Function that puts `row-interchange-by-pivot` and `traverse-rows-down` together.
  Returns the augmented matrix in its row-echelon form."
  ([mat] (get-row-echelon mat [] (size mat) 0))
  ([aug_mat signs [m n] i]
   (if (= i m)
     [aug_mat signs]
     (let [submat (submatrix aug_mat [i i] [(- m i) (- n i)])
           [pivoted sign_id] (row-interchange-by-pivot-with-sign submat)
           row_echelon_form (insert-submatrix aug_mat (traverse-rows-down pivoted) i i)]
       (recur row_echelon_form (conj signs sign_id) [m n] (+ i 1))
       ))
    ))

(defn- signs-product
  "Returns product of all the indicators (+1 or -1) for row interchanges."
  ([x] (signs-product x 1))
  ([x ans] (if (empty? x) ans (recur (drop-last x) (* ans (last x))))
    ))

(defn- det-square-matrix
  ([mat] (det-square-matrix (get-row-echelon mat) (size mat) 1 0))
  ([[reduced_mat signs] [m n] ans i]
    (if (= i m)
      (* ans (signs-product signs))
      (recur [reduced_mat signs] [m n] (* ans (get-in reduced_mat [i i])) (+ i 1))
      )))

(defn det [mat]
  (let [[m n] (size mat)]
    (if (= m n)
      (det-square-matrix mat)
      nil)))



