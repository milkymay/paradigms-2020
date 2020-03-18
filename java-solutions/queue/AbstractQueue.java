package queue;

public abstract class AbstractQueue implements Queue{
    protected int size;

    //pre: true
    protected abstract void doEnqueue(Object element);
    //post: size' = size + 1; the element is added to the end of queue
    //pre: size > 0;
    protected abstract Object doDequeue();
    //post: size' = size - 1; the first element is removed
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
        size++;
        doEnqueue(element);
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
    //pre: size > 0
    public Object element() {
        assert size > 0;
        return el();
    }
    //post: queue not changed; the last element returned as R
    //pre: true
    public void clear() {
        doClear();
        size = 0;
    }
    //post: queue is empty; size' = 0;
}
