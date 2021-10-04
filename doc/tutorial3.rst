=========================
Example: Journal Bearing
=========================

.. image:: ../resources/images/journal_bearing.png
   :width: 350px
   :align: center

If one were interested in studying the frictional coefficient |f| of the bearing, then we must consider the variables/parameters that may influence it.

* bearing length, |L|
* bearing diameter, |D|
* bearing load, |P|

  - The load on the bearing is represented in terms of the average bearing pressure |P_eq_W_div_LD| where |W| is the actual load of bearing.

* rotating speed, |N|

  - Assume that the resulting rotating speed of the bearing is the constant average speed *N*.

* viscosity of lubricating oil, |mu|

  - This is the viscosity at equillibrium temperature &mdash; the bearing rotating at an average of |N| produces heat which is conducts and convects.

* clearance between bearing and journal, |C|
* bearing moment, |M|

  - Load applied to the shaft passing through the bearing results in bearing moment.

We can therefore start our study with the assumption that the frictional coefficient of the bearing is a function of the above seven variables resulting in the function value

.. image:: ../resources/math/tutorial3_func_value_of_LDPNmuCM.gif
   :align: center

But, |L|, |D| and |C| have the same dimensions. Then, |LbyD| and |CbyD| are dimensionless. Therefore, if we temporarily disregard the variables |L| and |C|, then we can reduce seven variables to five.

Hence, the tentative function for the derivation is such that its value is given by

.. image:: ../resources/math/tutorial3_func_value_of_DPNmuM.gif
   :align: center

Since,

+-----------------+---------------+----------------+------------+
| quantity symbol | quantity name | unit (say, SI) | dimensions |
+=================+===============+================+============+
| |P|	          | pressure      | |Pa|           | |MbyLT2|   |
+-----------------+---------------+----------------+------------+
| |M|             | moment        | |Nm|           | |ML2byT2|  |
+-----------------+---------------+----------------+------------+
| |D|	          | diameter      | |m|            | |L|        |
+-----------------+---------------+----------------+------------+
| |mu|            | viscosity     | |Pas|          | |MbyLT|    |
+-----------------+---------------+----------------+------------+
| |N|	          | speed         | |1_divby_s|    | |1byT|     |
+-----------------+---------------+----------------+------------+

the dimensional system for the problem is MLT-system.

We can now proceed with the steps (four) for deriving the dimensionless products.

1. Generate Dimensional Formula for All the Terms
=================================================

The derivation of the dimensionless products will be based on the reduced |f| where
the parent function depends on the independent five variables. The generation of
the dimensional matrix is follows some preceding setup steps.

1.1. Setup for Generation
-------------------------

1.1.1. Definitions setup
~~~~~~~~~~~~~~~~~~~~~~~~

Since our problem uses MLT dimensional system

::

    (def varpars [{:symbol "x", :quantity "mass"}
                  {:symbol "y", :quantity "length"}
                  {:symbol "t", :quantity "time"}])

1.1.2. Expressions and equation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

We express the variables for the unknown function |f| as

::

    (def manifold_eqn [{:name "term-P", :eqn {:term1 "x^(1)*y^(-1)*t^(-2)"}}
                       {:name "term-M", :eqn {:term1 "x^(1)*y^(2)*t^(-2)"}}
                       {:name "term-D", :eqn {:term1 "y^(1)"}}
                       {:name "term-mu", :eqn {:term1 "x^(1)*y^(-1)*t^(-1)"}}
                       {:name "term-N", :eqn {:term1 "t^(-1)"}}])

1.2. Getting the Dimensional Formula
------------------------------------

The dimensional formula all the terms are

::

    => (pprint (formula-eqn-side-manifold varpars manifold_eqn))
      [{:quantity "term-P", :dimension "[M^(1)*T^(-2)*L^(-1)]"}
       {:quantity "term-M", :dimension "[M^(1)*T^(-2)*L^(2)]"}
       {:quantity "term-D", :dimension "[L^(1)]"}
       {:quantity "term-mu", :dimension "[M^(1)*T^(-1)*L^(-1)]"}
       {:quantity "term-N", :dimension "[T^(-1)]"}]

1.3 Standardize All the Generated Dimensional Formula
-----------------------------------------------------

We add the above dimensional formulae into the `standard_formula` 

::

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

1.4. Definitions setup for dimensional matrix
---------------------------------------------

::

    (def varpars2 [{:symbol "P", :quantity "term-P"}
                   {:symbol "M", :quantity "term-M"}
                   {:symbol "D", :quantity "term-D"}
                   {:symbol "mu", :quantity "term-mu"}
                   {:symbol "N", :quantity "term-N"}])

2. Generate Dimensional Matrix
==============================

::

    => (view-matrix (generate-dimmat varpars2))
      [-1N 2N 1N -1N 0]
      [-2N -2N 0 -1N -1N]
      [1N 1N 0 1N 0]
      Size -> 3 x 5

3. Get the Homogeneous equation of the Dimensional Matrix
=========================================================

3.1. Get the augmented matrix of the dimensional matrix
-------------------------------------------------------

::

    => (view-matrix (get-augmented-matrix (generate-dimmat varpars2)))
      [1N -1N 0 1N -2N]
      [0 -1N -1N 2N 2N]
      [0 1N 0 -1N -1N]
      Size -> 3 x 5

3.2. Solve the augmented matrix
-------------------------------

::

    => (view-matrix (solve (get-augmented-matrix (generate-dimmat varpars2))))
      [1N 0N 0N 0N -3N]
      [0 1N 0N -1N -1N]
      [0 0N 1N -1N -1N]
      Size -> 3 x 5

3.3. Get the solution matrix
----------------------------

::

    => (view-matrix (get-solved-matrix (solve (get-augmented-matrix (generate-dimmat varpars2)))))
      [1 0 0N -1N -1N]
      [0 1 -3N -1N -1N]
      Size -> 2 x 5

This is a 2 &times; 5 matrix. Therefore, two dimensionless products will be derived.

We can put all these individual steps involving matrix into one coding step such that it returns the solution matrix.

::

    => (def solution_matrix (get-solved-matrix
                                (solve
                                    (get-augmented-matrix
                                        (generate-dimmat varpars2)))))
    => (view-matrix solution_matrix)
      [1 0 0N -1N -1N]
      [0 1 -3N -1N -1N]
      Size -> 2 x 5

4. Get Dimensionless Products
=============================

::

    => (def all_dimless (get-dimensionless-products solution_matrix varpars2))

    => (pprint all_dimless)
      [{:symbol "pi0", :expression "P^(1)*mu^(-1)*N^(-1)"}
       {:symbol "pi1", :expression "M^(1)*D^(-3)*mu^(-1)*N^(-1)"}]

    => (get-pi-expression all_dimless "pi0")
      "P^(1)*mu^(-1)*N^(-1)"

Note that these two dimensionless products are derived from the tentative function |f| where we temporarily disregarded |LbyD| and |CbyD|.

But, |LbyD| and |CbyD| are dimensionless. Therefore, the number of products in the complete set of dimensionless products is four.
Hence, the frictional coefficient has the value

.. image:: ../resources/math/tutorial3_func_value_of_pis.gif
   :align: center

   

.. |f| image:: ../resources/math/f.gif

.. |L| image:: ../resources/math/L.gif

.. |D| image:: ../resources/math/D.gif

.. |P| image:: ../resources/math/P.gif

.. |W| image:: ../resources/math/W.gif

.. |N| image:: ../resources/math/N.gif

.. |C| image:: ../resources/math/C.gif

.. |M| image:: ../resources/math/M.gif

.. |mu| image:: ../resources/math/mu.gif

.. |P_eq_W_div_LD| image:: ../resources/math/P_eq_W_div_LD.gif

.. |Pa| image:: ../resources/math/Pascal.gif

.. |Nm| image:: ../resources/math/NewtonMeter.gif

.. |m| image:: ../resources/math/Meter.gif

.. |Pas| image:: ../resources/math/PascalSecond.gif

.. |1_divby_s| image:: ../resources/math/1overSecond.gif

.. |MbyLT2| image:: ../resources/math/MbyLT2.gif

.. |ML2byT2| image:: ../resources/math/ML2byT2.gif

.. |MbyLT| image:: ../resources/math/MbyLT.gif

.. |1byT| image:: ../resources/math/1byT.gif

.. |LbyD| image:: ../resources/math/LbyD.gif

.. |CbyD| image:: ../resources/math/CbyD.gif
