=========================
Example: Journal Bearing
=========================

.. image:: ../resources/images/journal_bearing.png
   :scale: 70%
   :align: center

If one were interested in studying the frictional coefficient
.. image:: ../resources/math/f.gif
of the bearing, then we must consider the variables/parameters that may influence it.

* bearing length, :raw-latex:`L`
* bearing diameter, :raw-latex:`D`
* bearing load, :raw-latex:`P`

  - The load on the bearing is represented in terms of the average bearing pressure :raw-latex:`P = W/(LD)` where :raw-latex:`W` is the actual load of bearing.

* rotating speed, :raw-latex:`N`

  - Assume that the resulting rotating speed of the bearing is the constant average speed *N*.

* viscosity of lubricating oil, :raw-latex:`\mu`

  - This is the viscosity at equillibrium temperature &mdash; the bearing rotating at an average of :raw-latex:`N` produces heat which is conducts and convects.

* clearance between bearing and journal, :raw-latex:`C`
* bearing moment, :raw-latex:`M`

  - Load applied to the shaft passing through the bearing results in bearing moment.

We can therefore start our study with the assumption that the frictional coefficient of the bearing is a function of the above seven variables.

.. raw:: latex html

    f = f(L, D, P, N, \mu, C, M)


But, <i>L</i>, <i>D</i> and <i>C</i> have the same dimensions. Then, <i>L</i>/<i>D</i> and <i>C</i>/<i>D</i> are dimensionless.
Therefore, if we temporarily disregard the variables <i>L</i> and <i>C</i>, then we can reduce seven variables to five.

Hence, the tentative function for the derivation is

<center>
<i>f</i> = <i>f</i>(<i>D, P, N, &mu;, M</i>)
</center>

Since,

| quantity symbol | quantity name | unit (say, SI)                           | dimensions                     |
|:---------------:|:-------------:|:----------------------------------------:|:------------------------------:|
| <i>P</i>	      | pressure      | Pa = kg/m&sdot;s<sup>2</sup>             | M/(LT<sup>2</sup>)             |
| <i>M</i>        | moment        | Nm = kg&sdot;m<sup>2</sup>/s<sup>2</sup> | (ML<sup>2</sup>)/T<sup>2</sup> |
| <i>D</i>	      | diameter      | m                                        | M                              |
| <i>&mu;</i>     | viscosity     | Pa&sdot;s = kg/m&sdot;s                  | M/LT                           |
| <i>N</i>	      | speed         | s<sup>&minus;1</sup>                     | 1/T                            |

the dimensional system for the problem is MLT-system.

We can now proceed with the steps (four) for deriving the dimensionless products.

## 1. Generate Dimensional Formula for All the Terms
Since the terms for our unknown function <i>f</i> are the above five variables we do the setup as follows.

### 1.1. Setup for Generation

#### 1.1.1. Definitions setup
Since our problem uses MLT dimensional system

```
(def varpars [{:symbol "x", :quantity "mass"}
              {:symbol "y", :quantity "length"}
              {:symbol "t", :quantity "time"}])
```

#### 1.1.2. Expressions and equation
We express the variables for the unknown function <i>f</i> as

```
(def manifold_eqn [{:name "term-P", :eqn {:term1 "x^(1)*y^(-1)*t^(-2)"}}
                   {:name "term-M", :eqn {:term1 "x^(1)*y^(2)*t^(-2)"}}
                   {:name "term-D", :eqn {:term1 "y^(1)"}}
                   {:name "term-mu", :eqn {:term1 "x^(1)*y^(-1)*t^(-1)"}}
                   {:name "term-N", :eqn {:term1 "t^(-1)"}}])
```

### 1.2. Getting the Dimensional Formula
The dimensional formula all the terms are

```
=> (pprint (formula-eqn-side-manifold varpars manifold_eqn))
[{:quantity "term-P", :dimension "[M^(1)*T^(-2)*L^(-1)]"}
 {:quantity "term-M", :dimension "[M^(1)*T^(-2)*L^(2)]"}
 {:quantity "term-D", :dimension "[L^(1)]"}
 {:quantity "term-mu", :dimension "[M^(1)*T^(-1)*L^(-1)]"}
 {:quantity "term-N", :dimension "[T^(-1)]"}]
```

### 1.3 Standardize All the Generated Dimensional Formula
We add the above dimensional formulae into the `standard_formula` 

```
=> (update-sformula (formula-eqn-side-manifold varpars manifold_eqn))
[{:quantity "volume", :dimension "[L^(3)]"}
{:quantity "frequency", :dimension "[T^(-1)]"}
{:quantity "velocity", :dimension "[L^(1)*T^(-1)]"}
{:quantity "acceleration", :dimension "[L^(1)*T^(-2)]"}
{:quantity "force", :dimension "[M^(1)*L^(1)*T^(-2)]"}
...
{:quantity "term-N", :dimension "[T^(-1)]"}
{:quantity "term-mu", :dimension "[M^(1)*T^(-1)*L^(-1)]"}
{:quantity "term-D", :dimension "[L^(1)]"}
{:quantity "term-M", :dimension "[M^(1)*T^(-2)*L^(2)]"}
{:quantity "term-P", :dimension "[M^(1)*T^(-2)*L^(-1)]"}]
```

### 1.4. Definitions setup for dimensional matrix

```
(def varpars2 [{:symbol "P", :quantity "term-P"}
               {:symbol "M", :quantity "term-M"}
               {:symbol "D", :quantity "term-D"}
               {:symbol "mu", :quantity "term-mu"}
               {:symbol "N", :quantity "term-N"}])
```

## 2. Generate Dimensional Matrix

```
=> (view-matrix (generate-dimmat varpars2))
[-1N 2N 1N -1N 0]
[-2N -2N 0 -1N -1N]
[1N 1N 0 1N 0]
Size -> 3 x 5
```

## 3. Get the Homogeneous equation of the Dimensional Matrix
### 3.1. Get the augmented matrix of the dimensional matrix

```
=> (view-matrix (get-augmented-matrix (generate-dimmat varpars2)))
[1N -1N 0 1N -2N]
[0 -1N -1N 2N 2N]
[0 1N 0 -1N -1N]
Size -> 3 x 5
```

### 3.2. Solve the augmented matrix

```
=> (view-matrix (solve (get-augmented-matrix (generate-dimmat varpars2))))
[1N 0N 0N 0N -3N]
[0 1N 0N -1N -1N]
[0 0N 1N -1N -1N]
Size -> 3 x 5
```

### 3.3. Get the solution matrix

```
=> (view-matrix (get-solved-matrix (solve (get-augmented-matrix (generate-dimmat varpars2)))))
[1 0 0N -1N -1N]
[0 1 -3N -1N -1N]
Size -> 2 x 5
```

This is a 2 &times; 5 matrix. Therefore, two dimensionless products will be derived.

We can put all these individual steps involving matrix into one coding step such that it returns the solution matrix.

```
=> (def solution_matrix (get-solved-matrix
                            (solve
                                (get-augmented-matrix
                                    (generate-dimmat varpars2)))))
=> (view-matrix solution_matrix)
[1 0 0N -1N -1N]
[0 1 -3N -1N -1N]
Size -> 2 x 5
```

## 4. Get Dimensionless Products

```
=> (def all-dimless (get-dimensionless-products solution_matrix varpars2))

=> (pprint all-dimless)
[{:symbol "pi0", :expression "P^(1)*mu^(-1)*N^(-1)"}
 {:symbol "pi1", :expression "M^(1)*D^(-3)*mu^(-1)*N^(-1)"}]

=> (get-pi-expression all-dimless "pi0")
"P^(1)*mu^(-1)*N^(-1)"
```

Note that these two dimensionless products are derived from the tentative function <i>f</i> where we temporily disregarded <i>L</i>/<i>D</i> and <i>C</i>/<i>D</i>.
But, <i>L</i>/<i>D</i> and <i>C</i>/<i>D</i> are dimensionless. Therefore, the number of products in the complete set of dimensionless products is four. Hence,

<center>
<i>f</i> = <i>f</i>(<i>P</i>/(<i>&mu;</i>&sdot;<i>N</i>), <i>M</i>/(<i>D</i><sup>3</sup>&sdot;<i>&mu;</i>&sdot;<i>N</i>), <i>L</i>/<i>D</i>, <i>C</i>/<i>D</i>)
</center> 
