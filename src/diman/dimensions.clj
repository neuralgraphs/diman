(ns diman.dimensions
  "Contains definitions and their getters.
  These are:

  - `base-dimensions`
  - `standard-formula`
  - `grab-notation` and `grab-name` from `base-dimensions`
  - `grab-sformula` from `standard-formula`
  - `notation?`
  - `matched-notation-sformula`

  ## How to use
  ### Loading
  ```
  (:require [diman.dimensions :refer [base_dimensions standard_formula
                                      grab-notation grab-name grab-sformula
                                      notation? matched-notation-sformula]])
  ```
  ### Examples
  #### View the seven fundamental dimensions
  `(pprint base_dimensions)`

  #### Get notation for dimension name \"mass\"
  ```
  => (grab-notation \"mass\")
  \"[M]\"
  ```

  #### Get name for notation \"[M]\"
  ```
  => (grab-name \"[M]\")
  \"mass\"
  ```

  #### View available standard formula
  `(pprint standard_formula)`

  #### Get formula of a quantity in the standard formula
  ```
  => (grab-sformula \"acceleration\")
  \"[M^0 L^1 T^(-2)]\"
  ```

  #### Get matching notation/standard formula of a defined symbol
  Let us define symbol `x`, `v`, `t` and `a` as
  ```
  (def varpars [{:symbol \"x\", :dimension \"length\"}
                {:symbol \"v\", :dimension \"velocity\"}
                {:symbol \"t\", :dimension \"time\"}
                {:symbol \"a\", :dimension \"acceleration\"}])
  ```
  Then based on the dimensions defined for respective symbol use
  ```
  => (matched-notation-sformula varpars \"x\")
  \"[L]\"
  => (matched-notation-sformula varpars \"a\")
  \"[M^(0)*L^(1)*T^(-2)]
  ```
  NOTE:

  * definition of any __single letter__ symbol must be a value to key `:symbol`
  * and must also have the key `:dimension` whose value must be a string corresponding to either
    - one of the seven `:name` values, i.e, one of the seven fundamental dimensions
    - one of the `quantity` values, i.e, one of the standard formulae

  "
  )

(def base_dimensions
  "The Seven Fundamental/Base Dimensions.
  NOTE: This is fixed. See [International Metrology](https://www.bipm.org/en/measurement-units/)
  "
  [{:name "mass"                      :notation "[M]"}
   {:name "length"                    :notation "[L]"}
   {:name "time"                      :notation "[T]"}
   {:name "electric current"          :notation "[A]"}
   {:name "thermodynamic temperature" :notation "[K]"}
   {:name "luminous intensity"        :notation "[cd]"}
   {:name "amount of substance"       :notation "[mol]"}
   ])

(def standard_formula
  "Dimensional Formula for standard physical quantity.
  NOTE: This is subject to expansion. See [International Metrology](https://www.bipm.org/en/measurement-units/)
  "
  [{:quantity "volume"       :sformula "[L^(3)]"}
   {:quantity "frequency"    :sformula "[T^(-1)]"}          ; hertz, Hz
   {:quantity "velocity"     :sformula "[L^(1)*T^(-1)]"}
   {:quantity "acceleration" :sformula "[L^(1)*T^(-2)]"}
   {:quantity "force"        :sformula "[M^(1)*L^(1)*T^(-2)]"} ; newton, N
   {:quantity "mass density" :sformula "[M^(1)*L^(-3)]"}
   ;; [Derived units in SI w/ special names/symbol](https://www.bipm.org/en/publications/si-brochure/table3.html)
   {:quantity "energy"             :sformula "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "work"               :sformula "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "amount of heat"     :sformula "[M^(1)*L^(2)*T^(-2)]"} ; joule, J, Nm
   {:quantity "pressure"           :sformula "[M^(1)*L^(-1)*T^(-2)]"} ; pascal, P, N/m2
   {:quantity "stress"             :sformula "[M^(1)*L^(-1)*T^(-2)]"} ; pascal, P, N/m2
   {:quantity "catalytic activity" :sformula "[mol^(1)*T^(-1)]"} ; katal, kat
   ;; [Electrical quantities](https://www.bipm.org/metrology/electricity-magnetism/units.html)
   {:quantity "charge"                :sformula "[A^(1)*T^(1)]"} ; charge or amount of electricity; coulomb, C
   {:quantity "capacitance"           :sformula "[M^(-1)*L^(-2)*T^(4)*A^(2)]"} ; farad, F
   {:quantity "inductance"            :sformula "[M^(1)*L^(2)*T^(-2)*A^(-2)]"} ; henry, H
   {:quantity "resistance"            :sformula "[M^(1)*L^(2)*T^(-3)*A^(-2)]"} ; ohm, omega
   {:quantity "conductance"           :sformula "[M^(-1)*L^(-2)*T^(3)*A^(2)]"} ; siemens, S
   {:quantity "magnetic flux density" :sformula "[M^(1)*T^(-2)*A^(-1)]"} ; tesla, T
   {:quantity "electromotive force"   :sformula "[M^(1)*L^(2)*T^(-3)*A^(-1)]"} ; volt, V
   {:quantity "power"                 :sformula "[M^(1)*L^(2)*T^(-3)]"} ; power or radiant flux, watt, W
   {:quantity "magnetic flux"         :sformula "[M^(1)*L^(2)*T^(-2)*A^(-1)]"} ; weber, Wb
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
  (if (= dim (:name base))
    (:notation base)
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
  (if (= ntn (:notation base))
    (:name base)
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
    (:sformula std)
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
     (grab-notation (:dimension (last varpar_def)))
     (recur (drop-last varpar_def) symb "")
     ))
  )

(defn- matched-sformula
  "Returns standard formula of the matching quantity of the given symbol (symb)
  which is defined (varpar_def); nil otherwise."
  ([varpar_def symb] (matched-sformula varpar_def symb ""))
  ([varpar_def symb ans]
   (if (= symb (:symbol (last varpar_def)))
     (grab-sformula (:dimension (last varpar_def)))
     (recur (drop-last varpar_def) symb "")
     ))
  )

(defn matched-notation-sformula [varpar_def symb]
  "Returns notation or standard formula for the given symbol (symb)
  which is defined (varpar_def); nil otherwise."
  (if (nil? (matched-notation varpar_def symb))
    (matched-sformula varpar_def symb)
    (matched-notation varpar_def symb))
  )
;; =====================================x======================================