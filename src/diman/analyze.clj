(ns diman.analyze
  "Contains function.
  These are

  - `dimnames`
  - `consistent?`

  ## How to use
  ### Loading
  ```
  (:require [diman.analyze :refer [dimnames consistent?]])
  ```
  ### Examples
  Consider the case
  ```
  (def varpars [{:symbol \"x\", :quantity \"length\"},
                {:symbol \"v\", :quantity \"velocity\"}
                {:symbol \"t\", :quantity \"time\"}
                {:symbol \"a\", :quantity \"acceleration\"}])
  (def lhs \"x^(1)\")
  (def rhs {:term1 \"x^(1)\", :term2 \"v^(2)\", :term3 \"t^(1)\", :term4 \"0.5*a^(1)*t^(2)\"})
  (def eqn {:lhs lhs, :rhs rhs})
  ```
  Then using ```(require '[diman.formula :refer [formula-term formula-eqn-side]])```
  The formula for the right hand side of the equation is
  ```
  => (formula-eqn-side varpars rhs)
  \"[L^(1)] + [T^(-2)*L^(2)] + [T^(1)] + [T^(0)*L^(1)]\"
  ```
  #### Dimension names of an equation side
  The rhs of our example equation as dimension names is
  ```
  => (dimnames (formula-eqn-side varpars rhs))
  \"length^(1) + time^(-2)*length^(2) + time^(1) + length^(1)\"
  ```
  and for lhs
  ```
  => (dimnames (formula-eqn-side varpars lhs))
  \"length^(1)\"
  ```
  Notice that names of the dimensions with exponent value = 0 is not seen.
  #### Consistency check
  To check if the lhs and rhs of the dimensional formula are the same do
  ```
  => (consistent? varpars eqn)
  true
  ```
  "
  (:require [diman.attach :refer [tie-names-in-subformula]]
            [diman.formula :refer [formula-eqn-side]])
  )

;; ============================================================================
;;    Function for representing the dimensional formula in terms of names.
;; ============================================================================
(defn dimnames
  "Returns names of the contents of the dimensional formula."
  ([eqn_form]
   (dimnames eqn_form (clojure.string/split
                        (clojure.string/replace eqn_form #"[\s]+" "")
                        #"[\+]")
             ""))
  ([eqn_form lst_subform lst_ans]
   (if (empty? lst_subform)
     (clojure.string/join " + " lst_ans)
     (recur eqn_form (drop-last lst_subform)
            (cons (tie-names-in-subformula (last lst_subform)) lst_ans))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;;             Function for performing Dimensional Consistency.
;; ============================================================================
(defn- replace-plus-by-empty-string [dimname_form]
  "Returns a vector of names of the dimensional formula with the plus sign
  replaced by an empty string."
  (let [wo_plus (clojure.string/replace dimname_form #"[\+]+" "")]
    (clojure.string/split wo_plus #" ")                     ; w/o space
    ))

(defn- clean-dimnames [dimnames_a_side]
  "Returns a list of names of the dimensional formula w/o the plus sign."
  (remove empty? (replace-plus-by-empty-string dimnames_a_side)))

(defn consistent? [varpar_def eqn]
  "Compares dimensional names on lhs vs rhs of the equation."
  (let [lhs (formula-eqn-side varpar_def (:lhs eqn))
        rhs (formula-eqn-side varpar_def (:rhs eqn))
        dimnames_lhs_cleaned (clean-dimnames (dimnames lhs))
        dimnames_rhs_cleaned (clean-dimnames (dimnames rhs))]
    (if (apply = dimnames_rhs_cleaned)                      ; all rhs names are same
      (= (sort (clojure.string/join " " dimnames_lhs_cleaned))
         (sort (first dimnames_rhs_cleaned)))               ; vs one rhs name
      (= (sort (clojure.string/join " " dimnames_lhs_cleaned))
         (sort (clojure.string/join " " dimnames_rhs_cleaned)))
      )
    ))
;; =====================================x======================================