(ns diman.exponents
  "Contains functions.
  These are:

  - `allexpt-times-expt`
  - `ref-expt-plus-expt-allmatch`

  ## How to use
  ### Loading
  ```
  (:require [diman.exponents :refer [allexpt-times-expt
                                     ref-expt-plus-expt-allmatch]])
  ```

  ### Examples
  #### Taking exponent of all the notations within a sub-formula
  For `(def subform1 \"[M^(2)]\")`, to square it, ie., perform \"[M^(2)]^2\"
  ```
  => (allexpt-times-expt subform1 \"2\")
  (\"4.0\")
  ```
  For `(def subform2 \"[M^(2/3)*cd^(1)*mol^(-2)]\")` with exponent \"(2)\")
  ```
  => (allexpt-times-expt subform2 \"3\")
  (\"2.0\" \"3.0\" \"-6.0\")
  ```
  **NOTE**:

  - Function returns a list of exponents
  - list length of corresponds to the number of notations in the expression

  #### Adding exponents among components with same notations
  For an expression `(def expr \"0.5*a^(1)*t^(2)\")` resulting in a
  dimensional formula with components `\"[M^(0)*L^(1)*T^(-2)]\"` and `\"[T^(2)]\"`.
  The 'longer' sub-formula is taken as the reference. Since all its components are
  separated by the multiplier `*` operator any exponents of common notations must be
  added. That is, `\"[M^(0)*L^(1)*T^(-2)] * [T^(2)] = [M^(0)*L^(1)*T^(-2+2)]\"`.

  Thus, for list of sub-formulae`(def lform '(\"[M^(0)*L^(1)*T^(-2)]\" \"[T^(2)]\")`
  ```
  => (ref-expt-plus-expt-allmatch (first formcomps) (last formcomps))
  [(\"M\" \"L\" \"T\") (\"0\" \"1\" \"0\")]
  ```
  returns a list of notations which is the same as the original reference sub-formula
  (first argument) but updated exponent values as a result of adding the exponents in
  in components with common notations in the next sub-formula (second argument).
  "
  (:require [diman.utilities :refer [remove-parentheses remove-brackets]]
            [diman.filter :refer [list-varpar-expt expts-in-subformula notns-in-subformula]])
  )

;; ============================================================================
;; Function for multiplying an exponent value to all exponents of components in
;; the sub-formula.
;; ============================================================================
(defn- convert-to-num [expt]
  "Returns numerical version of the exponent (string).
  NOTE: (last list_of_expts) returns expt as (\"expt \")."
  (let [matcher (re-matcher #"[\-\d|\d]+" (remove-parentheses expt))
        e_num   (re-find matcher)
        e_den   (re-find matcher)]
    (if (nil? e_den)                                        ; 0.0 will be taken such that
      (Float. (re-find #"[\-\d|\d]+" e_num))                ; 0. is e_num and .0 is e_den
      (/ (Float. (re-find #"[\-\d|\d]+" e_num))             ; divide by 0 will occur if
         (Float. (re-find #"[\-\d|\d]+" e_den)))            ; expt is not rationalized
      ))
  )

(defn- expt-times-expt [expt1 expt2]
  "Returns product as string version.
  NOTE: both expt1 and expt2 are strings."
  (str (rationalize (* (convert-to-num expt1) (convert-to-num expt2)))) ; crucial to rationalize
  )

(defn allexpt-times-expt
  "Returns multiplied list of exponents in the sub-formula."
  ([subform its_expt] (allexpt-times-expt subform its_expt
                                          (expts-in-subformula subform) ()))
  ([subform its_expt lst_subform_expts prod]
   (if (empty? lst_subform_expts)
     prod
     (recur subform its_expt (drop-last lst_subform_expts)
            (cons (expt-times-expt (last lst_subform_expts) its_expt) prod))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;;   Function for adding exponent values among components with same notations.
;; ============================================================================
(defn- expt-plus-expt [expt1 expt2]
  "Returns sum as string version.
  NOTE: both expt1 and expt2 are strings."
  (str (rationalize (+ (convert-to-num expt1) (convert-to-num expt2)))) ; crucial to rationalize
  )

(defn- ref-expt-plus-expt-match
  "Returns updated reference such that only ONE exponent of the corresponding
  notation is updated. The updated reference is returned as a list whose first
  is list of notations and last is list of exponents."
  ([ref_notn_expt a_notn a_expt]
   (ref-expt-plus-expt-match ref_notn_expt a_notn a_expt
                              (first ref_notn_expt) (last ref_notn_expt)
                              () ()))
  ([ref x e ref_notn ref_expt ref_notn_updated ref_expt_updated]
   (if (empty? ref_notn)
     [ref_notn_updated ref_expt_updated]
     (if (= x (last ref_notn))
       (recur ref x e (drop-last ref_notn) (drop-last ref_expt)
              (cons (last ref_notn) ref_notn_updated)
              (cons (expt-plus-expt e (last ref_expt)) ref_expt_updated))
       (recur ref x e (drop-last ref_notn) (drop-last ref_expt)
              (cons (last ref_notn) ref_notn_updated)
              (cons (last ref_expt) ref_expt_updated))
       ))
    )
  )

(defn ref-expt-plus-expt-allmatch
  "Returns updated reference sub-formula such that ALL exponents of the
  corresponding notations are updated. The updated reference is returned
  as a list whose first is list of notations and last is list of exponents."
  ([ref_subform next_subform]
   (ref-expt-plus-expt-allmatch
     (list-varpar-expt (remove-brackets ref_subform))
     (list-varpar-expt (remove-brackets next_subform))
     (notns-in-subformula (remove-brackets next_subform))
     (expts-in-subformula (remove-brackets next_subform))))
  ([ref_notn_expt_updated next_notn_expt lst_x lst_e]
   (if (empty? lst_x)
     ref_notn_expt_updated
     (recur (ref-expt-plus-expt-match ref_notn_expt_updated
                                       (last lst_x) (last lst_e))
            next_notn_expt (drop-last lst_x) (drop-last lst_e))
     ))
  )
;; =====================================x======================================