package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    //pre: true
    void enqueue(Object element);
    //post: size' = size + 1; the element is added to the end of queue
    //pre: size > 0;
    Object dequeue();
    //post: size' = size - 1; the first element is removed
    //pre: size > 0;
    Object element();
    //post: queue is not changed; R: the first element of the queue
    //pre: true
    Queue filter(Predicate<Object> p);
    Queue map(Function<Object, Object> f);
    //pre: true
    int size();
    //post: R: size
    //pre: true
    boolean isEmpty();
    //post: true if queue is empty, false otherwise
    //pre: true
    void clear();
    //post: size = 0;
}
