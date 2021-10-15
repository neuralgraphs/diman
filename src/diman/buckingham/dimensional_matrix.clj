(ns diman.buckingham.dimensional-matrix
  "Contains function

   - `generate-dimmat`

  Given
  ```
  (def varpars [ {:symbol \"x\", :quantity \"length\"}
                 {:symbol \"v\", :quantity \"velocity\"}
                 {:symbol \"t\", :quantity \"time\"}
                 {:symbol \"a\", :quantity \"acceleration\"} ])
  ```
  to get the [dimensional matrix](https://neuralgraphs.com/lectures/diman/lectp8.html#p4_thispage) of the system containing (only) the above dimensional quantities
  ```
  => (generate-dimmat varpars)
  [[1N 1N 0 1N] [0 -1N 1N -2N]]
  ```
  "
  (:require [diman.formula :refer [formula-term]]
            [diman.filter :refer [notns-in-subformula expts-in-subformula]]
            [diman.linalg [core :refer [zero-mat]]]
            ))

(defn- convert-symbol-in-varpar-to-expression [x]
  "x is the symbol, e.g. (:symbol (first varpars))."
  (clojure.string/join [x "^" "(1)"])
  )

(defn- get-notn-expt-of-expressed-symbol [varpars x]
  (let [expressed_symbol (convert-symbol-in-varpar-to-expression x)
        dimformula_symbol (formula-term varpars expressed_symbol)]
    [ (notns-in-subformula dimformula_symbol)
     (expts-in-subformula dimformula_symbol) ]
    ))

(defn- get-notn-expt-of-varpars
  ([varpars] (get-notn-expt-of-varpars
               varpars (:symbol (last varpars)) []))
  ([varpars curr_symbol varpar_with_notn_expt]
   (if (empty? varpars)
     varpar_with_notn_expt
     (let [notn_expt_list (get-notn-expt-of-expressed-symbol
                            varpars curr_symbol)
           abridged_varpars (drop-last varpars)]
       (recur abridged_varpars (:symbol (last abridged_varpars))
              (cons {:symbol curr_symbol
                     :notn_list (first notn_expt_list)
                     :expt_list (last notn_expt_list)}
                    varpar_with_notn_expt)
              ))
     )))

(defn- notn-collection
  ([notn_expt_of_varpars] (notn-collection notn_expt_of_varpars []))
  ([notn_expt_of_varpars ans]
   (if (empty? notn_expt_of_varpars)
     ans
     (recur (drop-last notn_expt_of_varpars)
            (into (:notn_list (last notn_expt_of_varpars)) ans))
     )
    ))

(defn- size-dimmat [notn_collection varpars]
  [(count (distinct notn_collection))                       ; no. of rows
   (count varpars)                                          ; no. of columns
   ])

(defn- get-element
  ([row_notn a_varpar_with_notn_expt]
   (get-element row_notn a_varpar_with_notn_expt
                [(:notn_list a_varpar_with_notn_expt)
                 (:expt_list a_varpar_with_notn_expt)]
                0))
  ([row_notn a_varpar_with_notn_expt notn_expt_list ans]
   (if (empty? (first notn_expt_list))
     ans
     (if (= (last (first notn_expt_list)) row_notn)
       (rationalize (Float/parseFloat (last (last notn_expt_list))))
       (recur row_notn a_varpar_with_notn_expt
              [(drop-last (first notn_expt_list))
               (drop-last (last notn_expt_list))]
              ans)
       ))
    ))

(defn- get-column-vector
  ([all_row_notns a_varpar_with_notn_expt]
   (get-column-vector all_row_notns a_varpar_with_notn_expt []))
  ([all_row_notns a_varpar_with_notn_expt ans]
   (if (empty? all_row_notns)
     (vec ans)
     (recur (drop-last all_row_notns) a_varpar_with_notn_expt
            (cons (get-element (last all_row_notns)
                               a_varpar_with_notn_expt)
                  ans)
            ))
    ))

(defn- get-row-vector
  ([row_notn varpars_with_notn_expt]
   (get-row-vector row_notn varpars_with_notn_expt []))
  ([row_notn varpars_with_notn_expt ans]
   (if (empty? varpars_with_notn_expt)
     (vec ans)
     (recur row_notn (drop-last varpars_with_notn_expt)
            (cons (get-element row_notn
                               (last varpars_with_notn_expt))
                  ans)
            ))
    ))

(defn- fillup-dimmat
  ([all_row_notns varpars_with_notn_expt dimmat]
   (fillup-dimmat all_row_notns varpars_with_notn_expt dimmat
                  (- (count dimmat) 1) (count (first dimmat))))
  ([all_row_notns varpars_with_notn_expt dimmat i n]
   (if (neg? i)
     dimmat
     (let [a_row_notn (last all_row_notns)
           row_vector (get-row-vector a_row_notn varpars_with_notn_expt)]
       (recur (drop-last all_row_notns) varpars_with_notn_expt
              (assoc dimmat i row_vector) (- i 1) n)
       ))
    ))

(defn generate-dimmat [varpars]
  (let [varpars_with_notn_expt (get-notn-expt-of-varpars varpars)
        notn_collection (notn-collection varpars_with_notn_expt)
        [m n] (size-dimmat notn_collection varpars)
        dimmat (zero-mat m n)]
    (fillup-dimmat (distinct notn_collection)
                   varpars_with_notn_expt dimmat)
    ))