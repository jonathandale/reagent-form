(set-env! :resource-paths #{"src/cljs"}
          :source-paths   #{"test/cljs"}
          :dependencies   '[[org.clojure/clojurescript "1.10.64" :scope "provided"]

                            [adzerk/bootlaces  "0.1.13" :scope "test"]
                            [adzerk/boot-test  "1.2.0"  :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[adzerk.bootlaces :refer :all]
         '[boot.git         :refer [last-commit]])

(def project 'oconn/reagent-form)
(def +version+ "0.1.2-SNAPSHOT")

(task-options!
 pom {:project     project
      :version     +version+
      :description "A ClojureScript (reagent) library for working with forms"
      :url         "https://github.com/oconn/reagent-form"
      :scm         {:url "https://github.com/oconn/reagent-form"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 push {:repo "deploy-clojars"
       :ensure-clean true
       :ensure-tag (last-commit)
       :ensure-version +version+})

(bootlaces! +version+ :dont-modify-paths? true)

(deftask install-local
  "Build and install the project locally."
  []
  (comp (pom)
        (jar)
        (install)))

(deftask dev
  "Watches for changes and then installs locally"
  []
  (comp (watch)
        (install-local)))

(deftask deploy-snapshot
  "Deploys a new build to clojars"
  []
  (comp (build-jar)
        (push-snapshot)))

(deftask deploy-release
  "Deploys a release build to clojars"
  []
  (comp (build-jar)
        (push-release)))
