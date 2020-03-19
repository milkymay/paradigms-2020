package queue;

public class LinkedQueue extends AbstractQueue implements Queue {
    private Node head;
    private Node tail;

    //I: head and tail are valid; (if queue if empty head = tail)

    //pre: true
    public void doEnqueue(Object element) {
        if (size == 1) {
            tail = new Node(element, null);
            head = tail;
        } else {
            tail.next = new Node(element, null);
            tail = tail.next;
        }
    }
    //post: queue is not empty; tail = new Node(element, null);

    //pre: size > 0
    public Object doDequeue() {
        Object result = head.value;
        head = head.next;
        return result;
    }
    //post: R: the last element of queue; size' = size - 1; head' = head.next;

    //pre: size > 0
    public Object el() {
        return head.value;
    }
    //post: R: the last element of queue; queue is the same

    //pre: true
    public void doClear() {
        head = new Node(0, null);
        tail = new Node(0, null);
    }
    //post: the queue is cleared; size = 0; head = tail;

    public Queue create() {
        return new LinkedQueue();
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    //pre: true
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node cur = head;
        for (int i = 0; i < size; i++) {
            arr[i] = cur.value;
            cur = cur.next;
        }
        return arr;
    }
    //post: queue is presented as an array linked from start to end
}
