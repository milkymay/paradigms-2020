;delay

(defn abstractNaryOperation [func op & args]
      (fn [vars]
          (func op
              (map
                  (fn [x] (x vars))
                  args)
          )
      )
)

(defn abstractUnaryOperation [f x]
      (fn [vars]
          (f (x vars))
    )
)

(defn variable [name] (fn [vars] (vars name)))
(defn constant [arg] (constantly arg))

(def divide (partial abstractNaryOperation reduce (fn [a b] (/ a (double b)))))
(def multiply (partial abstractNaryOperation apply *))
(def subtract (partial abstractNaryOperation apply -))
(def add (partial abstractNaryOperation apply +))
(def min (partial abstractNaryOperation reduce (fn [a b] (Math/min a b))))
(def max (partial abstractNaryOperation reduce (fn [a b] (Math/max a b))))
(def negate (partial abstractUnaryOperation -))
(def exp (partial abstractUnaryOperation (fn [a] (Math/exp (double a)))))
(def ln (partial abstractUnaryOperation (fn [a] (Math/log (Math/abs a)))))
(defn avg [& args] (fn [vars] (/ ((apply add args) vars) (count args))))
(defn med [& args] (fn [vars] (nth (sort (map (fn [x] (x vars)) args)) (/ (count args) 2))))
(def pw (partial abstractNaryOperation reduce (fn [a b] (Math/pow a b))))
(def lg (partial abstractNaryOperation reduce (fn [a b] (/ (Math/log (Math/abs b)) (double (Math/log (Math/abs a)))))))




(def variables
  {'x (variable "x")
   'y (variable "y")
   'z (variable "z")})

(def operations
  {'+      add
   'negate negate
   '-      subtract
   '*      multiply
   '/      divide
   'exp    exp
   'ln     ln
   'min    min
   'max    max
   'avg    avg
   'med    med
   'pw     pw
   'lg     lg})

(defn parse [expr]
      (cond
        (list? expr)
          (apply (operations (first expr))
                 (map parse (rest expr)))
        (number? expr)
          (constant expr)
        (contains? variables expr)
          (variables expr)
        (contains? operations expr)
          (operations expr)
      )
)

(defn parseFunction [expression]
      (parse (read-string expression)))