---
title: 'diman: A Clojure package for dimensional analysis'
tags:
  - Clojure
  - neuroscience
  - modeling
  - simulation
  - quantitative analysis
  - scientific computing
authors:
  - name: Lungsi Sharma^[co-first author] # note this makes a footnote saying 'co-first author'
    orcid: 0000-0002-1607-0164
    affiliation: 1
affiliations:
 - name: Ronin Institute
   index: 1

date: 30 July 2021
bibliography: paper.bib
---

# Summary

`diman` (**dim**ensional **an**alysis) is a Clojure based scientific software. It has the following features: create a dimensional formula, create dimensional equation, implement principle of dimensional homogeneity, i.e., perform consistency checks and derive dimensionless products.

`diman` provide functions for each step of the analytic process of checking for dimensional homogeneity or deriving dimensionless products. Users can write their custom function that performs the desired process by compounding the functions provided by `diman`. The functions make the computationally repetitive operations hidden. This saves the analyst from laboring in computational tasks while still able to go through the steps of dimensional analysis thereby affording the user to reflect on the analysis.

# Statement of need

In general, experiments in science deals with explaining the mechanism of a phenomenon. Description of the mechanism is usually represented by some measurable quantity that addresses the research question such that the value of the quantity is a function of other measurable quantities. Most experimental results suggests many candidates for independent variables of the function. In another scenario, a hypothesis built on experimental results may include a number of independent variables which would be the target for new experiments to test the hypothesis. The independent variables are often dimensional quantities.

Dimensionless products are scalars that carries the information of the dimensional quantities that it is a product of. Graphs of dimensionless products often provide more information than graphs having dimensions. Also, points in the dimensionless graph can be determined experimentally. Reducing the number of independent variables to a smaller collection of dimensionless products can assist in understanding the mechanism of the phenomenon [@Langhaar:1951; @Sharma:2021].

Numerous software have been developed that deal with dimensions in some shape or form [@Preussner:2018; @Sharma:2021]. Most software incorporate the ability to tag quantities with units. However, few are capable of doing consistency checking and fewer still deal with dimensionless products let alone, deriving dimensionless products.

`diman` is designed with an emphasis on **analysis** &mdash; the  application of the algebraic theory of dimensionally homogeneous functions [@Langhaar:1951]. It can check for dimensional homogeneity of a given equation and can derive the complete set of dimensionless products of a given equation.

# Design and implementation

Based on the International System of Units `diman` uses the seven base/elementary dimensions: [M], [L], [T], [A], [K], [mol] and [cd] for the quantities mass, length, time, electric current, thermodynamic temperature, amount of substance and luminous intensity respectively [@BIPM:2020]. They are defined in `base_dimensions`. Furthermore, some well-known dimensions derived from the `base_dimensions` are defined in `standard_formula` &mdash; a dimensional formula for respective quantity is its dimension.

## Consistency checking

This is done by the predicate `consistent?`. However, there are some preliminary steps before invoking the predicate. Consider the given function $E = \frac{1}{2}mv^2$

We define the variables
```
=> (def variables [{:symbol "E", :quantity "energy"}
                   {:symbol "m", :quantity "mass"}
                   {:symbol "v", :quantity "velocity"}])
```
then the equation
```
=> (def equation {:lhs "E^(1)", :rhs "0.5*m^(1)*v^(2)"})
```
Finally, the predicate `consistent?` is used to check if the equation is dimensionally homogenous.
```
=> (consistent? variables equation)
true
```

## Derivation of set of dimensionless products

Imagine that the study of a system results in a hypothesis such that some measurable dimensionless product is a homogeneous function $f$ of the independent variables $P$, $Q$, $R$, $S$, $T$, $U$ and $V$. Also, assume that the independent variables have dimensions such that
```
=> (def dimensional_formulae_of_all_independent_variables
        [{:quantity "term-p", :dimension "[M^(2)*L^(1)]"}
         {:quantity "term-q", :dimension "[M^(-1)*T^(1)]"}
         {:quantity "term-r", :dimension "[M^(3)*L^(-1)]"}
         {:quantity "term-s", :dimension "[T^(3)]"}
         {:quantity "term-t", :dimension "[L^(2)*T^(1)]"}
         {:quantity "term-u", :dimension "[M^(-2)*L^(1)*T^(-1)]"}
         {:quantity "term-v", :dimension "[M^(1)*L^(2)*T^(2)]"}]) 
```
Assuming they are not already defined in `standard_formula`, inject the dimensions of the independent variables into the `standard_formula` for the present read–eval–print loop session by
```
=> (update-sformula dimensional_formulae_of_all_independent_variables)
```
Thus, `diman` now contains dimensions of the independent variables of $f$. Hence, the independent variables can be defined as
```
=> (def independent_variables
        [{:symbol "P", :quantity "term-p"}
         {:symbol "Q", :quantity "term-q"}
         {:symbol "R", :quantity "term-r"}
         {:symbol "S", :quantity "term-s"}
         {:symbol "T", :quantity "term-t"}
         {:symbol "U", :quantity "term-u"}
         {:symbol "V", :quantity "term-v"}]) 
```
The theory of dimensionless products [@Ngwua:2020] tells us that the derivation of dimensionless products can be broken down into four steps: generate the dimensional matrix, solve the homogeneous equation, determine the solution matrix and get the set of dimensionless products. Compounding the first three steps into one code block we get,
```
=> (def solution_matrix
        (get-solved-matrix
            (solve (get-augmented-matrix
						(generate-dimmat independent_variables)))))
```
This is the solution matrix for a complete set of dimensionless products.
```
=> (view-matrix solution_matrix)
[1 0 0 0 -11N 5N 8N]
[0 1 0 0 9N -4N -7N]
[0 0 1 0 -9N 5N 7N]
[0 0 0 1 15N -6N -12N]
Size -> 4 x 7
```
The set of dimensionless products can be obtained from the solution matrix by using the function `get-dimensionless-products`. Thus
```
=> (println (get-dimensionless-products solution_matrix independent_variables))
  [{:symbol "pi0", :expression "P^(1)*T^(-11)*U^(5)*V^(8)"}
  {:symbol "pi1", :expression "Q^(1)*T^(9)*U^(-4)*V^(-7)"}
  {:symbol "pi2", :expression "R^(1)*T^(-9)*U^(5)*V^(7)"}
  {:symbol "pi3", :expression "S^(1)*T^(15)*U^(-6)*V^(-12)"}]
```
or
$$
\pi_0 = PT^{-11}U^5V^8, \pi_1 = QT^9U^{-4}V^{-7}, \pi_2 = RT^{-9}U^5V^7, \pi_3 = ST^{15}U^{-6}V^{-12}
$$
Therefore, the function $f$ is transformed into some function $f_1$ whose independent variables are the dimensionless products; $\pi_0$, $\pi_1$, $\pi_2$ and $\pi_3$ &mdash; $\pi$ is the conventional notation for any dimensionless product and is not a reference to the number 3.14159... Thus, the number of variables is reduced from 7 to 4.

# Conclusion

`diman` is a Clojure library with no other dependencies. It has its own linear algebra submodule which provides all the necessary operations required by `diman`. Internally, the numerical data type is Clojure's *ratio*, a ratio between integers rather than floats [@Clojure:2020]. This avoids truncation and rounding errors. Since, dimensional analysis do not often involve very large matrices the computational performance hit due to the *ratio* number type is practically insignificant. `diman` supplies all the necessary functions for dimensional homogeneity operations and the derivation of dimensionless products, thus making the analysis steps transparent.


# Acknowledgements

The project received no funding.

# References