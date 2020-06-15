(ns diman.linalg.matfun.gaussian-elimination
  "Contains function.
  These are:

  - `get-row-echelon` to get row-echelon form
  - `reduce-up` to get reduced row-echelon form (invoke `get-row-echelon` **first**)
  "
  (:require [diman.linalg.datafun [access :refer [get-column-vector submatrix insert-submatrix]]]
            [diman.linalg.datafun [maximum :refer [max-index]]]
            [diman.linalg [core :refer [zero-vector? size]]]
            [diman.linalg.elfun [absolute :refer [abs]]]
            [diman.linalg.ops [division :refer [rdivide]]
                              [multiplication :refer [times]]
                              [subtraction :refer [minus]]]
            ))
;;
;; ============================ row-interchange-by-pivot ==================================
(defn- leftmost-nonzero-columnindx
  "Given a matrix, this function returns the index for nonzero column vector."
  ([mat] (leftmost-nonzero-columnindx mat 0 (- (count (first mat)) 1)))
  ([mat current_j last_j]
   (if (not (zero-vector? (get-column-vector mat current_j)))
     current_j
     (if (= current_j last_j)
       current_j
       (recur mat (+ current_j 1) last_j)
       ))
    ))

(defn- row-interchange [mat indx1 indx2]
  "Given a matrix and two indices, this function **changes the matrix** such that
  its row of first index interchanges with row of second index."
  (let [row1 (get mat indx1)                                ;row1_to_row2 (assoc mat indx2 row1)
        row2 (get mat indx2)]                               ;row2_to_row1 (assoc mat indx1 row2)
    (assoc (assoc mat indx2 row1) indx1 row2)
    ))

(defn- row-interchange-by-pivot [mat]
  "Given a matrix, this function **changes the matrix** such that
  the row with *pivot entry* interchanges with top row."
  (let [nonzero_clmn (get-column-vector mat (leftmost-nonzero-columnindx mat))
        pivotindx (max-index (abs nonzero_clmn))]
    (row-interchange mat 0 pivotindx)
    ))
;; ========================================================================================

;; ============================= solve-down and solve-up ==================================
(defn- first-nonzero-leading-entry
  "Given a matrix and a row index, this function returns its nonzero leading entry."
  ([mat row_indx] (first-nonzero-leading-entry              ; the row is extracted and
                    (reverse (get mat row_indx)) row_indx 0)) ; reversed for tail-recursion
  ([the_row row_indx default_ans]                           ; default_ans initialized at 0
   (if (empty? the_row)                                     ; which get returned if no
     default_ans                                            ; entry is nonzero.
     (if (zero? (last the_row))                             ; If first row entry is zero
       (recur (drop-last the_row) row_indx default_ans)     ; Update row without the zero entry.
       (last the_row)                                       ; Otherwise return the the row entry.
       ))
    ))

(defn- divide-row-by-leading-entry [mat row_indx]
  "Given a matrix and a row index, this function returns the matrix such that
  the row (for the row index) is divided by its leading entry."
  (let [a (first-nonzero-leading-entry mat row_indx)]
    (if (zero? a)
      mat
      (assoc mat row_indx (rdivide (get mat row_indx) a))
        )))

(defn- subtract-rows [mat minuend_indx subtrahend_indx]
  "Given a matrix and two indices, this function returns the matrix such that
  the row of the first index is subtracted by the row of the second index
  (multiplied by the first entry of the minuend row) which
  prior to subtraction is divided by the the first column (leading) entry of the
  the row of the first index."
  (let [minuend_row_entry (get-in mat [minuend_indx 0])]       ; for (minuend_indx, 0)
    (if (zero? minuend_row_entry)                            ; If entry is zero,
      mat                                                   ; Return unchanged matrix.
      (let [minuend_row (get mat minuend_indx)
            subtrahend_row (times minuend_row_entry (get mat subtrahend_indx))]
        (assoc mat minuend_indx (minus minuend_row subtrahend_row)))
    )))

(defn- traverse-rows-down
  "Given a matrix, this function returns the matrix after
  going down the matrix during which the rows are subtracted.
  The subtrahend row in the returned matrix is result of the division
  by the leading entry.
  Due to this subtraction, for performing elimination the matrix passed here
  is after invoking `row-interchange-by-pivot`."
  ([mat] (traverse-rows-down (divide-row-by-leading-entry mat 0) (count mat) 1))
  ([mat m minuend_indx] (if (= minuend_indx m)
                          mat
                          (recur (subtract-rows mat minuend_indx 0) m (+ minuend_indx 1))
                          )))

(defn- traverse-rows-up
  "Given a matrix, this function returns the matrix after
  going up the matrix during which the rows are subtracted.
  Due to this subtraction, for performing elimination the matrix passed here
  is after invoking `traverse-rows-down`."
  ([mat] (traverse-rows-up mat (- (count mat) 1) (- (count mat) 2)))
  ([mat subtrahend_indx minuend_indx] (if (neg? minuend_indx)
                          mat
                          (recur (subtract-rows mat minuend_indx subtrahend_indx)
                                 subtrahend_indx (- minuend_indx 1))
                          )))

(defn get-row-echelon
  "Function that puts `row-interchange-by-pivot` and `traverse-rows-down` together.
  Returns the augmented matrix in its row-echelon form."
  ([aug_mat] (get-row-echelon aug_mat (size aug_mat) 0))
  ([aug_mat [m n] i]
   (if (= i m)
     aug_mat
     (let [submat (submatrix aug_mat [i i] [(- m i) (- n i)])
           pivoted (row-interchange-by-pivot submat)
           row_echelon_aug (insert-submatrix aug_mat (traverse-rows-down pivoted) i i)]
       (recur row_echelon_aug [m n] (+ i 1))
       ))
    ))

(defn reduce-up
  "Function that puts all the numerous times `traverse-rows-up` is called together.
  Returns the augmented matrix in reduced row-echelon form."
  ([aug_mat] (reduce-up aug_mat (size aug_mat) 0))
  ([aug_mat [m n] i]
   (if (= i m)
     aug_mat
     (let [clmn_indx (- m (+ i 1))
           submat (submatrix aug_mat [0 clmn_indx] [(- m i) (- n clmn_indx)])
           soln_aug (insert-submatrix aug_mat (traverse-rows-up submat) 0 clmn_indx)]
       (recur soln_aug [m n] (+ i 1))
       ))
    ))
;; ========================================================================================