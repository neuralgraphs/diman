(ns diman.formula
  "Contains function.
  These are:

  - `formula-term`
  - `formula-eqn-side`
  - `formula-eqn-side-manifold`

  ## How to use
  ### Loading
  ```
  (require '[diman.formula :refer [formula-term formula-eqn-side formula-eqn-side-manifold]])
  ```
  ### Example
  Given
  ```
  (def varpars [{:symbol \"x\", :quantity \"length\"}
                {:symbol \"v\", :quantity \"velocity\"}
                {:symbol \"t\", :quantity \"time\"}
                {:symbol \"a\", :quantity \"acceleration\"}])
  (def lhs \"x^(1)\")
  (def rhs {:term1 \"x^(1)\", :term2 \"v^(2)\", :term3 \"t^(1)\", :term4 \"0.5*a^(1)*t^(2)\"})
  (def eqn {:lhs lhs, :rhs rhs})
  ```
  #### Formula of term
  To get the formula of a term, say, `(:term4 rhs)`
  ```
  => (formula-term varpars (:term4 rhs))
  \"[T^(0)*L^(1)]\"
  ```
  and for left hand side
  ```
  => (formula-term varpars lhs)
  \"[L^(1)]\"
  ```
  #### Formula of a side of the equation
  For right hand side
  ```
  => (formula-eqn-side varpars rhs)
  \"[L^(1)] + [T^(-2)*L^(2)] + [T^(1)] + [T^(0)*L^(1)]\"
  ```
  for left hand side
  ```
  => (formula-eqn-side varpars lhs)
  \"[L^(1)]\"
  ```
  #### Evaluating formula for a side for multiple equations
  Lets say we have two different equations
  ```
  (def p_leftside \"p^(1)\")
  (def p_rightside {:term1 \"x^(2)*v^(-1)*t^(1)\"})
  (def p_equation {:lhs p_leftside, :rhs p_rightside})
  ```
  and
  ```
  (def q_leftside \"q^(1)\")
  (def q_rightside {:term1 \"x^(1)*a^(6)*t^(20)\"})
  (def q_equation {:lhs q_leftside, :rhs q_rightside})
  ```
  The RHS of the above two equations can be evaluated in one step using
  `formula-eqn-side-manifold`. To do this let us set up its argument
  ```
  (def manifold_eqn [{:name \"p_quantity\" :eqn (:rhs p_equation)}
                     {:name \"q_quantity\" :eqn (:rhs q_equation)}])
  ```
  Now we may invoke the method by passing the above as the argument
  ```
  => (formula-eqn-side-manifold varpars manifold_eqn)
  [{:quantity \"p_quantity\", :dimension \"[L^(-1)*L^(2)*T^(2)]\"}
  {:quantity \"q_quantity\", :dimension \"[L^(6)*T^(8)*L^(1)]\"}]
  ```
  **NOTE:** If the user plans to insert these derived dimensional formulae into
  the `standard_formula` the name given for respective `:quantity` in the above
  argument (here we named it `manifold_eqn`) **should be the name defined** for
  the quantity (so, in the definition `varpars`).
  "
  (:require [diman.utilities :refer [key-in-expr?]]
            [diman.dimensions :refer [notation? matched-notation-sformula]]
            [diman.filter :refer [list-varpar-expt
                                  next-subformula-components-with-common-notation]]
            [diman.exponents :refer [ref-expt-plus-expt-allmatch]]
            [diman.attach :refer [tie-notn-expt
                                  tie-subformula-expt
                                  tie-notnlist-exptlist
                                  tie-subformulae-in-term
                                  tie-subformulae-in-eqn-side]])
  )

;(require '[flatland.ordered.set :refer [ordered-set]])

;; ============================================================================
;; Dimensional formula for a term in a chosen side (lhs/rhs) of the equation.
;; ============================================================================
(defn- formula-single-varpar [varpar_def a_varpar_symb a_varpar_expt]
  "Returns formula for a given variable/parameter."
  (let [notn_sform (matched-notation-sformula varpar_def a_varpar_symb)
        its_expt   a_varpar_expt]
    (if (notation? notn_sform)
      (tie-notn-expt notn_sform its_expt)               ; exponent in expression
      (tie-subformula-expt notn_sform its_expt)))
  )

(defn- formula-all-varpar-for-term
  "Returns list of formula for each variable/parameter for given expression term."
  ([varpar_def term] (formula-all-varpar-for-term varpar_def
                                                  (first (list-varpar-expt term))
                                                  (last (list-varpar-expt term))
                                                  ""))
  ([varpar_def lst_varpar lst_expt lst_formula_components]
    (if (empty? lst_varpar)
      lst_formula_components
      (recur varpar_def (drop-last lst_varpar) (drop-last lst_expt)
             (cons
               (formula-single-varpar varpar_def (last lst_varpar) (last lst_expt))
               lst_formula_components))
      ))
  )

(defn- formula-with-most-notations
  "Returns the formula component with the most number of notations, component is
  from list of formula, otherwise returns just that one formula in the list."
  ([lst_subform] (formula-with-most-notations lst_subform
                                              lst_subform
                                              () () ))
  ([lst_subform changing_list lst_counts lst_indx]
    (if (empty? changing_list)
      (nth lst_subform (nth lst_indx
                            (.indexOf lst_counts (apply max lst_counts))))
      (recur lst_subform (drop-last changing_list)
             (cons (count (last changing_list)) lst_counts)
             (cons (- (count changing_list) 1) lst_indx))
      ))
  )

(defn- update-reference-with-next-subformula [ref_subform next_subform]
  "Returns updated reference subformula (in the list of subformulae) by adding
  exponents of corresponding notations in the other subformula."
  (let [common (next-subformula-components-with-common-notation ref_subform next_subform)]
    (if (nil? common)
      ref_subform
      (tie-notnlist-exptlist (ref-expt-plus-expt-allmatch ref_subform common))
      )
    ))

(defn- updated-reference-subformula-overall
  "Returns updated reference subformula (in the list of subformulae) after going
  through all (but itself) the subformulae in the list."
  ([lst_formula_comps]
   (updated-reference-subformula-overall lst_formula_comps
                                         (formula-with-most-notations lst_formula_comps)
                                         (formula-with-most-notations lst_formula_comps)))
  ([lst_comps ref_comp ref_comp_updated]
   (if (empty? lst_comps)
     ref_comp_updated
     (if (= ref_comp (last lst_comps))
       (recur (drop-last lst_comps) ref_comp ref_comp_updated)
       (recur (drop-last lst_comps) ref_comp
              (update-reference-with-next-subformula ref_comp_updated (last lst_comps)))
       )
     ))
  )

(defn formula-term [varpar_def term_in_expr]
  "Returns formula of a term."
  (let [lst_subform (formula-all-varpar-for-term varpar_def term_in_expr)]
    (tie-subformulae-in-term lst_subform
                          (updated-reference-subformula-overall lst_subform)))
  )
;; =====================================x======================================

;; ============================================================================
;;   Function for adding exponent values among components with same notations.
;; ============================================================================
(defn- list-formula-all-terms
  "Returns list of sub-formulae of all terms of a side (rhs/lhs) of the equation."
  ([varpar_def eqn_side_expr] (list-formula-all-terms varpar_def eqn_side_expr ""))
  ([varpar_def expr lst]
    (if (empty? expr)
      lst
      (recur varpar_def (drop-last expr)
             (cons (formula-term varpar_def (last (last expr))) lst))
      ))
  )

(defn formula-eqn-side [varpar_def eqn_side_expr]
  "Returns formula of side (right or left) of the equation"
  (if (key-in-expr? eqn_side_expr)
    (tie-subformulae-in-eqn-side (list-formula-all-terms varpar_def eqn_side_expr))
    (formula-term varpar_def eqn_side_expr))
  )

(defn formula-eqn-side-manifold
  ([varpar_def manifold_eqn_side_expr]
   (formula-eqn-side-manifold varpar_def manifold_eqn_side_expr []))
  ([varpars manifold_eqn manifold_formula]
   (if (empty? manifold_eqn)
     manifold_formula
     (recur varpars (rest manifold_eqn)
            (conj manifold_formula
                  {:quantity (:name (first manifold_eqn))
                   :dimension (formula-eqn-side varpars (:eqn (first manifold_eqn)))}))
     )))
;; =====================================x======================================
