(defn isVector [a]
      (reduce (fn [a b] (and a (number? b))) true a))

(defn transpose [a]
      (if (isVector a)
      (mapv (fn [x] [x]) a)
      (apply mapv vector a))
)

(defn applyOperation [op args]
      (reduce (fn [a b]
            {:pre [(= (count a) (count b)) (= (count (transpose a)) (count (transpose b)))
                   (identical? (type a) (type b)) (identical? (type a) (type []))]
             :post [(= (count %) (count b)) (= (count (transpose %)) (count (transpose b)))]}
            (mapv op a b)) args)
)

(defn v+ [& a] (applyOperation + a))

(defn v* [& a] (applyOperation * a))

(defn v*s [a & b]
      (reduce (fn [a b]
            (mapv (fn [x] (* x b)) a)) a b)
)

(defn v- [& a]
      (if (empty? (rest a))
      (v*s (first a) -1)
      (applyOperation - a))
)

(defn scalar [a & b]
      (reduce (fn [a b] (apply + (v* a b))) a b)
)

(defn det3 [a b n1 n2]
      (- (* (nth a n1) (nth b n2)) (* (nth a n2) (nth b n1)))
)

(defn vect [a & b] (reduce (fn [a b]
           {:pre [(= (count b) (count a)) (identical? (type a) (type b)) (identical? (type a) (type [])) ()]
            :post [(= (count %) (count b))]}
           (vector (det3 a b 1 2) (- (det3 a b 0 2)) (det3 a b 0 1))) a b)
)

(defn m+ [& a]
      (applyOperation v+ a)
)

(defn m*s [a & b]
      (reduce (fn [a b] (mapv (fn [x] (v*s x b)) a)) a b)
)

(defn m- [& a]
      (if (empty? (rest a))
      (m*s (first a) -1)
      (applyOperation v- a))
)

(defn m* [& a]
      (applyOperation v* a)
)

(defn m*v [a & b]
      (reduce (fn [a b]
            {:pre [(= (count (transpose a)) (count b))]
             :post [(= (count %) (count a)) (identical? (type %) (type b)) (identical? (type %) (type []))]}
            (mapv (fn [x] (scalar x b)) a)) a b)
)

(defn m*m [a & b]
      (reduce (fn [a b]
          {:pre [(= (count (transpose a)) (count b))]
           :post [(= (count %) (count a)) (= (count (transpose %)) (count (transpose b)))]}
          (mapv (fn [x] (m*v (transpose b) x)) a)) a b)
)


(defn Shapeless [f & args]
      (if (number? (first args))
      (apply f args)
      (apply mapv (partial Shapeless f) args))
)

(defn s+ [& args] (apply (partial Shapeless +) args))
(defn s- [& args] (apply (partial Shapeless -) args))
(defn s* [& args] (apply (partial Shapeless *) args))
