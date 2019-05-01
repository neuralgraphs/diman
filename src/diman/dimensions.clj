(ns diman.dimensions
  "Contains definitions and their getters.
  These are:

  - `base-dimensions`
  - `standard-formula`
  - `grab-notation` and `grab-name` from `base-dimensions`
  - `grab-formula` from `standard-formula`
  - `notation?`

  ## How to use
  ### Loading
  ```
  (:require [diman.dimensions :refer [base_dimensions standard_formula
                                      grab-notation grab-name
                                      grab-formula notation?]])
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
  => (grab-formula \"acceleration\")
  \"[M^0 L^1 T^(-2)]\"
  ```
  "
  )

(def base_dimensions
  "The Seven Fundamental/Base Dimensions.

  NOTE: This is fixed.
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

  NOTE: This is subject to expansion.
  "
  [{:quantity "volume"       :formula "[M^(0)*L^(3)*T^(0)]"}
   {:quantity "velocity"     :formula "[M^(0)*L^(1)*T^(-1)]"}
   {:quantity "acceleration" :formula "[M^(0)*L^(1)*T^(-2)]"}
   {:quantity "force"        :formula "[M^(1)*L^(1)*T^(-2)]"}
   {:quantity "mass density" :formula "[M^(1)*L^(-3)*T^(0)]"}
   ])

(defn notation? [x]
  "Checks for notation."
  (if (nil? (re-matches #"[\[M\]\[L\]\[T\]\[A\]\[K\]\[cd\]\[mol\]]+" x))
    false
    true))

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
     )
    )
  )


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
     )
    )
  )

(defn- get-formula-std-form [qnt std]
  "Returns formula if quantity is one of the standards, else \"nil\"."
  (if (= qnt (:quantity std))
    (:formula std)
    nil)
  )

(defn grab-formula
  "Returns formula for any one of the standard quantities, else \"nil\"."
  ([quantity] (grab-formula quantity standard_formula nil))
  ([quantity sform form]
   (if (or (empty? sform) (some? form))
     form
     (recur quantity (drop-last sform) (get-formula-std-form quantity (last sform)))
     )
    )
  )