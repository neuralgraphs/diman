(ns diman.default
  (:require
    [diman.dimensions :refer [standard_formula update-sformula]]
    [diman.formula :refer [formula-term formula-eqn-side formula-eqn-side-manifold]]
    [diman.analyze :refer [dimnames consistent?]]
    [diman.buckingham
     [dimensional-matrix :refer [generate-dimmat]]
     [homogeneous-equation :refer [get-augmented-matrix solve get-solved-matrix]]
     [dimensionless-product :refer [get-dimensionless-products get-pi-expression]]]
    [diman [core :refer [view-matrix]]]
    ))
