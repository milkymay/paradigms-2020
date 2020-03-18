package queue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int start = 3;
    private int end = 3;
    private Object[] elements = new Object[4];

    //pre: element != null
    public void doEnqueue(Object element) {
        elements[end--] = element;
        if (end < 0) { end = elements.length - 1;}
        end = (end + elements.length) % elements.length;
        if (size == elements.length) {
            ensureCapacity();
        }
    }
    //post: for i = 0..size-1 elements[i] = elements[i]' && elements[size] = element && size = min(size'+1, possible_size)
    //start and ens are valid (end--)

    //pre: start != end
    public Object doDequeue() {
        Object value = element();
        elements[start--] = null;
        start = (start + elements.length) % elements.length;
        return value;
    }
    //post: for i = 0..size-2 elements[i] = elements[i]' && elements[size-1] = null && size = size' - 1;

    //pre: start != end;
    public Object el() {
        return elements[start];
    }
    //post: for i = 0..size-1 elements[i] = elements[i]', start = start'

    //pre: true
    public void doClear() {
        start = elements.length - 1;
        end = start;
        Arrays.fill(elements, null);
    }
    //post: elements is an empty array of nulls with start = end = elements.length - 1 && size = 0

    private void ensureCapacity() {
        Object[] queue = new Object[2 * elements.length];
        int sz = elements.length;
        System.arraycopy(elements, 0, queue,sz * 2 - 1 - start, start + 1);
        System.arraycopy(elements, start + 1, queue, sz, sz - start - 1);
        start = queue.length - 1;
        end = start - sz;
        elements = queue;
    }

    //pre: true
    public Object[] toArray() {
        int size = size(), length = elements.length;
        Object[] arr = new Object[size];
        int cur = start;
        for (int i = 0; i < size; i++) {
            arr[i] = elements[cur];
            cur = (cur - 1 + length) % length;
        }
        return arr;
    }
    //post: arr[queue.size()]: arr[0] = queue[start], arr[1] = queue[start + 1] ... arr[size - 1] = queue[end];
    //queue is the same

    //pre: correct predicate
    @Override
    public Queue filter(Predicate<Object> p) {
        int cur = start, len = elements.length;
        ArrayQueue q = new ArrayQueue();
        for (int i = 0; i < size; i++) {
            if (p.test(elements[cur])) {
                q.enqueue(elements[cur]);
            }
            cur = (cur - 1 + len) % len;
        }
        return q;
    }
    //R: new ArrayQueue q: q.elements is this.elements subset; p.test(q.elements[0..q.size-1]) = true;
    //p.test(this/q.elements[i]) = false; order is saved;

    //pre: true
    @Override
    public Queue map(Function<Object, Object> f) {
        int cur = start, len = elements.length;
        ArrayQueue q = new ArrayQueue();
        for (int i = 0; i < size; i++) {
            q.enqueue(f.apply(elements[cur]));
            cur = (cur - 1 + len) % len;
        }
        return q;
    }
    //R: new ArrayQueue q: q.elements; q.elements[0..q.size-1] = f.apply(elements[0..q.size-1]);
    //order is saved; q.elements.length = this.elements.length
}
