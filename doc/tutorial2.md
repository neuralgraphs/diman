# Introduction to diman: Deriving dimensionless products
For this tutorial imagine the equation derived from experimental observations is

<img src="https://latex.codecogs.com/svg.image?u=\frac{x^3}{y}t&plus;xy^6t^{20}&plus;\frac{x^3}{y^3t^3}&plus;\frac{x^4}{t^8}" title="u=\frac{x^3}{y}t+xy^6t^{20}+\frac{x^3}{y^3t^3}+\frac{x^4}{t^8}" />

**What are its dimensionless products?**

The four steps for deriving the dimensionless products are as follows.

## 1. Generate Dimensional Formula for All the Terms (usually right hand side of equation)
Let us define <img src="https://latex.codecogs.com/svg.image?p\triangleq\frac{x^3}{y}t" title="p\triangleq\frac{x^3}{y}t" />, <img src="https://latex.codecogs.com/svg.image?q\triangleq&space;xy^6t^{20}" title="q\triangleq xy^6t^{20}" />, <img src="https://latex.codecogs.com/svg.image?r\triangleq&space;\frac{x^3}{y^3t^3}" title="r\triangleq \frac{x^3}{y^3t^3}" /> and <img src="https://latex.codecogs.com/svg.image?s\triangleq&space;\frac{x^4}{t^8}" title="s\triangleq \frac{x^4}{t^8}" />. Replacing *p*, *q*, *r* and *s* for the terms in the main equation we get

<img src="https://latex.codecogs.com/svg.image?u=p&plus;q&plus;r&plus;s" title="u=p+q+r+s" />

Broadly, the task is to

- generate dimensional formula for each term
- insert all the generated dimensional formula into `standard_formula`

### 1.1. Setup for Generation

#### 1.1.1. Definitions setup
Define all the symbols in the parent mathematical expression that is associated with a dimension.

```
(def varpars [{:symbol "x", :dimension "mass"}
              {:symbol "y", :dimension "length"}
              {:symbol "t", :dimension "time"}])
```

#### 1.1.2. Expressions and equation
Considering each term of the parent equation as some individual equation we define each of them as follows

```
(def p_equation {:lhs "p^(1)", :rhs {:term1 "x^(2)*y^(-1)*t^(1)"}})
(def q_equation {:lhs "q^(1)", :rhs {:term1 "x^(1)*y^(6)*t^(20)"}})
(def r_equation {:lhs "r^(1)", :rhs {:term1 "x^(3)*y^(-3)*t^(-3)"}})
(def s_equation {:lhs "s^(1)", :rhs {:term1 "x^(4)*t^(8)"}})
```

and then stacking the equations into a vector

```
(def manifold_eqn [{:name "term-p", :eqn (:rhs p_equation)}
                   {:name "term-q", :eqn (:rhs q_equation)}
                   {:name "term-r", :eqn (:rhs r_equation)}
                   {:name "term-s", :eqn (:rhs s_equation)}])
```

The two steps are equivalent to

```
(def manifold_eqn [{:name "term-p", :eqn {:term1 "x^(2)*y^(-1)*t^(1)"}}
                   {:name "term-q", :eqn {:term1 "x^(1)*y^(6)*t^(20)"}}
                   {:name "term-r", :eqn {:term1 "x^(3)*y^(-3)*t^(-3)"}}
                   {:name "term-s", :eqn {:term1 "x^(4)*t^(8)"}}])
```

However, the two step approach is recommended because it affords the user the flexibility to actually see the generation of individual dimensional formula and hence introspecting them.

### 1.2. Getting the Dimensional Formula
The dimensional formula for one side of the expression (often right hand side) for every equation in the vector of all the equations defined earlier can be generated using the `formula-eqn-side-manifold` function.

```
=> (pprint (formula-eqn-side-manifold varpars manifold_eqn))
[{:quantity "term-p", :formula "[L^(-1)*M^(2)*T^(1)]"}
 {:quantity "term-q", :formula "[L^(6)*M^(1)*T^(20)]"}
 {:quantity "term-r", :formula "[T^(-3)*L^(-3)*M^(3)]"}
 {:quantity "term-s", :formula "[M^(4)*T^(8)]"}]
```

### 1.3 Standardize All the Generated Dimensional Formula
All the dimensional formula generated from each equation in the vector of equations is added to the `standard_formula` with 

```
=> (update-sformula (formula-eqn-side-manifold varpars manifold_eqn))
[{:quantity "volume", :sformula "[L^(3)]"}
{:quantity "frequency", :sformula "[T^(-1)]"}
{:quantity "velocity", :sformula "[L^(1)*T^(-1)]"}
{:quantity "acceleration", :sformula "[L^(1)*T^(-2)]"}
{:quantity "force", :sformula "[M^(1)*L^(1)*T^(-2)]"}
...
{:quantity "term-s", :sformula "[M^(4)*T^(8)]"}
{:quantity "term-r", :sformula "[T^(-3)*L^(-3)*M^(3)]"}
{:quantity "term-q", :sformula "[L^(6)*M^(1)*T^(20)]"}
{:quantity "term-p", :sformula "[L^(-1)*M^(2)*T^(1)]"}]
```

### 1.4. Definitions setup for the reduced form of the parent expression
Since all the dimensional formula of *p*, *q*, *r* and *s*, representing all the terms in the main equation are now part of the `standard_formula`, we can now define all the symbols in the reduced form of the parent mathematical expression

<img src="https://latex.codecogs.com/svg.image?u=p&plus;q&plus;r&plus;s" title="u=p+q+r+s" />

The definition will be such that each term symbol has the dimension name as defined in the preceeding step (and hence incorporated into the `standard_formula`. For instance, since the term *p* (<img src="https://latex.codecogs.com/svg.image?p\triangleq\frac{x^3}{y}t" title="p\triangleq\frac{x^3}{y}t" />) was named *"term-p"* in

```
=> (pprint manifold_eqn)
[{:name "term-p", :eqn {:term1 "x^(2)*y^(-1)*t^(1)"}}
 {:name "term-q", :eqn {:term1 "x^(1)*y^(6)*t^(20)"}}
 {:name "term-r", :eqn {:term1 "x^(3)*y^(-3)*t^(-3)"}}
 {:name "term-s", :eqn {:term1 "x^(4)*t^(8)"}}]
```

we will have `{:symbol "p", :dimension "term-p"}`. Therefore, we define

```
(def varpars2 [{:symbol "p", :dimension "term-p"}
               {:symbol "q", :dimension "term-q"}
               {:symbol "r", :dimension "term-r"}
               {:symbol "s", :dimension "term-s"}])
```

## 2. Generate Dimensional Matrix
The dimensional matrix of the parent equation is generated with the help of the `generate-dimmat` function.

```
=> (view-matrix (generate-dimmat varpars2))
[1N 20N -3N 8N]
[2N 1N 3N 4N]
[-1N 6N -3N 0]
Size -> 3 x 4
```

This is a 3 &times; 4 dimensional matrix.

## 3. Get the Homogeneous equation of the Dimensional Matrix
### 3.1. Get the augmented matrix of the dimensional matrix


```
=> (view-matrix (get-augmented-matrix (generate-dimmat varpars2)))
[-3N 8N -1N -20N]
[3N 4N -2N -1N]
[-3N 0 1N -6N]
Size -> 3 x 4
```

### 3.2. Solve the augmented matrix

```
=> (view-matrix (solve (get-augmented-matrix (generate-dimmat varpars2))))
[1N 0N -1/3 2N]
[0N 1N -1/4 -7/4]
[0N 0N 0N 0N]
Size -> 3 x 4
```

### 3.3. Get the solution matrix

```
=> (view-matrix (get-solved-matrix (solve (get-augmented-matrix (generate-dimmat varpars2)))))
[1 0 -1/3 -1/4]
[0 1 2N -7/4]
Size -> 2 x 4
```

This is a 2 &times; 4 matrix. Therefore, there will be two dimensionless products.

We can put all these individual steps involving matrix into one coding step such that it returns the solution matrix.

```
=> (def solution_matrix (get-solved-matrix
                            (solve
                                (get-augmented-matrix
                                    (generate-dimmat varpars2)))))
=> (view-matrix solution_matrix)
[1 0 -1/3 -1/4]
[0 1 2N -7/4]
Size -> 2 x 4
```

## 4. Get Dimensionless Products
The dimensionless products are generated with the help of the `get-dimensionless-products` function.

```
=> (pprint (get-dimensionless-products solution_matrix varpars2))
[{:symbol "pi0", :expression "p^(1)*r^(-1/3)*s^(-1/4)"}
 {:symbol "pi1", :expression "q^(1)*r^(2)*s^(-7/4)"}]
```

Since, &pi; is the conventional symbol for dimensionless products to get the &pi;<sub><i>i</i></sub><sup>th</sup> one use the `get-pi-expression` function. For example, for &pi;<sub>0</sub><sup>

```
=> (def all-dimless (get-dimensionless-products solution_matrix varpars2))
=> (get-pi-expression all-dimless "pi0")
"p^(1)*r^(-1/3)*s^(-1/4)"
```
