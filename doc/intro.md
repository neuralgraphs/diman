# Introduction to diman

TODO: write [great documentation](http://jacobian.org/writing/what-to-write/)

## Naming Convention
### Name: Notations
Make sure with or without brackets.
### Name: Standard formula
pprint `standard_formula`
### Name: Formula component
### Name: Sub-formula (or Sub-dimensional formula)

## Setting up
### Definitions setup
```
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
```
### Expressions and equation
```
(def lhs "x^(1)")
(def rhs {:term1 "x^(1)",
          :term2 "v^(2)",
          :term3 "t^(1)",
          :term4 "0.5*a^(1)*t^(2)"})
(def eqn {:lhs lhs, :rhs rhs})
```
## Getting dimensional formula
### Sub-formula of the dimensional formula for one side of the equation
That is dimensional formula for one of the terms. Notice that the sub-formula **IS** the dimensional formula for the expression if there is just one term.

Thus for the right hand side of the given  equation
```
=> rhs
{:term1 "x^(1)", :term2 "v^(2)", :term3 "t^(1)", :term4 "0.5*a^(1)*t^(2)"}
```
the dimensional formula for `:term4` expression in the above `rhs` is given by
```
=> (formula-term varpars (:term4 rhs))
"[T^(0)*M^(0)*L^(1)]"
```
### Dimensional formula for one side of the equation
Dimensional formula for the `rhs` expression is given by
```
=> (formula-eqn-side varpars rhs)
"[L^(1)]+[T^(-2)*M^(0)*L^(2)]+[T^(1)]+[T^(0)*M^(0)*L^(1)]"
```


(def lform '("[M^(0)*L^(1)*T^(-2)]" "[A^(1)*T^(2)]" "[cd]^(0)*[mol]^(-2)]"))

(require '[diman.dimensions :refer :all])
(require '[diman.utilities :refer :all])
(require '[diman.filter :refer :all])
(require '[diman.exponents :refer :all])
(require '[diman.attach :refer :all])
