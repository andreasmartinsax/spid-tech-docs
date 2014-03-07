(ns spid-docs.pages.type-pages
  (:require [spid-docs.formatting :refer [to-html]]))

(defn type-path [type]
  "Given a type (typically one entry from the vector in resources/types.end),
   return its URL"
  (if (:description type)
    (str "/types/" (name (:id type)))))

(defn create-page [type]
  {:body (list [:h1 (name (:id type))]
               (to-html (:description type)))})

(defn create-pages [types]
  "Takes a list of types (typically those defined in resources/types.edn) and
   returns a map of url => page function."
  (->> types
       (filter :description)
       (map (juxt type-path #(partial create-page %)))
       (into {})))
