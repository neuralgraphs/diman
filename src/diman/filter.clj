(ns diman.filter
  "Contains functions.
  These are:

  - `list-varpar-expt`
  - `notns-in-subformula`
  - `expts-in-subformula`
  - `names-in-subformula`
  - `next-subformula-components-with-common-notation`

  ## How to use
  ### Loading
  ```
  (:require [diman.filter :refer [list-varpar-expt notns-in-subformula
                                  expts-in-subformula names-in-subformula
                                  next-subformula-components-with-common-notation])
  ```
  ### Examples
  #### Filter out the variable/parameter and its exponents separately
  Given an expression
  ```(def expr \"0.5*a^(1)*t^(2)\")```
  the variable/parameter and corresponding exponents can be filtered out as
  ```
  => (list-varpar-expt expr)
  [(\"a\" \"t\") (\"1\" \"2\")]
  ```
  #### Filtering out one of the seven fundamental notations and its exponents
  A sub-formula (special case of dimensional formula)
  ```(def subform1 \"[M^(0)*L^(1)*T^(-2)]\")```
  is also an expression. Therefore,
  ```
  => (list-varpar-expt (remove-brackets subform1))
  [(\"M\" \"L\" \"T\") (\"0\" \"1\" \"-2\")]
  ```
  Similarly, for `(def subform2 \"[M^(2/3)*cd^(1)*mol^(-2)]\")`
  ```
  => (list-varpar-expt (remove-brackets subform2))
  [(\"M\" \"cd\" \"mol\") (\"2/3\" \"1\" \"-2\")]
  ```

  #### Filter out the notations, notations names in a sub-formula and also its corresponding exponents
  For the same `subform2` defined above
  ```
  => (notns-in-subformula subform2)
  (\"M\" \"cd\" \"mol\")
  ```
  and
  ```
  => (expts-in-subformula subform2)
  (\"2/3\" \"1\" \"-2\")
  ```
  The names of the notations are
  ```
  => (names-in-subformula subform2)
  (\"mass\" \"luminous intensity\" \"amount of substance\")
  ```
  #### Filter out recurring notation of component/s in next sub-formula w.r.t reference sub-formula
  For `(def lsubform '(\"[M^(0)*L^(1)*T^(-2)]\" \"[A^(1)*T^(2)]\" \"[cd]^(0)*[mol]^(-2)]\"))`
  consider `\"[M^(0)*L^(1)*T^(-2)]\"` to be the reference sub-formula (because it has the
  largest number of unique notations). Component in the next sub-formula `\"[A^(1)*T^(2)]\"`
  with common notation is given in formula representation as
  ```
  => (next-subformula-components-with-common-notation (first lsubform) (second lsubform))
  \"[T^(2)]\"
  ```
  and if `\"[cd]^(0)*[mol]^(-2)]\"` is the next component
  ```
  => (next-subformula-components-with-common-notation (first lsubform) (last lsubform))
  nil
  ```
  and with itself
  ```
  => (next-subformula-components-with-common-notation (first lsubform) (first lsubform))
  nil
  ```
  "
  (:require [diman.utilities :refer [remove-brackets include-brackets]]
            [diman.dimensions :refer [grab-name]])
  )

;; ============================================================================
;; Function for filtering out variable/parameter and its exponents in a term of
;; the expression.
;; Note:
;; - term will result in sub-formula as part of formula of the expression.
;; - sub-formula is also an expression so use of this function will filter out
;; notations and its exponents.
;; ============================================================================
(defn- pull-varpar [x]
  "List all the variable/parameters without operators (after removing spaces).
  This is compatible with single or multi character variable/parameter names."
  (re-seq #"[A-Za-z]+" (clojure.string/replace x " " ""))
  )

(defn- symb-indx [x]
  "Returns index of first exponent symbol (^) in expression (numerator or denominator)."
  (clojure.string/index-of x "^")
  )

(defn- next-indx [x]
  "Returns index of the first character after the exponent value, i.e, after )*.
  For example `\"m^(-3)*t\"`, `(next-indx x)` returns 7 for `t`.
  "
  (let [indx_multiply  (clojure.string/index-of x "*")
        indx_backparen (clojure.string/index-of x ")")]
    (if (some? indx_multiply)
      (inc indx_multiply)
      (if (some? indx_backparen)
        (inc indx_backparen)
        nil
        )
      ))
  )

(defn- make-expt
  "Returns exponent value as strings that is inside two indices of the expression."
  ([x indx_start indx_end] (make-expt x indx_start indx_end ()))
  ([x indx_start indx_end made]
   (if (< indx_end indx_start)
     (clojure.string/replace made #"[\(\s\\)]+" "")
     (recur x indx_start (- indx_end 1) (cons (nth x indx_end) made))
     ))
  )

(defn- remain-expr
  "Returns expression from next-index onwards.
  It strips backslash and first and last parenthesis."
  ([x indx_next] (remain-expr x indx_next 1 (clojure.string/replace
                                              (nthrest x indx_next) #"[\s\\]" "")))
  ([x indx_next paren_no expr]
   (if (= paren_no 1)
     (recur x indx_next (+ paren_no 1) (clojure.string/replace expr #"^[\(]" "" ))
     (if (= paren_no 2)
       (recur x indx_next (+ paren_no 1) (clojure.string/replace expr #"[\)]$" ""))
       expr)
     ))
  )

(defn- rest-expr [x indx_next]
  "Returns expression from next-index onwards and nil if there's nothing."
  (if (some? indx_next)
    (remain-expr x indx_next)
    nil)
  )

(defn- pull-expt [x indx_sym]
  "Returns exponent value without symbol (^) and parenthesis (if it has)."
  (let [indx_frontparen (clojure.string/index-of x "(")
        indx_backparen  (clojure.string/index-of x ")")
        indx_multiply   (clojure.string/index-of x "*")]
    (if (and (some? indx_frontparen) (= indx_frontparen (inc indx_sym)))
      (make-expt x (inc indx_frontparen) (dec indx_backparen))
      (if (and (some? indx_multiply) (> indx_multiply (inc indx_sym)))
        (make-expt x (inc indx_sym) (dec indx_multiply))
        (make-expt x (inc indx_sym) (dec (count x)))
        )
      ))
  )

(defn- filter-src-expr [x]
  "Return expression after the first * if it is in front of ^; if the source/original
  expression is of the form \"0.5*a^(1)*t^(2)\" this function will return \"a^(1)*t^(2)\",
  but for \"a^(1)*t^(2)\" this function will return unchanged."
  (let [[indx_multiply indx_symb]
        [(clojure.string/index-of x "*") (symb-indx x)]]
    (if (and (some? indx_multiply) (< indx_multiply indx_symb))
      (clojure.string/replace (nthrest x (inc indx_multiply)) #"^[\(]|[\s\\]|[\)]$" "")
      x)
    ))

(defn list-varpar-expt
  "Returns list with two nested lists for a given expression;
  first is list of variables/parameters
  last is the list of its corresponding exponent values.
  "
  ([x] (list-varpar-expt (filter-src-expr x) (pull-varpar x) ()))
  ([x lst_varpars lst_expts]
   (if (empty? x)
     [lst_varpars (reverse lst_expts)]
     (recur (rest-expr x (next-indx x)) lst_varpars
            (cons (pull-expt x (symb-indx x)) lst_expts))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;; Function(s) for filtering out the notations/exponents/notation names in a
;; sub-formula.
;; ============================================================================
(defn notns-in-subformula [subform]
  "Returns list of notations in the formula component (same order as the formula)."
  (first (list-varpar-expt (remove-brackets subform)))
  )

(defn expts-in-subformula [subform]
  "Returns list of exponents in the formula component (same order as the formula)."
  (last (list-varpar-expt (remove-brackets subform)))
  )

(defn names-in-subformula
  "Returns list of names in the formula component (same order as the formula)."
  ([subform] (names-in-subformula subform (notns-in-subformula subform) ""))
  ([subform lst_notns lst_names]
   (if (empty? lst_notns)
     lst_names
     (recur subform (drop-last lst_notns)
            (cons (grab-name (include-brackets (last lst_notns))) lst_names))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;; Function for filtering out the components in the next sub-formula having
;; same notation as its reference sub-formula.
;; NOTE:
;; - it's return form is more formula-like than list of components.
;; - it returns components linked by * and surrounded by brackets.
;; - however it is not sub-formula of the expression under analysis
;; - to avoid confusion it will not be referred to as sub-formula.
;; ============================================================================
(defn- compare-notations [ref_notn next_notn next_expt]
  "Returns <notn>^<expt> without brackets (unlike `tie-expt-notation`) if <notn> in
  the next sub-formula equals the reference sub-formula."
  (if (= ref_notn next_notn)
    ;(remove-brackets (tie-expt-notation next_notn next_expt))
    (clojure.string/join "^" [next_notn (clojure.string/join ["(" next_expt ")"])])
    nil))

(defn- link-matched-components [ref_subform matched_components]
  "Returns connected <notn>^<expt> pairs by * if the resultant connection is either
  not the same as the reference or not empty."
  (let [linked
        (include-brackets
           (clojure.string/replace (clojure.string/join "*" matched_components)
                                   #"^[\*]" ""))]
    (if (or (= ref_subform linked) (= "[]" linked))
      nil
      linked)
    ))

(defn next-subformula-components-with-common-notation
  "Returns formula within the next component having the same notation as its reference."
  ([ref_subform next_subform]
   (next-subformula-components-with-common-notation ref_subform
                                                    (notns-in-subformula ref_subform)
                                                    (notns-in-subformula next_subform)
                                                    (expts-in-subformula next_subform) ""))
  ([ref_subform lst_ref_notns lst_next_notns lst_next_expts matched_component]
   (if (empty? lst_next_notns)
     (link-matched-components ref_subform matched_component)
     (recur ref_subform (drop-last lst_ref_notns) (drop-last lst_next_notns)
            (drop-last lst_next_expts)
            (cons (compare-notations (last lst_ref_notns)
                                     (last lst_next_notns)
                                     (last lst_next_expts))
                  matched_component))
     ))
  )
;; =====================================x======================================

