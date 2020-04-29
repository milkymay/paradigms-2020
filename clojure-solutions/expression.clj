(defn abstractNaryOperation [func op & args]
      (fn [vars]
          (func op (map (fn [x] (x vars)) args))))

(defn abstractUnaryOperation [f x]
      (fn [vars]
          (f (x vars))))

(defn variable [name] (fn [vars] (vars name)))
(defn constant [arg] (constantly arg))

(def divide (partial abstractNaryOperation reduce (fn [a b] (/ a (double b)))))
(def multiply (partial abstractNaryOperation apply *))
(def subtract (partial abstractNaryOperation apply -))
(def add (partial abstractNaryOperation apply +))
;(def min (partial abstractNaryOperation reduce (fn [a b] (Math/min a b))))
;(def max (partial abstractNaryOperation reduce (fn [a b] (Math/max a b))))
(def negate (partial abstractUnaryOperation -))
;(def exp (partial abstractUnaryOperation (fn [a] (Math/exp a))))
;(def ln (partial abstractUnaryOperation (fn [a] (Math/ln a))))


(def variables
  {'x (variable "x")
   'y (variable "y")
   'z (variable "z")})

(def operations
  {'+      add
   'negate negate
   '-      subtract
   '*      multiply
   '/      divide})

(defn parse [expr]
      (cond
        (list? expr)
        (apply (operations (first expr))
               (map parse (rest expr)))
        (number? expr) (constant expr)
        (contains? variables expr) (variables expr)
        (contains? operations expr) (operations expr)))

(defn parseFunction [expression]
      (parse (read-string expression)))