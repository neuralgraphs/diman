(ns diman.default
  (:require
    [diman.dimensions :refer [base_dimensions standard_formula update-sformula]]
    [diman.formula :refer [formula-term formula-eqn-side formula-eqn-side-manifold]]
    [diman.analyze :refer [dimnames consistent?]]
    [diman.buckingham
     [dimensional-matrix :refer [generate-dimmat]]
     [homogeneous-equation :refer [get-augmented-matrix solve get-solution-matrix]]
     [dimensionless-product :refer [get-dimensionless-products get-pi-expression]]]
    [diman [core :refer [view-matrix]]]
    [diman.linalg.matfun [rank :refer [rank]]]
    ))

(def default-functions
    "\n
    base_dimensions*, standard_formula*, update-sformula,\n
    formula-term, formula-eqn-side, formula-eqn-side-manifold,\n
    dimnames, consistent?,\n
    generate-dimmat,\n
    get-augmented-matrix, solve, get-solution-matrix,\n
    get-dimensionless-products, get-pi-expression,\n
    view-matrix,\n
    rank\n
    Asterix (*) denotes data and not a function.")
