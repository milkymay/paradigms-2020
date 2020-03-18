package queue;

public class LinkedQueueTestLocal {
    public static void fill(LinkedQueue queue) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
    }

    public static void dump(LinkedQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.size() + " " +
                queue.element() + " " + queue.dequeue());
        }
    }

    public static void printArray(LinkedQueue queue) {
        Object[] arr = queue.toArray();
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LinkedQueue queue = new LinkedQueue();
        fill(queue);
        printArray(queue);
        dump(queue);
    }
}
