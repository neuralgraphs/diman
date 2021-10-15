(ns diman.buckingham.dimensionless-product
  "Contains function

   - `get-dimensionless-products`
   - `get-pi-expression`

  Given
  ```
  (def varpars [ {:symbol \"x\", :quantity \"length\"}
                 {:symbol \"v\", :quantity \"velocity\"}
                 {:symbol \"t\", :quantity \"time\"}
                 {:symbol \"a\", :quantity \"acceleration\"} ])
  (def soln_matrix [ [1 -11 5 8]
                     [0 9 -4 -7] ])
  ```
  to get the [set of dimensionless products](https://neuralgraphs.com/lectures/diman/lectp8.html#p4_thispage)
  ```
  => (get-dimensionless-products soln_matrix varpars)
  [{:symbol \"pi0\", :expression \"x^(1)*v^(-11)*t^(5)*a^(8)\"} {:symbol \"pi1\", :expression \"v^(9)*t^(-4)*a^(-7)\"}]
  ```
  and to extract a particular dimensionless product from the set
  ```
  => (get-pi-expression (get-dimensionless-products soln_matrix varpars) \"pi0\")
  \"x^(1)*v^(-11)*t^(5)*a^(8)\"
  ```
  "
  (:require [diman [attach :refer [tie-notnlist-exptlist]]
             [utilities :refer [remove-brackets]]
             [filter :refer [remove-zero-powers]]]
            ))

(defn- get-exponents-for-pi
  "Given a row of the solution matrix, this function returns a list such that each element is the
  string of the corresponding element in the row vector.
  "
  ([a_row_of_solution_matrix] (get-exponents-for-pi a_row_of_solution_matrix []))
  ([a_row ans]
   (if (empty? a_row)
     ans
     (recur (drop-last a_row) (cons (str (last a_row)) ans))
     )))

(defn- get-variables-for-pi
  "Given the definition of variables/parameters of a system, this function returns a list such that
  each element is symbol in the definition.
  "
  ([varpars] (get-variables-for-pi varpars []))
  ([varpars ans]
   (if (empty? varpars)
     ans
     (recur (drop-last varpars) (cons (:symbol (last varpars)) ans))
     )))

(defn get-dimensionless-products
  "Given a solution matrix and its definition of variables/parameters of a system, this function returns
  a vector such that each element is a map with keys, `:symbol` and `:expression`. The elements of this
  vector represents the elements of the set of dimensionless products.
  "
  ([solution_matrix varpars]
   (get-dimensionless-products solution_matrix varpars (get-variables-for-pi varpars)
                               (count solution_matrix) 0 []))
  ([solution_matrix varpars variable_symbol_list m i ans]
   (if (= i m)
     ans
     (let [pi_i (clojure.string/join "" ["pi" i])
           expt_list (get-exponents-for-pi (get solution_matrix i))
           formula_pi_i (tie-notnlist-exptlist [variable_symbol_list expt_list])
           cleaned_formula (remove-brackets (remove-zero-powers formula_pi_i))]
       (recur solution_matrix varpars variable_symbol_list m (+ i 1)
              (conj ans {:symbol pi_i :expression cleaned_formula}))
       ))
    ))

(defn get-pi-expression
  "Given the set of dimensionless products (a vector of maps with keys, `:symbol` and `:expression`) and
  the symbol of a dimensionless product (as `\"piX\"` where `X` is an integer from 0, 1, 2, ...), this
  function returns the expression of the desired dimensionless product.
  "
  ([all_pi desired_pi] (get-pi-expression all_pi desired_pi []))
  ([all_pi desired_pi ans]
   (if (or (empty? all_pi) (not (empty? ans)))
     ans
     (recur (drop-last all_pi) desired_pi
            ((fn [x] (if (= desired_pi (:symbol x)) (:expression x) [])) (last all_pi))
            ))
    ))