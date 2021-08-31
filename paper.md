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

`diman` (**dim**ensional **an**alysis) is a Clojure based scientific software with the ability to: create dimensional formula, create dimensional equation, check dimensional homogeneity (consistency), and derive dimensionless products.

`diman` provides functions for each step of the analytic process for checking dimensional homogeneity or deriving dimensionless products; they make the computationally repetitive operations hidden. Users can write compound functions that performs a desired process. Thus, not only is the computational labor saved, but also introspection of the analysis is possible; the analyst is able to go through the steps of dimensional analysis.

# Statement of need

Explaining the mechanism of a phenomenon is often the goal of experiments. As most mechanistic description is expressible in terms of some measurable quantity, its value is a function of other measurable quantities, the function represents the relationship among the quantities, which provides a mechanistic explanation. For example, $F = ma = mdv/dt$ where the measurable value of force $F$ is a function of the measurable quantities: mass, $m$; velocity, $v$; and time, $t$.

Some or all the independent variables of the parent (first or original) function have dimensions. Since most of the functions are unknown, and hence conceptual, the researcher deals with many candidates for independent variable, whose considerations are based on experimental results. Although the mathematical expression of the function is unknown, knowledge of the relationship among the measurable quantities is profitable not only in putting together the series of experimental results to explain the mechanism, but also testing the hypothesis presented by the function.

If possible, it is beneficial to use the transformed parent function, where all the independent variables are dimensionless. Dimensionless products are scalars that contains information of the dimensional quantities that it is a product of. Not only are points in a graph of dimensionless products experimentally determinable, but also dimensionless graphs can provide more information than dimensional graphs [@Langhaar:1951]. Reducing the number of independent variables to a smaller collection of dimensionless products can assist in understanding the mechanism of the phenomenon [@Langhaar:1951; @Sharma:2021].

Numerous softwares have been developed to deal with dimensions in some shape or form [@Preussner:2018; @Sharma:2021]. Most incorporate the ability to tag quantities with units, however, few are capable of doing consistency checks and fewer still deal with dimensionless products let alone, deriving dimensionless products.

`diman` is designed with an emphasis on **analysis**; the  application of the algebraic theory of dimensionally homogeneous functions [@Langhaar:1951]. It can check for dimensional homogeneity of a given equation and can derive the complete set of dimensionless products of a given equation.

# Design and implementation

Based on the International System of Units `diman` uses the seven base (or elementary) dimensions: [M], [L], [T], [A], [K], [mol] and [cd] for the quantities mass, length, time, electric current, thermodynamic temperature, amount of substance and luminous intensity respectively [@BIPM:2020]. They are defined in `base_dimensions`. Furthermore, some well-known dimensions derived from the `base_dimensions` are defined in `standard_formula`; a dimensional formula for respective quantity is its dimension.

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
Supposing the independent variables of the parent function $f$ are not already defined in `standard_formula`, inject the dimensions of the independent variables into the `standard_formula` for the present read–eval–print loop session by
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
Therefore, function $f$ is transformed into some function $f_1$ whose independent variables are the dimensionless products; $\pi_0$, $\pi_1$, $\pi_2$ and $\pi_3$&mdash;$\pi$ is the conventional notation for any dimensionless product and is not a reference to the number 3.14159... Thus, the number of variables is reduced from 7 to 4.

# Conclusion

`diman` is a Clojure library with no other dependencies. It has its own linear algebra submodule which provides all the necessary operations. Internally, the numerical data type is Clojure's *ratio*; a ratio between integers rather than floats [@Clojure:2020]. This avoids truncation and rounding errors. Since dimensional analysis do not often involve very large matrices, the hit on computational performance due to using the *ratio* number type is practically insignificant. `diman` supplies all the necessary functions for dimensional homogeneity operations and the derivation of dimensionless products; thus making the analysis steps transparent.


# Acknowledgements

The project received no funding.

# References