;review HW 11 hard (from string 42), review HW 12 easy (string 165)

(defn abstractOperation [op]
      (fn [& args] (fn [vars] (apply op (map (fn [x] (x vars)) args)))))

(defn variable [name] (fn [vars] (vars name)))
(def constant constantly)

(def divide (abstractOperation #(/ %1 (double %2))))
(def multiply (abstractOperation *))
(def subtract (abstractOperation -))
(def add (abstractOperation +))
(def negate subtract)
(def pw (abstractOperation #(Math/pow %1 %2)))
(def lg (abstractOperation #(/ (Math/log (Math/abs %2)) (double (Math/log (Math/abs %1))))))
(def avg (abstractOperation #(/ (apply + %&) (count %&))))
(def med (abstractOperation #(nth (sort %&) (/ (count %&) 2))))

(def operations
  {'+      add
   'negate negate
   '-      subtract
   '*      multiply
   '/      divide
   'pw     pw
   'lg     lg
   'avg    avg
   'med    med})

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
(def toStringSuffix (method :toStringSuffix))
(def diff (method :diff))
(def args (field :args))

(def Constant)

(defn Constant [val]
      (let [number (field :value)] {
                                    :toString #(format "%.1f" (number %))
                                    :evaluate (fn [this _] (number this))
                                    :toStringSuffix  #(format "%.1f" (number %))
                                    :diff (fn [_ _] (Constant 0))
                                    :value     val}))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(defn Variable [val]
      (let [name (field :value)] {
                                  :toString #(name %)
                                  :evaluate #(%2 (name %1))
                                  :toStringSuffix #(name %)
                                  :diff     #(if (= (name %1) %2) ONE ZERO)
                                  :value     val})
      )

(def diffAtInd (fn [var this ind] (diff ((args this) ind) var)))
(def diffEach #(map (fn [arg] (diff arg %2)) (args %1)))
(def operandByInd #((args %1) %2))

(def abstractOperation
  (let [args (field :args)
        symbol (field :operationName)
        function (field :operation)
        wayToDiff (field :wayToDiff)]
       {:toString #(str "(" (symbol %) " " (clojure.string/join " " (mapv toString (args %))) ")")
        :toStringSuffix #(str "(" (clojure.string/join " " (mapv toStringSuffix (args %))) " " (symbol %) ")")
        :evaluate #(apply (function %1) (mapv (fn [arg] (evaluate arg %2)) (args %1)))
        :diff (fn [this var] (let [vars (args this)
                                   diffed (mapv #(diff % var) vars)]
                                  ((wayToDiff this) vars diffed)))}))

 (defn makeOperation
       [operationName operation wayToDiff]
       (fn [& args]
           {:prototype {:prototype     abstractOperation
                        :operationName operationName
                        :operation     operation
                        :wayToDiff     wayToDiff}
            :args      (vec args)}))


(def Add (makeOperation '+ + #(apply Add %2)))

(def Subtract (makeOperation '- - #(apply Subtract %2)))

(def Negate (makeOperation 'negate - #(apply Negate %2)))

(comment ":NOTE: copy-paste (at least call `diffEach` of operands in each declaration)")
(comment ":NOTE: diffAtInd is not a solution, you sill call diff in each declaration but in other function")
(def Multiply (makeOperation '* * #(Add (Multiply (%1 0) (%2 1))
                                        (Multiply (%1 1) (%2 0)))))

(def Divide (makeOperation '/ #(/ %1 (double %2))
                           #(Divide (Subtract (Multiply (%1 1) (%2 0))
                                              (Multiply (%1 0) (%2 1)))
                                    (Multiply (%1 1) (%1 1)))))

(def Sum (makeOperation 'sum + #(apply Sum %2)))

(def Avg (makeOperation 'avg #(/ (apply + %&) (double (count %&)))  #(Divide (apply Sum %2) (Constant (count %1)))))

(def Square (makeOperation 'square #(* % %) #(Multiply TWO (%1 0) (%2 0))))

(def Pow (makeOperation '** #(Math/pow (double %1) (double %2)) (fn [x y] ONE)))

(def Log (makeOperation "//" #(/ (Math/log (Math/abs %2)) (double (Math/log (Math/abs %1)))) nil))

(def Sqrt
  (makeOperation 'sqrt #(Math/sqrt (Math/abs %))
                 #(Multiply (Sqrt (Square (%1 0))) (%2 0) (Divide ONE (Multiply (%1 0) TWO (Sqrt (%1 0)))))))

(def objectOperations {
                       '+      Add
                       '-      Subtract
                       (symbol "**") Pow
                       (symbol "//") Log
                       '*      Multiply
                       '/      Divide
                       'negate Negate
                       'sqrt Sqrt
                       'square Square
                       'sum Sum
                       'avg Avg})

(defn parseObjectExpression [expr]
      (cond
        (seq? expr) (apply (objectOperations (first expr)) (mapv parseObjectExpression (rest expr)))
        (number? expr) (Constant expr)
        :else (Variable (str expr))))

(def parseObject (comp parseObjectExpression read-string))

; HW 12 easy /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
      (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result)))) "!"))
(defn tabulate [parser inputs]
      (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))


(defn _empty [value] (partial -return value))

(defn _char [p]
      (fn [[c & cs]]
          (if (and c (p c)) (-return c cs))))

(defn _map [f result]
      (if (-valid? result)
        (-return
          (f (-value result))
          (-tail result))))

(defn _combine [f a b]
      (fn [str]
          (let [ar ((force a) str)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar)))))))

(defn _either [a b]
      (fn [str]
          (let [ar ((force a) str)]
               (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
      (fn [input]
          (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))


(defn +string [p]
      (fn [[c & cs]]
          (if (and c (p c)) (-return c cs))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
      (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
      (reduce (partial _combine iconj) (_empty []) ps))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
      (reduce _either p ps))

(defn +opt [p]
      (+or p (_empty nil)))

(defn +star [p]
      (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))

(defn +plus [p] (+seqf cons p (+star p)))

(defn +str [p] (+map (partial apply str) p))
(defn +string [st] (apply +seq (mapv #(+char (str %)) st)))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+plus *digit))))
(def *double (+map read-string (+seqf str (+opt (+char "-")) *number (+opt (+seqf str (+char ".") *number)))))

(def *string (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(defn _str [args] (apply +or (mapv #(+string (str %)) args)))

(def OPERATIONS {
                 '+ Add
                 '- Subtract
                 (symbol "**") Pow
                 (symbol "//")  Log
                 '* Multiply
                 '/ Divide
                 (symbol "negate") Negate})

(def *Const (+seqf #(Constant %) *double))
(def *Opers (+seqf  #(get OPERATIONS (symbol (apply str %))) (_str (keys OPERATIONS))))
(def *Vars (+seqf #(Variable (apply str %)) (_str ['x 'y 'z])))
(def *Brackets (+seqf #(apply (nth % 1) (nth % 0)) (+seq (+ignore (+char "(")) *ws
                                                         (+plus (+seqf identity (+or *Const *Vars (delay *Brackets)) *ws))
                                                         *Opers *ws (+ignore (+char ")")))))

(defn parseObjectSuffix [expr] (nth (-value ((+seq *ws (+or *Const *Vars *Brackets) *ws) expr)) 0))

