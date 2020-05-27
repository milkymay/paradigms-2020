;review HW 10, review HW 11 (from string 39)

(defn abstractOperation [op]
      (fn [& args] (fn [vars] (apply op (map (fn [x] (x vars)) args)))))

(defn variable [name] (fn [vars] (vars name)))
(defn constant [val] (constantly val))

(comment "common 1")
(def divide (abstractOperation #(/ %1 (double %2))))
(def multiply (abstractOperation *))
(def subtract (abstractOperation -))
(def add (abstractOperation +))
(def negate (abstractOperation -))
(def pw (abstractOperation #(Math/pow %1 %2)))
(def lg (abstractOperation #(/ (Math/log (Math/abs %2)) (double (Math/log (Math/abs %1))))))

(def operations
  {'+      add
   'negate negate
   '-      subtract
   '*      multiply
   '/      divide
   'pw     pw
   'lg     lg})

(defn parse [expr]
      (cond
        (list? expr)
          (apply (operations (first expr))
                 (map parse (rest expr)))
        (number? expr)
          (constant expr)
        :else (variable (str expr))))

(defn parseFunction [expression]
      (parse (read-string expression)))

; review HW 11 /////////////////////////////////////////////////////////////////////////////////////////////////////////

(defn proto-get [obj key]
      (cond
        (contains? obj key) (obj key)
        (contains? obj :prototype) (proto-get (obj :prototype) key)
        :else nil))

(defn proto-call [this key & args]
      (apply (proto-get this key) this args))

(defn field [key]
      (fn [this] (proto-get this key)))

(defn method [key]
      (fn [this & args] (apply proto-call this key args)))

(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def args (field :args))

(def Constant)

(defn Constant [val]
      (let [number (field :value)] {
          :toString #(format "%.1f" (number %))
          :evaluate (fn [this _]
                        (number this))
          :diff (fn [_ _] (Constant 0))
          :value     val}))

(comment ":NOTE: merge or remove prototype of Constant and Variable")

(defn Variable [val]
      (let [name (field :value)] {
          :toString #(name %)
          :evaluate #(%2 (name %1))
          :diff     #(if (= (name %1) %2) (Constant 1) (Constant 0))
          :value     val})
      )

(def diffAtInd (fn [var this ind] (diff ((args this) ind) var)))
(def diffEach #(map (fn [arg] (diff arg %2)) (args %1)))
(def operandByInd #((args %1) %2))

(def abstractOperation
  (let [args (field :args)
        symbol (field :operationName)
        function (field :operation)
        wayToDiff (method :wayToDiff)]
       {:toString #(str "(" (symbol %) " " (clojure.string/join " " (mapv toString (args %))) ")")
        :evaluate #(apply (function %1) (mapv (fn [arg] (evaluate arg %2)) (args %1)))
        :diff     #(wayToDiff %1 %2)}))

(defn makeOperation
      [operationName operation wayToDiff]
      (fn [& args]
          {:prototype {:prototype  abstractOperation
                       :operationName   operationName
                       :operation   operation
                       :wayToDiff wayToDiff}
           :args  (vec args)}))

(comment ":NOTE: copy-paste (at least call `diffEach` of operands in each declaration)")

(defn Func [Op] (fn [this var] (apply Op (diffEach this var))))

(def Add (makeOperation '+ + (Func #'Add)))

(def Subtract (makeOperation '- - (Func #'Subtract)))

(def Negate (makeOperation 'negate - (Func #'Negate)))


(def Multiply (makeOperation '* * #(Add (Multiply (operandByInd %1 0) (diffAtInd %2 %1 1))
                                       (Multiply (operandByInd %1 1) (diffAtInd %2 %1 0)))))

(def Divide (makeOperation '/ #(/ %1 (double %2))
                              #(Divide (Subtract (Multiply (operandByInd %1 1) (diffAtInd %2 %1 0))
                                                     (Multiply (operandByInd %1 0) (diffAtInd %2 %1 1)))
                                           (Multiply (operandByInd %1 1) ((args %1) 1)))))

(def Square (makeOperation 'square #(* % %) #(Multiply (Constant 2) (operandByInd %1 0) (diffAtInd %2 %1 0))))

(def Sqrt
  (makeOperation 'sqrt #(Math/sqrt (Math/abs %))
        (fn [this var] (Multiply (Sqrt (Square (operandByInd this 0)))
             (diffAtInd var this 0)
             (Divide (Constant 1) (Multiply (operandByInd this 0)
                 (Constant 2)
                 (Sqrt (operandByInd this 0))))))))

(def objectOperations
  {
   '+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'negate Negate
   'sqrt Sqrt
   'square Square
   })

(defn parseObjectExpression [expr]
      (cond
        (seq? expr) (apply (objectOperations (first expr)) (mapv parseObjectExpression (rest expr)))
        (number? expr) (Constant expr)
        :else (Variable (str expr))))

(def parseObject (comp parseObjectExpression read-string))

