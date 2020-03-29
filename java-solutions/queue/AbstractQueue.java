package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue{
    protected int size;

    //pre: true
    protected abstract void doEnqueue(Object element);
    //post: the element is added to the end of queue
    //pre: size > 0;
    protected abstract Object doDequeue();
    //post: the first element is removed
    //pre: size > 0;
    protected abstract Object el();
    //post: queue is not changed; R: the first element of the queue
    //pre: true
    protected abstract void doClear();
    //post: size = 0;
    //pre: size > 0
    public Object dequeue() {
        assert size > 0;
        Object res = doDequeue();
        size--;
        return res;
    }
    //post: size' = size - 1; the first element is removed from the queue (replaced with null)
    //pre: element != null;
    public void enqueue(Object element) {
        assert element != null;
        doEnqueue(element);
        size++;
    }
    //post: size' = size + 1; element is added to the end of queue
    //pre: true
    public int size() {
        return size;
    }
    //post: R = size
    //pre: true
    public boolean isEmpty() { return size == 0; }
    //post: R = true if size = 0 or false otherwise
    //pre: true
    public abstract Object[] toArray();
    //post: R: queue as an Array, order is saved, all elements copied, array.size = queue.size
    //pre: true
    public abstract Queue create();
    //post: R: new empty Queue
    //pre: size > 0
    public Object element() {
        assert size > 0;
        return el();
    }
    //post: R: the first element of queue; queue is not changed
    //pre: true
    public void clear() {
        doClear();
        size = 0;
    }
    //post: queue is empty; size = 0
    //pre: correct predicate
    @Override
    public Queue filter(Predicate<Object> predicate) {
        return map((x) -> (predicate.test(x)) ? x : null);
    }
    //R: new ArrayQueue q: q.elements is this.elements subset; p.test(q.elements[0..q.size-1]) = true;
    //p.test(this/q.elements[i]) = false; order is saved;

    //pre: true
    @Override
    public Queue map(Function<Object, Object> function) {
        Object[] temp = toArray();
        Queue queue = create();
        for (Object e: temp) {
            if (function.apply(e) != null) {
                queue.enqueue(function.apply(e));
            }
        }
        return queue;
    }
    //R: new ArrayQueue q: q.elements; q.elements[0..q.size-1] = f.apply(elements[0..q.size-1]);
    //order is saved; q.elements.length = this.elements.length

}
