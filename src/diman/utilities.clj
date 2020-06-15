(ns diman.utilities
  "Contains functions.
  These are:

  - `remove-brackets`
  - `remove-parentheses`
  - `include-brackets`
  - `include-parentheses`
  - `key-in-expr?`

  ## How to use
  ### Loading
  ```
  (:require [diman.utilities :refer [remove-brackets remove-parentheses
                                     include-brackets include-parentheses]
                                     )
  ```
  ### Examples
  Self-explanatory
  "
  )

(defn remove-brackets [notn_sform]
  "Returns contents inside []."
  (clojure.string/replace notn_sform #"[\[\]]+" "")         ;; {}-braces
  )

(defn remove-parentheses [notn_sform]
  "Returns contents inside ()."
  (clojure.string/replace notn_sform #"[\(\)]+" "")
  )

(defn include-brackets [formula]
  "Returns contents surrounded by []."
  (clojure.string/join ["[" formula "]"])
  )

(defn include-parentheses [expt]
  "Returns contents surrounded by ()."
  (clojure.string/join ["(" expt ")"])
  )

;(defn key-in-expr? [expr] (if (nil? (:term1 expr)) false true))
(defn key-in-expr? [expr]
  "Checks if the expression (usually side of an equation) has keywords."
  (if (= (type expr) clojure.lang.PersistentArrayMap)
    true
    false)
  )