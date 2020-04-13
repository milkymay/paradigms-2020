(defn v+ [& a] (reduce (fn [a b]
      {:pre [(= (count a) (count b))]
      :post [(= (count %) (count b))]}
      (mapv + a b)) a))

(defn v* [& a] (reduce (fn [a b]
      {:pre [(= (count a) (count b))]
      :post [(= (count %) (count b))]}
      (mapv * a b)) a))

(defn v*s [a & b] (reduce (fn [a b] (mapv (fn [x] (* x b)) a)) a b))

(defn v- [& a] (if (empty? (rest a))
                 (v*s (first a) -1)
                 (reduce (fn [a b]
                      {:pre [(= (count a) (count b))]
                       :post [(= (count %) (count b))]}
                       (mapv - a b)) a)))

(defn scalar [a & b] (reduce (fn [a b] (apply + (v* a b))) a b))

(defn det3 [a b n1 n2] (- (* (nth a n1) (nth b n2)) (* (nth a n2) (nth b n1))))

(defn vect [a & b] (reduce (fn [a b]
      {:pre [(= (count a) (count b))]
      :post [(= (count %) (count b))]}
      (vector (det3 a b 1 2) (- (det3 a b 0 2)) (det3 a b 0 1))) a b))

(defn transpose [a] (apply mapv vector a))

(defn m+ [a & b] (reduce (fn [a b]
      {:pre [(and (= (count a) (count b)) (= (count (transpose a)) (count (transpose b))))]
      :post [(and (= (count %) (count b)) (= (count (transpose %)) (count (transpose b))))]}
      (mapv v+ a b)) a b))

(defn m*s [a & b] (reduce (fn [a b] (mapv (fn [x] (v*s x b)) a)) a b))

(defn m- [a & b] (if (empty? b)
                   (m*s a -1)
                   (reduce (fn [a b]
                        {:pre [(and (= (count a) (count b)) (= (count (transpose a)) (count (transpose b))))]
                         :post [(and (= (count %) (count b)) (= (count (transpose %)) (count (transpose b))))]}
                        (mapv v- a b)) a b)))

(defn m* [a & b] (reduce (fn [a b] (mapv v* a b)) a b))

(defn m*v [a & b] (reduce (fn [a b]
      {:pre [(= (count (transpose a)) (count b))]
       :post [(and (= (count %) (count a)) (identical? (type %) (type b)) (identical? (type %) (type [])))]}
      (mapv (fn [x] (scalar x b)) a)) a b))

(defn m*m [a & b] (reduce (fn [a b]
      {:pre [(= (count (transpose a)) (count b))]
       :post [(and (= (count %) (count a)) (= (count (transpose %)) (count (transpose b))))]}
      (mapv (fn [x] (m*v (transpose b) x)) a)) a b))
	


