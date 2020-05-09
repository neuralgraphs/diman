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
(require '[diman.analyze :refer [dimnames consistent?]])
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
### Consistency of multiple equations
Let us consider the case below
$$
e = m^2 v^2
e = \frac{1}{2}m v^2
e = m a
e = \frac{3}{16}m v^2
e = \frac{1}{2}m v^2 + m a
$$
the question is, which of these equations are correct? To do this let us perform dimensional consistency check.

Thus

| Equation                     | Set-up                                           |
|:----------------------------:|:------------------------------------------------:|
| $e = m^2 v^2$                |`(def eqn1 {:lhs "e^(1)", :rhs "m^(2)*v^(2)"})`       |
| $e = \frac{1}{2}m v^2$       |`(def eqn2 {:lhs "e^(1)", :rhs "0.5*m^(1)*v^(2)"})`    |
| $e = m a$                    |`(def eqn3 {:lhs "e^(1)", :rhs "m^(1)*a^(1)"})`        |
| $e = \frac{3}{16}m v^2$      |`(def eqn4 {:lhs "e^(1)", :rhs "0.1875*m^(1)*v^(2)"})`|
| $e = \frac{1}{2}m v^2 + m a$ |`(def eqn5 {:lhs "e^(1)", :rhs {:term1 "0.5*m^(1)*v^(2)", :term2 "m^(1)*a^(1)"}})`|

and define the variables/parameters as
```
(def varpars [{:symbol "e", :dimension "energy"}
              {:symbol "m", :dimension "mass"}
              {:symbol "v", :dimension "velocity"}
              {:symbol "a", :dimension "acceleration"}])

```
Then
```
=> (consistent? varpars eqn1)
false
=> (consistent? varpars eqn2)
true
=> (consistent? varpars eqn3)
false
=> (consistent? varpars eqn4)
true
=> (consistent? varpars eqn5)
false
```
which suggests $e = \frac{1}{2}m v^2$ and $e = \frac{3}{16}m v^2$ to be dimensionally consistent.

But both equations can't be correct, illustrating the point that
> a dimensionally consistent equation does not guarantee correct equation

Notice that kinetic `e` is not defined in the `standard_formula`
```
=> (require '[diman.dimensions :refer [standard_formula]])
=> (pprint standard_formula)
[{:quantity "volume", :sformula "[M^(0)*L^(3)*T^(0)]"}
 {:quantity "velocity", :sformula "[M^(0)*L^(1)*T^(-1)]"}
 {:quantity "acceleration", :sformula "[M^(0)*L^(1)*T^(-2)]"}
 {:quantity "force", :sformula "[M^(1)*L^(1)*T^(-2)]"}
 {:quantity "mass density", :sformula "[M^(1)*L^(-3)*T^(0)]"}]
```
Since we already know that the kinetic energy is in Joules and $1J = kg*m^2*s^(-2)$ whose dimensional formula is `[M^(1)*L^(2)*T(-2)]` this can be added to the `standard_formula` as
```
=> (def updated_sform (conj standard_formula {:quantity "energy", :sformula "[M^(1)*L^(2)*T(-2)]"}))
=> (intern 'diman.dimensions 'standard_formula updated_sform)
=> (pprint standard_formula)
[{:quantity "volume", :sformula "[M^(0)*L^(3)*T^(0)]"}
 {:quantity "velocity", :sformula "[M^(0)*L^(1)*T^(-1)]"}
 {:quantity "acceleration", :sformula "[M^(0)*L^(1)*T^(-2)]"}
 {:quantity "force", :sformula "[M^(1)*L^(1)*T^(-2)]"}
 {:quantity "mass density", :sformula "[M^(1)*L^(-3)*T^(0)]"}
 {:quantity "energy", :sformula "[M^(1)*L^(2)*T(-2)]"}]
```
Now since `energy` is one of the `:quantity` in the `standard_formula`, we can add the symbol `e` in our definition
```
=> (def varpars (conj varpars {:symbol "e", :dimension "energy"}))
=> (pprint varpars)
[{:symbol "m", :dimension "mass"}
 {:symbol "v", :dimension "velocity"}
 {:symbol "a", :dimension "acceleration"}
 {:symbol "e", :dimension "energy"}]
```
