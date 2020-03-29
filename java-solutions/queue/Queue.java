package queue;

import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Tests failed
 * 
Running queue.QueueToArrayTest for LinkedQueue in CLASS mode
        === testEmpty
        === testSingleton
        === testClear
        === testRandom, add frequency = 0.0
        === testRandom, add frequency = 0.2
        === testRandom, add frequency = 0.4
        === testRandom, add frequency = 0.6
        === testRandom, add frequency = 0.8
        === testRandom, add frequency = 1.0
===========================================
Test run: 674878, passed: 674878, failed: 0
Finished in 6986 ms
Version: QueueToArrayTest, 12.03.2020 12:07:32
Running queue.QueueToArrayTest for ArrayQueue in CLASS mode
        === testEmpty
        === testSingleton
        === testClear
        === testRandom, add frequency = 0.0
        === testRandom, add frequency = 0.2
Exception in thread "main" java.lang.AssertionError: element():
     expected `[a]`,
       actual `null`
        at base.Asserts.assertTrue(Unknown Source)
        at base.Asserts.assertEquals(Unknown Source)
        at queue.ReflectionTest.checkResult(Unknown Source)
        at queue.ArrayQueueTest.checkResult(Unknown Source)
        at queue.ReflectionTest.lambda$checking$0(Unknown Source)
        at queue.$Proxy0.element(Unknown Source)
        at queue.ArrayQueueTest.check(Unknown Source)
        at queue.ArrayQueueTest.checkAndSize(Unknown Source)
        at queue.ArrayQueueTest.testRandom(Unknown Source)
        at queue.ArrayQueueTest.test(Unknown Source)
        at queue.ArrayQueueTest.test(Unknown Source)
        at queue.QueueTest.test(Unknown Source)
        at queue.QueueToArrayTest.main(Unknown Source)
 */

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
