![diman logo](./resources/images/logo/diman.png)
# diman

[![Clojars Project](https://img.shields.io/clojars/v/com.neuralgraphs/diman.svg)](https://clojars.org/com.neuralgraphs/diman)

A Clojure library for applying dimensional analysis.

[Motivation for diman.](ProjectPlan.pdf)

## Current Features

- Create dimensional formulae.
- Create dimensional equations.
- Implement principle of dimensional homogeneity; Perform consistency checks.
- Derive dimensionless products.

## Usage

The easiest way to get all the built-in functions is to be in the default namespace `(in-ns 'diman.default)`. Then, `(println default-functions)` to list all the available functions. 

- Tutorial: Generate dimensional formulae and perform consistency checking; [reStructuredText](./doc/tutorial1.rst), [AsciiDoc](./doc/tutorial1.adoc)
- Tutorial: Derive dimensionless products; [reStructuredText](./doc/tutorial2.rst), [AsciiDoc](./doc/tutorial2.adoc)
- Example: Journal Bearing; [reStructuredText](./doc/tutorial3.rst), [AsciiDoc](./doc/tutorial3.adoc)
- Rationale for the seven base dimensions; [reStructuredText](./doc/rationale1.rst), [AsciiDoc](./doc/rationale1.adoc)
- Rationale for implementing the steps for deriving a complete set of dimensionless products; [reStructuredText](./doc/rationale2.rst), [AsciiDoc](./doc/rationale2.adoc)
- [Source code documentation](https://cljdoc.org/d/com.neuralgraphs/diman) 

## License

Copyright © 2021 Lungsi Ngwua

Distributed under BSD 3-Clause "New" or "Revised" License.