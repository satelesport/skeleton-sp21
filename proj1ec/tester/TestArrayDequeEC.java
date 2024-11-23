package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;
import static org.junit.Assert.*;

public class TestArrayDequeEC {
    @Test
    public void endlessTest() {
        StudentArrayDeque<Integer> d1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> d2 = new ArrayDequeSolution<>();

        while (true) {
            int situation = StdRandom.uniform(0,5);
            if (situation == 0){
                int item = StdRandom.uniform(0,100);
                d1.addFirst(item);
                d2.addFirst(item);
                System.out.println("addFirst(" + item +")");
            }
            if (situation == 1) {
                int item = StdRandom.uniform(0,100);
                d1.addLast(item);
                d2.addLast(item);
                System.out.println("addLast(" + item +")");
            }
            if (situation == 2) {
                if (!d2.isEmpty() && !d1.isEmpty()) {
                    Integer item1 = d1.removeFirst();
                    Integer item2 = d2.removeFirst();
                    org.junit.Assert.assertEquals("RemoveFirst(), student was " + item1 + ", correct was " + item2, item2, item1);
                    System.out.println("RemoveFirst()");
                }
            }
            if (situation == 3) {
                if (!d2.isEmpty() && !d1.isEmpty()) {
                    Integer item1 = d1.removeLast();
                    Integer item2 = d2.removeLast();
                    org.junit.Assert.assertEquals("RemoveLast(), student was " + item1 + ", correct was " + item2, item2, item1);
                    System.out.println("RemoveLast()");
                }
            }
            if (situation == 4) {
                if (!d2.isEmpty()) {
                    Integer item1 = d1.get(0);
                    Integer item2 = d2.get(0);
                    org.junit.Assert.assertEquals("get(0), student was " + item1 + " correct was " + item2, item2, item1);
                    System.out.println("get(0)");
                }
            }
        }
    }
}
