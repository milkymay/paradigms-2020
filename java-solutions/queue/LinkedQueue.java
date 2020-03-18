package queue;

import java.util.function.Function;
import java.util.function.Predicate;

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

    @Override
    public Queue filter(Predicate p) {
        LinkedQueue q = new LinkedQueue();
        Node cur = head;
        int cnt = 0;
        for (int i = size - 1; i >= 0; i--) {
            if (p.test(cur.value)) {
                q.enqueue(cur.value);
                cnt++;
            }
            cur = cur.next;
        }
        q.size = cnt;
        return q;
    }

    @Override
    public Queue map(Function f) {
        LinkedQueue q = new LinkedQueue();
        Node cur = head;
        for (int i = size - 1; i >= 0; i--) {
            q.enqueue(f.apply(cur.value));
            cur = cur.next;
        }
//        q.size = cnt;
        return q;
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
