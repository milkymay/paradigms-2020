;review

(defn checkSizes [args]
      (every? #(== (count (first args)) (count %)) args))

(defn checkVectors [vectors]
      (every? #(and (vector? %) (every? number? %)) vectors))

(defn checkMatrix [matrix]
      (every? #(and (vector? %) (and (checkVectors %) (checkSizes %))) matrix))

(defn transpose [a]
      (apply mapv vector a))

(defn abstractVectorOperation [f & args]
      {:pre [(and (checkVectors args) (checkSizes args)) ]}
      (apply mapv f args))

(defn abstractMatrixOperation [f & args]
      {:pre [(checkMatrix args) (checkSizes args)]}
      (apply mapv f args))

(def v+ (partial abstractVectorOperation +))
(def v* (partial abstractVectorOperation *))
(def v- (partial abstractVectorOperation -))

(defn v*s [a & b]
      (reduce (fn [a b] {:pre [(number? b)]}
            (mapv #(* % b) a)) a b))

(defn scalar [& a]
      {:pre [(checkVectors a) (checkSizes a)]}
      (apply + (apply v* a)))

(defn det3 [a b n1 n2]
      (- (* (nth a n1) (nth b n2)) (* (nth a n2) (nth b n1))))

(defn vect [& a] {:pre [(checkVectors a) (checkSizes a)]}
      (reduce #(vector (det3 %1 %2 1 2) (- (det3 %1 %2 0 2)) (det3 %1 %2 0 1)) a))

(defn m*s [a & b]
      (reduce (fn [a b] {:pre [(number? b)]}
            (mapv #(v*s % b) a)) a b))

(def m+ (partial abstractMatrixOperation v+))
(def m- (partial abstractMatrixOperation v-))
(def m* (partial abstractMatrixOperation v*))

(defn m*v [a & b]
      (reduce (fn [a b]
            {:pre [(= (count (transpose a)) (count b))]}
            (mapv #(scalar % b) a)) a b))

(defn m*m [a & b]
      (reduce (fn [a b]
          {:pre [(= (count (transpose a)) (count b))]}
          (mapv #(m*v (transpose b) %) a)) a b))

(defn abstractShapelessOperation [f & args]
      (if (number? (first args))
      (apply f args)
      (apply mapv (partial abstractShapelessOperation f) args)))

(defn s+ [& args] (apply (partial abstractShapelessOperation +) args))
(defn s- [& args] (apply (partial abstractShapelessOperation -) args))
(defn s* [& args] (apply (partial abstractShapelessOperation *) args))
