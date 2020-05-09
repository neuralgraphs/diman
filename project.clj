(defproject diman "0.0.1-Alpha"
  :description "A Clojure library for applying dimensional analysis."
  :url "https://github.com/lungsi/diman"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :plugins [[lein-codox "0.10.7"]]                          ; https://github.com/weavejester/codox
  :codox {
          :output-path "codox"                              ; write output to codox/
          :source-uri                                       ; document links to source files
                       "http://github.com/lungsi/diman/blob/{version}/{filepath}#L{line}"
          :source-paths ["src"]                             ; location of source files
          ;:doc-files ["ProjectPlan.pdf",                    ; only supports .md or .markdown
          ;            "doc/tutorial1.pdf"]
          }
  )