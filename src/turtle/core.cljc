(ns turtle.core
  "Turtle serializer for EDN RDF triples."
  (:require [clojure.string :as str]))

(defn esc [s]
  (-> (str s)
      (str/replace "\\" "\\\\")
      (str/replace "\"" "\\\"")
      (str/replace "\n" "\\n")
      (str/replace "\r" "\\r")))

(defn iri-ref [s] (str "<" s ">"))

(defn literal-str [{:keys [value datatype language]}]
  (str "\"" (esc value) "\""
       (cond
         language (str "@" language)
         datatype (str "^^" (iri-ref (:value datatype)))
         :else "")))

(defn term [x]
  (case (:rdf/type x)
    :iri (iri-ref (:value x))
    :blank (str "_:" (:id x))
    :literal (literal-str x)
    (throw (ex-info "Unknown RDF term" {:term x}))))

(defn prefix [[p iri]]
  (str "@prefix " (name p) ": <" iri "> ."))

(defn triple [{:keys [subject predicate object]}]
  (str (term subject) " " (term predicate) " " (term object) " ."))

(defn turtle
  ([triples] (turtle {} triples))
  ([prefixes triples]
   (str/join "\n"
             (concat
              (map prefix prefixes)
              (when (seq prefixes) [""])
              (map triple triples)))))

(def render turtle)
