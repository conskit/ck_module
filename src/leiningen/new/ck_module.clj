(ns leiningen.new.ck_module
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clojure.walk :as cw]))

(def render (renderer "ck_module"))

(defn ck_module
  "FIXME: write documentation"
  [name & args]
  (let [opts (cw/keywordize-keys (apply hash-map args))
        data {:name       name
              :ck-version (or (:-ck opts) "0.2.0-SNAPSHOT")
              :module-name (or (:-mod opts) "module")
              :sanitized  (name-to-path name)}]
    (main/info "Generating fresh ck_module project.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             [".gitignore" (render ".gitignore" data)]
             [".travis.yml" (render ".travis.yml" data)]
             ["CHANGELOG.md" (render "CHANGELOG.md" data)]
             ["README.md" (render "README.md" data)]
             ["dev/user.clj" (render "user.clj" data)]
             ["dev-resources/logback-test.xml" (render "logback-test.xml" data)]
             ["dev-resources/test-config.conf" (render "test-config.conf" data)]
             ["src/{{sanitized}}.clj" (render "mod.clj" data)]
             ["test/{{sanitized}}_test.clj" (render "mod_test.clj" data)]
             )))
