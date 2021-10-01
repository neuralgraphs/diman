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

- Tutorial: Generate dimensional formulae and perform consistency checking; [AsciiDoc](./doc/tutorial1.adoc), [reStructuredText for Bitbucket](./doc/tutorial1.rst)
- Tutorial: Derive dimensionless products; [AsciiDoc](./doc/tutorial2.adoc), [reStructuredText for Bitbucket](./doc/tutorial2.rst)
- Example: Journal Bearing; [AsciiDoc](./doc/tutorial3.adoc), [reStructuredText for Bitbucket](./doc/tutorial3.rst)
- Rationale for the seven base dimensions; [AsciiDoc](./doc/rationale1.adoc), [reStructuredText for Bitbucket](./doc/rationale1.rst)
- Rationale for implementing the steps for deriving a complete set of dimensionless products; [AsciiDoc](./doc/rationale2.adoc), [reStructuredText for Bitbucket](./doc/rationale2.rst)
- [Source code documentation](https://cljdoc.org/d/com.neuralgraphs/diman) 

## License

Copyright © 2021 Lungsi Ngwua

Distributed under BSD 3-Clause "New" or "Revised" License.