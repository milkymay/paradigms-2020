;review

(defn checkSizes [args]
      (every? #(== (count (first args)) (count %)) args))

(defn checkVectors [vectors]
      (every? #(and (vector? %) (every? number? %)) vectors))

(defn checkMatrix [matrix]
      (every? #(and (vector? %) (and (checkVectors %) (checkSizes %))) matrix))

(defn checkTensors [tensors]
      (every? #(or (and (vector? %) (every? number? %)) (and (vector? %) (checkTensors %))) tensors))

(defn transpose [a]
      {:pre [(or (number? a) (vector? a))]}
      (apply mapv vector a))

(defn abstractVectorOperation [f]
      (fn [& args] {:pre [(and (checkVectors args) (checkSizes args))]} (apply mapv f args)))

(defn abstractMatrixOperation [f]
      (fn [& args] {:pre [(checkMatrix args) (checkSizes args)]} (apply mapv f args)))

(def v+ (abstractVectorOperation +))
(def v* (abstractVectorOperation *))
(def v- (abstractVectorOperation -))

(defn v*s [a & b] {:pre [(every? number? b) (vector? a)]}
      (let [scal (apply * b)] (mapv #(* % scal) a)))

(defn scalar [& a] {:pre [(checkVectors a)]}
      (apply + (apply v* a)))

(defn det3 [a b n1 n2]
      {:pre [(vector? a) (vector? b) (number? n1) (number? n2)]}
      (- (* (nth a n1) (nth b n2)) (* (nth a n2) (nth b n1))))

(defn vect [& a]
      {:pre [(checkVectors a) (checkSizes a)]}
      (reduce #(vector (det3 %1 %2 1 2) (- (det3 %1 %2 0 2)) (det3 %1 %2 0 1)) a))

(defn m*s [a & b]
      (let [scal (apply * b)] (mapv #(v*s % scal) a)))

(def m+ (abstractMatrixOperation v+))
(def m- (abstractMatrixOperation v-))
(def m* (abstractMatrixOperation v*))

(defn m*v [a & b]
      (reduce (fn [a b]
            {:pre [(= (count (transpose a)) (count b))]}
            (mapv #(scalar % b) a)) a b))

(defn m*m [a & b]
      (reduce (fn [a b]
          {:pre [(checkMatrix [a]) (checkMatrix [b]) (= (count (transpose a)) (count b)) ]}
          (mapv #(m*v (transpose b) %) a)) a b))

(defn abstractShapelessOperation [f]
      (fn [& args]
          {:pre [(or (every? number? args) (and (every? vector? args) (checkSizes args)))]}
          (if (number? (first args))
                (apply f args)
                (apply mapv (abstractShapelessOperation f) args))))

(def s+ (abstractShapelessOperation +))
(def s- (abstractShapelessOperation -))
(def s* (abstractShapelessOperation *))

(defn checkTensor [tensor]
      (or (every? number? tensor)
          (and (every? vector? tensor) (and (checkSizes tensor) (every? checkTensor (apply mapv vector tensor))))))

(defn checkRec [f & tensors]
      {:pre [(or (number? (first tensors)) (checkSizes tensors))]}
      (if (vector? (first tensors))
            (apply mapv (partial checkRec f) tensors)
            (apply f tensors)))

(defn abstractTensorOperation [f]
      (fn [& args]
          {:pre [(checkTensor (first args))]}
          (apply checkRec f args)))

(def t+ (abstractTensorOperation +))
(def t- (abstractTensorOperation -))
(def t* (abstractTensorOperation *))