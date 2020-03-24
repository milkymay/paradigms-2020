package queue;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int start;
    private int end;
    private Object[] elements = new Object[4];

    // :NOTE: copy-paste of code for calculating current head/tail
    private int inc(int val) {
        return (val + 1) % elements.length;
    }

    //pre: element != null
    public void doEnqueue(Object element) {
        if (size == elements.length) {
            ensureCapacity();
        }
        elements[end] = element;
        end = inc(end);
        // :NOTE: ensure capacity after enqueue?
    }
    //post: for i = 0..size-1 elements[i] = elements[i]' && elements[size] = element && size = min(size'+1, possible_size)
    //start and ens are valid (end++)

    //pre: start != end
    public Object doDequeue() {
        Object value = element();
        elements[start] = null;
        start = inc(start);
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
        start = 0;
        end = 0;
        elements = new Object[4];
    }
    //post: elements is an empty array of nulls with start = end = elements.length - 1 && size = 0

    private void ensureCapacity() {
        Object[] queue = new Object[2 * elements.length];
        elements = copy(queue);
        start = 0;
        end = size();
    }

    //pre: true
    // :NOTE: by-hand copy of elements in this override?
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        return size() == 0 ? arr : copy(arr);
    }
    //post: arr[queue.size()]: arr[0] = queue[start], arr[1] = queue[start + 1] ... arr[size - 1] = queue[end];
    //queue is the same

    private Object[] copy(Object[] dest) {
        int len = (start < end ? end : elements.length) - start ;
        System.arraycopy(elements, start, dest,0, len);
        System.arraycopy(elements, 0, dest, len, size() - len);
        return dest;
    }

    public Queue create() {
        return new ArrayQueue();
    }
}
