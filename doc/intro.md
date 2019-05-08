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
### Import required functions
```
(require '[diman.formula :refer [formula-term formula-eqn-side]])
(require '[diman.analyze :refer [dimnames]])
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
## Analyze
### Represent sub-formula of an expression term as dimension names
```
=> (dimnames (formula-term varpars (:term4 rhs)))
"length^(1)"
```
### Represent dimensional formula of an equation side as dimension names
```
=> (dimnames (formula-eqn-side varpars rhs))
"length^(1) + time^(-2)*length^(2) + time^(1) + length^(1)"
```
## Consistency check
Let us consider the equation
```
(def lhs "x^(1)")
(def rhs {:term1 "x^(1)",
          :term2 "v^(1)*t^(1)",
          :term3 "0.5*a^(1)*t^(2)"})
(def eqn {:lhs lhs, :rhs rhs})
```
whose variables/parameters are defined as
```
(def varpars [{:symbol "x", :dimension "length"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "t", :dimension "time"}
              {:symbol "a", :dimension "acceleration"}])
```
If the correctness of an equation is in doubt checking for dimensional consistency is a useful preliminary step.

To perform consistency check based on dimensional analysis you first import the function `consistent?` as `(require '[diman.analyze :refer [consistent?]])`.
Then
```
=> (consistent? varpars eqn)
true
```

However dimensionally consistent equation **does not guarantee** correct equation.
