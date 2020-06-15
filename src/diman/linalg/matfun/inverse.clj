(ns diman.linalg.matfun.inverse
  (:require [diman.linalg.matfun [gaussian-elimination :refer [get-row-echelon reduce-up]]]
            [diman.linalg [core :refer [size eye]]]
            [diman.linalg.datafun [access :refer [insert-submatrix submatrix]]]
            ))

(defn inv [mat]
  (let [[m m] (size mat)
        ident (eye m m)
        mat_I (insert-submatrix mat ident 0 m)
        mat_inv (reduce-up (get-row-echelon mat_I))]
    (if (= ident (submatrix mat_inv [0 0] [m m]))
      (submatrix mat_inv [0 m] [m m])                       ; "matrix is invertible"
      nil)))

