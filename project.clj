;; Copyright (c) Lungsi Ngwua. All rights reserved
(defproject diman "0.0.3-Alpha"
  :description "A Clojure library for applying dimensional analysis."
  :url "https://github.com/lungsi/diman"
  :license {:name "BSD 3-Clause 'New' or 'Revised' License"
            :url "https://choosealicense.com/licenses/bsd-3-clause/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :plugins [[lein-codox "0.10.7"]]                          ; https://github.com/weavejester/codox
  :codox {
          :output-path "codox"                              ; write output to codox/
          :source-uri                                       ; document links to source files
                       "http://github.com/lungsi/diman/blob/{version}/{filepath}#L{line}"
          :source-paths ["src"]                             ; location of source files
          ;:doc-files ["ProjectPlan.pdf",                    ; only supports .md or .markdown
          ;            "doc/tutorial1.pdf"]
          }
  :repl-options {:prompt (fn [ns] (str "diman <" ns "> => "));; Custom repl prompt for diman
                 :welcome (println "Welcome to diman\nA <dim>ensional <an>alysis tool capable of deriving dimensionless products.\nTo view built-in functions do (println default-functions).")
                 :init-ns diman.default
                 ;:init (println "You are in" *ns*)          ;; Top of the repl
                 }
  :repositories [["releases" {:url "https://repo.clojars.org"
                              :creds :gpg}]
                ;; w/o gpg you are the only author,does not require jar verification from others
                 ["snapshots" {:sign-releases false :url "https://repo.clojars.org"}]]
  )
