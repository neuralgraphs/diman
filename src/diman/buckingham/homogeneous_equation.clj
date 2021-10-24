(ns diman.buckingham.homogeneous-equation
  "Contains function

   - `get-augmented-matrix`
   - `solve`
   - `get-solution-matrix`

  Given the dimensional matrix of a system
  ```
  (def dimat [ [2 -1 3 0 0 -2 1]
               [1 0 -1 0 2 1 2]
               [0 1 0 3 1 -1 2] ])
  ```
  to get the augmented matrix
  ```
  => (get-augmented-matrix dimat)
  [[0 -2 1 -2 1 -3 0] [2 1 2 -1 0 1 0] [1 -1 2 0 -1 0 -3]]
  ```
  and solve
  ```
  => (solve (get-augmented-matrix dimat))
  [[1 0N 0N -11N 9N -9N 15N] [0 1 0N 5N -4N 5N -6N] [0 0N 1N 8N -7N 7N -12N]]
  ```
  To get the solution matrix
  ```
  => (get-solution-matrix (solve (get-augmented-matrix dimat)))
  [[1 0 0 0 -11N 5N 8N] [0 1 0 0 9N -4N -7N] [0 0 1 0 -9N 5N 7N] [0 0 0 1 15N -6N -12N]]
  ```
  "
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
  "Renamed to `get-solution-matrix`"
  (let [adjusted_mat (strip-zero-rows solved_augmat)
        [m n] (size adjusted_mat)
        submat_solved (transpose (submatrix adjusted_mat [0 m] [m (- n m)]))
        solved_matrix (eye (- n m) n)]
    (insert-submatrix solved_matrix submat_solved 0 (- n m))
    ))

(defn get-solution-matrix [solved_augmat]
  "Transforms the solved augmented matrix such that the returned matrix
  rows are the number of dimensionless products (i.e rank) and
  columns are the number of varpars. [Replaces `get-solved-matrix`]"
  (get-solved-matrix solved_augmat))