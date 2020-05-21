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
(def ConstantPrototype
  (let [number (field :value)]
       {:toString #(format "%.1f" (number %))
        :evaluate (fn [this _]
                      (number this))
        :diff (fn [_ _] (Constant 0))}))

(defn Constant [val]
      {:prototype ConstantPrototype
       :value     val})

(def VariablePrototype
  (let  [name (field :value)]
        {:toString #(name %)
         :evaluate #(%2 (name %1))
         :diff     #(if (= (name %1) %2) (Constant 1) (Constant 0))}))

(defn Variable [val]
      {:prototype VariablePrototype
       :value     val})

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

(def Add (makeOperation '+ + (fn [this var] (apply Add (diffEach this var)))))

(def Subtract (makeOperation '- - (fn [this var] (apply Subtract (diffEach this var)))))

(def Multiply (makeOperation '* * (fn [this var]
                                         (Add (Multiply (operandByInd this 0) (diffAtInd var this 1))
                                              (Multiply (operandByInd this 1) (diffAtInd var this 0))))))

(def Divide (makeOperation '/ (fn [x y] (/ x (double y)))
                              (fn [this var] (Divide (Subtract (Multiply (operandByInd this 1) (diffAtInd var this 0))
                                                               (Multiply (operandByInd this 0) (diffAtInd var this 1)))
                                                     (Multiply (operandByInd this 1) ((args this) 1))))))

(def Negate (makeOperation 'negate - (fn [this var] (apply Negate (diffEach this var)))))

(def Square (makeOperation 'square #(* % %) (fn [this var] (Multiply (Constant 2) (operandByInd this 0) (diffAtInd var this 0)))))

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