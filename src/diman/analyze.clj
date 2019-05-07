(ns diman.analyze
  "Contains function.
  These are

  - `dimnames`

  ## How to use
  ### Loading
  ```
  (:require [diman.analyze :refer [allexpt-times-expt]])
  ```
  ### Examples
  Consider the case
  ```
  (def varpars [{:symbol \"x\", :dimension \"length\"},
                {:symbol \"v\", :dimension \"velocity\"}
                {:symbol \"t\", :dimension \"time\"}
                {:symbol \"a\", :dimension \"acceleration\"}])
  (def lhs \"x^(1)\")
  (def rhs {:term1 \"x^(1)\", :term2 \"v^(2)\", :term3 \"t^(1)\", :term4 \"0.5*a^(1)*t^(2)\"})
  (def eqn {:lhs lhs, :rhs rhs})
  ```
  Then using ```(require '[diman.formula :refer [formula-term formula-eqn-side]])```
  The formula for the right hand side of the equation is
  ```
  => (formula-eqn-side varpars rhs)
  \"[L^(1)] + [T^(-2)*M^(0)*L^(2)] + [T^(1)] + [T^(0)*M^(0)*L^(1)]\"
  ```
  whose dimension names are
  ```
  => (dimnames (formula-eqn-side varpars rhs))
  \"length^(1) + time^(-2)*length^(2) + time^(1) + length^(1)\"
  ```
  Notice that names of the dimensions with exponent value = 0 is not seen.
  "
  (:require [diman.attach :refer [tie-names-in-subformula]])
  )

(defn dimnames
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
