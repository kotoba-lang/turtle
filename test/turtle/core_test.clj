(ns turtle.core-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [turtle.core :as turtle]))

(defn iri [v] {:rdf/type :iri :value v})
(defn lit [v] {:rdf/type :literal :value v})

(deftest renders-turtle
  (let [q {:subject (iri "https://example.test/s")
           :predicate (iri "https://example.test/p")
           :object (lit "hello")}]
    (is (= "<https://example.test/s> <https://example.test/p> \"hello\" ."
           (turtle/triple q)))))

(deftest renders-prefixes
  (is (str/includes? (turtle/turtle {:ex "https://example.test/"} [])
                     "@prefix ex: <https://example.test/> .")))
