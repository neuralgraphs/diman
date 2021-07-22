(ns diman.dimensions
  "Contains definitions and their getters.
  These are:

  - `base_dimensions`
  - `standard_formula`
  - `grab-notation` and `grab-name` from `base-dimensions`
  - `grab-sformula` from `standard-formula`
  - `notation?`
  - `matched-notation-sformula`
  - `update-sformula`

  ## How to use
  ### Loading
  ```
  (require '[diman.dimensions :refer [base_dimensions standard_formula
                                      grab-notation grab-name grab-sformula
                                      notation? matched-notation-sformula]])
  ```
  ### Examples
  #### View the seven fundamental dimensions
  `(pprint bas e_dimensions)`

  #### Get notation for the base dimension name \"mass\"
  ```
  => (grab-notation \"mass\")
  \"[M]\"
  ```

  #### Get base dimension name for notation \"[M]\"
  ```
  => (grab-name \"[M]\")
  \"mass\"
  ```

  #### View available standard formula
  `(pprint standard_formula)`

  #### Get formula of a quantity in the standard formula
  ```
  => (grab-sformula \"acceleration\")
  \"[L^(1)*T^(-2)]\"
  ```
  Notice that a formula in `standard_formula` is a dimensional formula and hence as a consequence a dimension.

  #### Get matching notation/standard formula of a defined symbol
  Let us define symbol *x*, *v*, *t* and *a* representing a combination of variables and parameters as
  ```
  (def varpars [{:symbol \"x\", :quantity \"length\"}
                {:symbol \"v\", :quantity \"velocity\"}
                {:symbol \"t\", :quantity \"time\"}
                {:symbol \"a\", :quantity \"acceleration\"}])
  ```
  Then based on the dimensions defined for respective symbol use
  ```
  => (matched-notation-sformula varpars \"x\")
  \"[L]\"
  => (matched-notation-sformula varpars \"a\")
  \"[L^(1)*T^(-2)]\"
  ```
  NOTE:

  * definition of any __single letter__ symbol must be a value to key `:symbol`
  * and must also have the key `:dimension` whose value must be a string corresponding to either
    - one of the seven `:name` values, i.e, one of the seven fundamental dimensions
    - one of the `quantity` values, i.e, one of the standard formulae

  "
  (:require [clojure.pprint :refer [pprint]])        ; only necessary for lein test else ClassNotFoundException error due to clojure.set
  )

(def base_dimensions
  "The Seven Fundamental/Base Dimensions.
  NOTE: This is fixed. See [International Metrology](https://www.bipm.org/en/measurement-units/)
  "
  [{:quantity "mass"                      :dimension "[M]"}
   {:quantity "length"                    :dimension "[L]"}
   {:quantity "time"                      :dimension "[T]"}
   {:quantity "electric current"          :dimension "[A]"}
   {:quantity "thermodynamic temperature" :dimension "[K]"}
   {:quantity "luminous intensity"        :dimension "[cd]"}
   {:quantity "amount of substance"       :dimension "[mol]"}
   ])

(def standard_formula
  "Dimensional Formula for standard physical quantity.
  NOTE: This is subject to expansion. See [International Metrology](https://www.bipm.org/en/measurement-units/)
  "
  [{:quantity "volume"       :dimension "[L^(3)]"}
   {:quantity "frequency"    :dimension "[T^(-1)]"}          ; hertz, Hz
   {:quantity "velocity"     :dimension "[L^(1)*T^(-1)]"}
   {:quantity "acceleration" :dimension "[L^(1)*T^(-2)]"}
   {:quantity "force"        :dimension "[M^(1)*L^(1)*T^(-2)]"} ; newton, N
   {:quantity "mass density" :dimension "[M^(1)*L^(-3)]"}
   ;; [Derived units in SI w/ special names/symbol](https://www.bipm.org/en/publications/si-brochure/table3.html)
   {:quantity "energy"             :dimension "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "work"               :dimension "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "amount of heat"     :dimension "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "pressure"           :dimension "[M^(1)*L^(-1)*T^(-2)]"} ; pascal, P, N/m2
   {:quantity "stress"             :dimension "[M^(1)*L^(-1)*T^(-2)]"} ; pascal, P, N/m2
   {:quantity "catalytic activity" :dimension "[mol^(1)*T^(-1)]"} ; katal, kat
   ;; [Electrical quantities](https://www.bipm.org/metrology/electricity-magnetism/units.html)
   {:quantity "charge"                :dimension "[A^(1)*T^(1)]"} ; charge or amount of electricity; coulomb, C
   {:quantity "capacitance"           :dimension "[M^(-1)*L^(-2)*T^(4)*A^(2)]"} ; farad, F
   {:quantity "inductance"            :dimension "[M^(1)*L^(2)*T^(-2)*A^(-2)]"} ; henry, H
   {:quantity "resistance"            :dimension "[M^(1)*L^(2)*T^(-3)*A^(-2)]"} ; ohm, omega
   {:quantity "conductance"           :dimension "[M^(-1)*L^(-2)*T^(3)*A^(2)]"} ; siemens, S
   {:quantity "magnetic flux density" :dimension "[M^(1)*T^(-2)*A^(-1)]"} ; tesla, T
   {:quantity "electromotive force"   :dimension "[M^(1)*L^(2)*T^(-3)*A^(-1)]"} ; volt, V
   {:quantity "power"                 :dimension "[M^(1)*L^(2)*T^(-3)]"} ; power or radiant flux, watt, W
   {:quantity "magnetic flux"         :dimension "[M^(1)*L^(2)*T^(-2)*A^(-1)]"} ; weber, Wb
   ])

(defn notation? [x]
  "Checks for notation."
  (if (nil?
        (re-matches
          #"[\[M\]]{3}|[\[L\]]{3}|[\[T\]]{3}|[\[A\]]{3}|[\[K\]]{3}|[\[cd\]]{4}|[\[mol\]]{5}"
          x))
    false
    true))

;; ============================================================================
;;    Function for grabbing notation given a base/fundamental dimension name.
;; ============================================================================
(defn- get-notation-base-dim [dim base]
  "Returns notation if dimension is a base dimension, else \"nil\"."
  (if (= dim (:quantity base))
    (:dimension base)
    nil)
  )

(defn grab-notation
  "Returns notation for any one of the 7-Base dimensions, else \"nil\"."
  ([name] (grab-notation name base_dimensions nil))
  ([name bdim dim]
   (if (or (empty? bdim) (some? dim))
     dim
     (recur name (drop-last bdim) (get-notation-base-dim name (last bdim)))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;;    Function for grabbing name given a base/fundamental dimension notation.
;; ============================================================================
(defn- get-name-base-dim [ntn base]
  "Returns name if notation is a base notation, else \"nil\"."
  (if (= ntn (:dimension base))
    (:quantity base)
    nil)
  )

(defn grab-name
  "Returns name for any one of the 7-Base notations, else \"nil\"."
  ([notation] (grab-name notation base_dimensions nil))
  ([notation bdim dim]
   (if (or (empty? bdim) (some? dim))
     dim
     (recur notation (drop-last bdim) (get-name-base-dim notation (last bdim)))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;;          Function for grabbing standard formula given a quantity.
;; ============================================================================
(defn- get-sformula [qnt std]
  "Returns formula if quantity is one of the standards, else \"nil\"."
  (if (= qnt (:quantity std))
    (:dimension std)
    nil)
  )

(defn grab-sformula
  "Returns formula for any one of the standard quantities (hence standard formula), else \"nil\"."
  ([quantity] (grab-sformula quantity standard_formula nil))
  ([quantity sform form]
   (if (or (empty? sform) (some? form))
     form
     (recur quantity (drop-last sform) (get-sformula quantity (last sform)))
     ))
  )
;; =====================================x======================================

;; ============================================================================
;;    Function matches a defined symbol to base notation or standard formula.
;; ============================================================================
(defn- matched-notation
  "Returns notation of the matching dimension of the given symbol (symb) which
  is defined (varpar_def); nil if it is not one of the seven base dimensions."
  ([varpar_def symb] (matched-notation varpar_def symb ""))
  ([varpar_def symb ans]
   (if (= symb (:symbol (last varpar_def)))
     (grab-notation (:quantity (last varpar_def)))
     (if (empty? varpar_def) nil
       (recur (drop-last varpar_def) symb ""))
     )))

(defn- matched-sformula
  "Returns standard formula of the matching quantity of the given symbol (symb)
  which is defined (varpar_def); nil otherwise."
  ([varpar_def symb] (matched-sformula varpar_def symb ""))
  ([varpar_def symb ans]
   (if (= symb (:symbol (last varpar_def)))
     (grab-sformula (:quantity (last varpar_def)))
     (if (empty? varpar_def) nil
       (recur (drop-last varpar_def) symb ""))
     )))

(defn matched-notation-sformula [varpar_def symb]
  "Returns notation or standard formula for the given symbol (symb)
  which is defined (varpar_def); nil otherwise."
  (if (nil? (matched-notation varpar_def symb))
    (matched-sformula varpar_def symb)
    (matched-notation varpar_def symb))
  )

(defn update-sformula
  ([x] (update-sformula x []))
  ([x y]
   (if (empty? x)
     (clojure.pprint/pprint standard_formula)
     (let [to_insert (conj standard_formula {:quantity (:quantity (last x))
                                             :dimension (:dimension (last x))})]
       (recur (drop-last x) (intern 'diman.dimensions 'standard_formula to_insert)))
     )))
;; =====================================x======================================