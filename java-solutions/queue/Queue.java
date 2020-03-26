package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    //pre: true
    void enqueue(Object element);
    //post: size' = size + 1; the element is added to the end of queue

    //pre: size > 0;
    Object element();
    //post: queue is not changed; R: the first element of the queue

    //pre: size > 0;
    Object dequeue();
    //post: size' = size - 1; the first element is removed

    //pre: true
    int size();
    //post: R = size

    //pre: true
    boolean isEmpty();
    //post: R = true if size = 0 or false otherwise

    //pre: true
    void clear();
    //post: queue is empty; size = 0

    //pre: predicate != null
    Queue filter(Predicate<Object> p);
    //R: new ArrayQueue q: q.elements is this.elements subset; p.test(q.elements[0..q.size-1]) = true;
    //p.test(this/q.elements[i]) = false; order is saved;

    //pre: f != null
    Queue map(Function<Object, Object> f);
    //R: new ArrayQueue q: q.elements; q.elements[0..q.size-1] = f.apply(elements[0..q.size-1]);
    //order is saved; q.elements.length = this.elements.length
}
