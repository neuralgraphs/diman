(ns diman.buckingham.homogeneous-equation
  (:require [diman.linalg.matfun [rank :refer [rank]]]
            [diman.linalg.matfun [gaussian-elimination :refer [get-row-echelon reduce-up]]]
            [diman.linalg.ops [multiplication :refer [times]]]
            [diman.linalg.datafun [access :refer [submatrix insert-submatrix]]]
            [diman.linalg [core :refer [size zero-vector? transpose eye]]]
            ))

(defn get-augmented-matrix [dimmat]
  "Transforms the dimensional matrix and returns the augmented matrix.
  Rows in the augmented matrix is number of varpars minus the number of dimensionless products (i.e. rank).
  Columns in augmented matrix is number of varpars."
  (let [[m n] (size dimmat)
        rank_dimmat (rank dimmat)
        no_dimless (- n rank_dimmat)
        submat_LHS (submatrix dimmat [0 0] [m no_dimless])
        submat_RHS (submatrix dimmat [0 no_dimless] [m rank_dimmat])
        augmat (insert-submatrix dimmat submat_RHS 0 0)]    ; dimmat RHS -> augmat LHS
    (insert-submatrix augmat (times -1 submat_LHS) 0 (last (size submat_RHS))) ; dimmat LHS -> augmat RHS
    ))

(defn solve [aug_mat] (reduce-up (get-row-echelon aug_mat)))

(defn- strip-zero-rows
  ([X] (strip-zero-rows X []))
  ([X adjustedX]
    (if (empty? X)
      adjustedX
      (recur (rest X)
             ((fn [y Y] (if (zero-vector? y) Y (conj Y y))) (first X) adjustedX)
             ))
    ))

(defn get-solved-matrix [solved_augmat]
  "Transforms the solved augmented matrix such that the returned matrix
  rows are the number of dimensionless products (i.e rank) and
  columns are the number of varpars."
  (let [adjusted_mat (strip-zero-rows solved_augmat)
        [m n] (size adjusted_mat)
        submat_solved (transpose (submatrix adjusted_mat [0 m] [m (- n m)]))
        solved_matrix (eye (- n m) n)]
    (insert-submatrix solved_matrix submat_solved 0 (- n m))
    ))
