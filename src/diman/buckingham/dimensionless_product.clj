(ns diman.buckingham.dimensionless-product
  (:require [diman [attach :refer [tie-notnlist-exptlist]]
             [utilities :refer [remove-brackets]]
             [filter :refer [remove-zero-powers]]]
            ))

(defn- get-exponents-for-pi
  ([a_row_of_solved_matrix] (get-exponents-for-pi a_row_of_solved_matrix []))
  ([a_row ans]
   (if (empty? a_row)
     ans
     (recur (drop-last a_row) (cons (str (last a_row)) ans))
     )))

(defn- get-variables-for-pi
  ([varpars] (get-variables-for-pi varpars []))
  ([varpars ans]
   (if (empty? varpars)
     ans
     (recur (drop-last varpars) (cons (:symbol (last varpars)) ans))
     )))

(defn get-dimensionless-products
  ([solved_matrix varpars]
   (get-dimensionless-products solved_matrix varpars (get-variables-for-pi varpars)
                               (count solved_matrix) 0 []))
  ([solved_matrix varpars variable_symbol_list m i ans]
   (if (= i m)
     ans
     (let [pi_i (clojure.string/join "" ["pi" i])
           expt_list (get-exponents-for-pi (get solved_matrix i))
           formula_pi_i (tie-notnlist-exptlist [variable_symbol_list expt_list])
           cleaned_formula (remove-brackets (remove-zero-powers formula_pi_i))]
       (recur solved_matrix varpars variable_symbol_list m (+ i 1)
              (conj ans {:symbol pi_i :expression cleaned_formula}))
       ))
    ))

(defn get-pi-expression
  ([all_pi desired_pi] (get-pi-expression all_pi desired_pi []))
  ([all_pi desired_pi ans]
   (if (or (empty? all_pi) (not (empty? ans)))
     ans
     (recur (drop-last all_pi) desired_pi
            ((fn [x] (if (= desired_pi (:symbol x)) (:expression x) [])) (last all_pi))
            ))
    ))