(ns diman.attach
  "Contains function.
  These are:

  - `tie-notn-expt`
  - `tie-subformula-expt`
  - `tie-notnlist-exptlist`
  - `tie-ref-with-distinct-next`
  - `tie-subformulae-in-term`
  - `tie-subformulae-in-eqn-side`
  - `tie-names-in-subformula`

  ## How to use
  ### Loading
  ```
  (:require [diman.attach :refer :all])
  ```
  ### Examples
  #### Attaching an exponent to one of seven fundamental notations
  For notation `[M]` with exponent 2
  ```
  => (tie-notn-expt \"[M]\" \"2\")
  \"M^(2)\"
  ```
  which is a formula with just one component, `M^(2)`.

  #### Attach exponent of a variable/parameter in the expression
  Consider the expression `v^2` where variable/parameter `v` stands for velocity with
  the standard formula `[M^(0)*L^(1)*T^(-1)]`. To attach the exponent (= 2) of `v` to
  its standard formula is to multiply all the exponents of the notations contained in
  the standard formula
  ```
  => (tie-subformula-expt \"[M^(0)*L^(1)*T^(-1)]\" \"2\")
  \"[M^(0.0)*L^(2.0)*T^(-2.0)]\"
  ```
  **NOTE**:

  - `tie-subformula-expt` is for all practical purpose multiplying exponents of all the
  notations in the formula by the desired exponent value.
  - it is similar to `allexpt-times-expt` in `diman.exponents` but unlike it
  `tie-subformula-expt` returns the same form of the formula (say, standard formula) but
  with exponents changed while `allexpt-times-expt` only returns the list of changed exponents.

  #### Attach list of notation and its corresponding list of exponents
  For a list `[(\"M\" \"L\" \"T\") (\"0\" \"1\" \"-2\")]` defined in `x` its formula is
  ```
  => (tie-notnlist-exptlist x)
  \"[M^(0)*L^(1)*T^(-2)]\"
  ```
  **NOTE**:

  - the list of notation and its exponents is usually that of a sub-formula, that is,
  formula of variable/parameter in a term comprised of >= variable/parameter
  - the list can be obtained using the `list-varpar-expt` (in `diman.filter` namespace)
  - for a list of sub-formulae = `lform = [form1 form2 from3 ...]` to get the
  list of notation and exponents of `form1` you get `x` from
  `(list-varpar-expt (remove-brackets (first lform)))`

  #### Attach distinct components in next sub-formula to the reference sub-formula
  Given an expression (lhs or rhs of the equation), let us assume that a variable/parameter
  for the defined problem has a formula representation. An expression is composed of
  subexpressions separated by **+** sign. We shall refer to each subexpression as a
  **term**.

  As a general convention formula for each term is referred here as a **sub-formula**. The
  sub-formula will be the result of the term regardless of the number of variable/parameter.
  A sub-formula consists of __components which themselves are made up of the base notations.__
  The number of components in a sub-formula resulting from a term with one variable/parameter
  will be the number of base notations in the sub-formula.

  However, if the sub-formula is a result of a term with more than one variable/parameter
  then each formula resulting from respective variable/parameter is referred to as the
  **supra-component**. A supra-component consists of components made up of base notations.

  A term with one variable/parameter will have only one supra-component or sub-formula
  (dimensional formula of the variable/parameter). A term with __n__ variable/parameters
  will have __n__ supra-components in **one** sub-formula. In such cases, the sub-formula of
  the term is the union of supra-components.

  For the example of the term whose list of sub-formulae is
  `(def lform '(\"[M^(0)*L^(1)*T^(-2)]\" \"[A^(1)*T^(2)]\" \"[cd]^(0)*[mol]^(-2)]\"))`
  \"[M^(0)*L^(1)*T^(-2)]\" is taken as the reference sub-formula because it is the longest.
  Looking at `\"[A^(1)*T^(2)]\"` the notation common with the reference is `T` so
  ```
  => (tie-ref-with-distinct-next (first lform) (second lform) \"[T^(2)]\")
  \"[T^(-2)*M^(0)*L^(1)*A^(1)]\"
  ```
  Considering `\"[cd]^(0)*[mol]^(-2)]\"` since there are `nil` notations in common with
  the reference
  ```
  => (tie-ref-with-distinct-next (first lform) (last lform) nil)
  \"[T^(-2)*mol^(-2)*M^(0)*cd^(0)*L^(1)]\"
  ```
  Also notice that tying with itself (reference subformula) will return itself
  ```
  => (tie-ref-with-distinct-next (first lform1) (first lform1) nil)
  \"[T^(-2)*M^(0)*L^(1)]\"
  ```
  Therefore, this function returns a sub-formula comprising all the distinct
  components among the sub-formulae resulting from the variable/parameter.

  #### Tying the sub-formulae of a term to get the **term formula**
  The sub-formulae of a term or expression will normally have notations and hence
  components that are in common to the sub-formulae generated from the variable/parameter
  defined in the expression. Therefore, to obtain the sub-formula of a term or an
  expression comprised of just one term the first step is to create a sub-formula
  composed of distinct components.

  Given the list of sub-formulae defined above
  ```
  => lform
  (\"[M^(0)*L^(1)*T^(-2)]\" \"[A^(1)*T^(2)]\" \"[cd]^(0)*[mol]^(-2)]\")
  ```
  with the reference sub-formula `\"[M^(0)*L^(1)*T^(-2)]\"`, notice that there are
  other sub-formula in the list that have components with same notation as in the
  reference sub-formula. Here, it is `T^(2)`. Also, although it is represented as a
  list the sub-formulae have the multiplication operator between them. With this
  knowledge and the law of exponents the updated reference sub-formula is
  ```
  \"[M^(0)*L^(1)*T^(0)]\"
  ```
  Then the formula of the term is its tied sub-formulae
  ```
  => (tie-subformulae-in-term lform \"[M^(0)*L^(1)*T^(0)]\")
  \"[mol^(-2)*T^(0)*M^(0)*cd^(0)*L^(1)*A^(1)]\"
  ```
  Notice that this function ties up the updated reference sub-formula with distinct
  components in the rest of the sub-formulae; note distinct relative to the reference.

  #### Tying the sub-formulae of an equation side to get its **formula**
  In the context of formula for a side of the equation the formulae of each of term
  are its sub-formulae. These then can be tied up using `tie-subformulae-in-eqn-side`.

  Therefore, if the right hand side of the equation is
  ```
  (def rhs {:term1 \"x^(1)\", :term2 \"v^(2)\", :term3 \"t^(1)\", :term4 \"0.5*a^(1)*t^(2)\"})
  ```
  resulting in its corresponding sub-formulae
  ```
  (\"[L^(1)]\" \"[T^(-2)*M^(0)*L^(2)]\" \"[T^(1)]\" \"[T^(0)*M^(0)*L^(1)]\")
  ```
  Then when tied up we get
  ```
  => (tie-subformulae-in-eqn-side '(\"[L^(1)]\" \"[T^(-2)*M^(0)*L^(2)]\" \"[T^(1)]\" \"[T^(0)*M^(0)*L^(1)]\"))
  \"[L^(1)] + [T^(-2)*M^(0)*L^(2)] + [T^(1)] + [T^(0)*M^(0)*L^(1)]\"
  ```
  NOTE: Unlike tying of sub-formulae for getting the term formula, sub-formulae tying
  for the equation side does not perform exponent operation. This is because for the
  case of equation side the sub-formulae are separated by the addition operator.

  #### Tying dimension names in a sub-formula
  To have a more readable view of the sub-formula of the term
  ```
  => (tie-subformulae-in-term lform \"[M^(0)*L^(1)*T^(0)]\")
  \"[mol^(-2)*T^(0)*M^(0)*cd^(0)*L^(1)*A^(1)]\"
  ```
  you can call `tie-names-in-subformula` as
  ```
  => (tie-names-in-subformula \"[mol^(-2)*T^(0)*M^(0)*cd^(0)*L^(1)*A^(1)]\")
  \"amount of substance^(-2)*length^(1)*electric current^(1)\"
  ```
  Notice that names of the dimensions with exponent value = 0 is not shown. If it were
  it would have appeared as
  ```
  \"amount of substance^(-2)*time^(0)*mass^(0)*luminous intensity^(0)*length^(1)*electric current^(1)\"
  ```
  "
  (:require [diman.utilities :refer [remove-brackets
                                     include-parentheses include-brackets]]
            [diman.filter :refer [list-varpar-expt notns-in-subformula
                                  names-in-subformula expts-in-subformula
                                  next-subformula-components-with-common-notation]]
            [diman.exponents :refer [allexpt-times-expt]]
            [clojure.set :refer [union difference]])        ; only necessary for lein test else ClassNotFoundException error due to clojure.set
  )

;; ============================================================================
;;     Function for tying a notation and exponent resulting in a component.
;; ============================================================================
(defn tie-notn-expt [notn expt]
  "Returns [<notn>^(<expt>)], i.e, a single component formula."
  (clojure.string/join "^" [(clojure.string/replace notn #"[\]]+" "")
                            (include-parentheses expt)])
  )
;; =====================================x======================================

;; ============================================================================
;; Function tying the sub-formula with exponent value of the variable/parameter.
;; ============================================================================
(defn- tie-components [lst_components]
  (clojure.string/join ["[" (clojure.string/join "*" lst_components) "]"])
  )

(defn- frame-notn-expt-component [notn expt]
  "Returns <notn>^(<expt>), i.e, a component."
  (clojure.string/join "^" [(clojure.string/replace notn #"[\[\]]+" "")
                            (include-parentheses expt)])
  )

(defn tie-subformula-expt
  "Returns sub-formula attached to the multiplied exponents, multiplied
  by a given exponent."
  ([subform varpar_expt] (tie-subformula-expt
                            (notns-in-subformula subform)
                            (allexpt-times-expt subform varpar_expt)
                            ()))
  ([lst_notn lst_expt lst_comps]
   (if (empty? lst_expt)
     (tie-components lst_comps)
     (recur (drop-last lst_notn) (drop-last lst_expt)
            (cons (frame-notn-expt-component (last lst_notn) (last lst_expt))
                  lst_comps))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;; Function tying the list of notations with its corresponding list of exponents
;; resulting in a sub-formula.
;; ============================================================================
(defn- tie-notn-expt-with-others [notn expt previous_notn_expt] ;tie-expt-notation-and-others
  "Returns *<notn>^<expt> if there are no preceding ones, else it returns
  *<notn>^<expt>*<prev_notn>^<prev_expt>."
  (if (empty? previous_notn_expt)
    (clojure.string/join ["*" notn "^" (include-parentheses expt)])
    (clojure.string/join ["*" notn "^" (include-parentheses expt) previous_notn_expt])
    ))

(defn tie-notnlist-exptlist
  "Returns sub-formula by tying the list of notations with its corresponding
  list of exponents."
  ([lst_notn_expt] (tie-notnlist-exptlist lst_notn_expt (first lst_notn_expt)
                                           (last lst_notn_expt) "" ))
  ([lst_notn_expt lst_notn lst_expt notn_expt_joined]
   (if (empty? lst_notn)
     (include-brackets (clojure.string/replace notn_expt_joined #"^[\*]" ""))
     (recur lst_notn_expt (drop-last lst_notn) (drop-last lst_expt)
            (tie-notn-expt-with-others (last lst_notn) (last lst_expt)
                                       notn_expt_joined))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;; Function tying the reference sub-formula with distinct notation components of
;; next sub-formula.
;; ============================================================================
(defn- set-of-notn-expt
  "Returns a set with elements <notn>^<expt> given a list. The list is such that its
  first is a list of notations and last is its list of corresponding exponents."
  ([lst_notn_expt] (set-of-notn-expt lst_notn_expt (first lst_notn_expt)
                                     (last lst_notn_expt) []))
  ([lst_notn_expt lst_notn lst_expt notn_expt_set]
    (if (empty? lst_notn)
      (set notn_expt_set)
      (recur lst_notn_expt (drop-last lst_notn) (drop-last lst_expt)
             (cons (clojure.string/replace
                     (tie-notn-expt (last lst_notn) (last lst_expt))
                     #"[\]]$" "")                           ; remove last bracket
                   notn_expt_set))
      ))
  )

(defn- set-to-subformula
  "Returns sub-formula to the given set of <notn>^<expt> by joining them with *."
  ([set_notn_expt] (set-to-subformula set_notn_expt ""))
  ([set_notn_expt formula]
    (if (empty? set_notn_expt)
      (clojure.string/join ["[" (clojure.string/replace formula #"^[\*]" "") "]"])
      (recur (drop-last set_notn_expt)
             (clojure.string/join
               [(clojure.string/join ["*" (last set_notn_expt)]) formula]))
      ))
  )

(defn tie-ref-with-distinct-next [ref_subform next_subform common]
  "Returns a sub-formula which is the union of the reference and difference of
  common from next sub-formula (i.t, next sub-formula without the common components)."
  (let [ref_set (set-of-notn-expt (list-varpar-expt (remove-brackets ref_subform)))]
    (if (and (nil? next_subform) (nil? common))
      ref_subform
      (if (and (not= nil next_subform) (nil? common))
        (let [next_set (set-of-notn-expt (list-varpar-expt (remove-brackets next_subform)))]
          (set-to-subformula (clojure.set/union ref_set next_set)))
        (let [next_set   (set-of-notn-expt (list-varpar-expt (remove-brackets next_subform)))
              common_set (set-of-notn-expt (list-varpar-expt (remove-brackets common)))]
          (set-to-subformula
            (clojure.set/union ref_set
                               (clojure.set/difference next_set common_set)))
          )
        )
      )
    ))
;; =====================================x======================================

;; ============================================================================
;; Function(s) for tying the reference sub-formula with distinct notation
;; components of next sub-formula, for tying the sub-formulae of an equation side
;; ============================================================================
(defn tie-subformulae-in-term
  "Returns formula of the terms by tying up sub-formulae. Specifically, tying the
  updated reference sub-formula with distinct components in rest of sub-formulae."
  ([lst_subform updated_ref_subform]
   (tie-subformulae-in-term lst_subform updated_ref_subform updated_ref_subform))
  ([lst_subform updated_ref_subform updated_ref_tied]
   (if (empty? lst_subform)
     updated_ref_tied
     (recur (drop-last lst_subform) updated_ref_subform
            (tie-ref-with-distinct-next updated_ref_tied (last lst_subform)
                                        (next-subformula-components-with-common-notation updated_ref_subform
                                                                                         (last lst_subform))
                                        ))
     ))
  )

(defn tie-subformulae-in-eqn-side [lst_formulae]
  "Returns formula of an equation side by tying up the sub-formulae (formula of
  each term) separated by the addition operator."
  (clojure.string/join " + " lst_formulae)
  )
;; =====================================x======================================

;; ============================================================================
;; Function tying a notation name with its exponent value for dimensional analysis.
;; ============================================================================
(defn- tie-name-expt [name expt]
  "Returns dimension name with its exponent value for value =/= 0, else return nil."
  ;(clojure.string/join [(grab-name (include-brackets notn)) "^"
  ;                      (include-parentheses expt)])
  (if (= expt "0")
    nil
    (clojure.string/join "^" [name (include-parentheses expt)])
    ))

(defn tie-names-in-subformula
  "Returns dimension name and its exponent value with each pair separated by *."
  ([subform] (tie-names-in-subformula subform
                                      (names-in-subformula subform)
                                      (expts-in-subformula subform) ""))
  ([subform lst_names lst_expts tied_name_expts]
   (if (empty? lst_names)
     (clojure.string/join "*" (remove nil? tied_name_expts))
     (recur subform (drop-last lst_names) (drop-last lst_expts)
            (cons (tie-name-expt (last lst_names) (last lst_expts))
                  tied_name_expts))
     ))
  )
;; =====================================x======================================