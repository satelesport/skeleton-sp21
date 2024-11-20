package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    AListNoResizing<Integer> list1=new AListNoResizing<>();
    BuggyAList<Integer> list2=new BuggyAList<>();
    int n=10;
    @Test
    public void testThreeAddThreeRemove(){
        list1.addLast(4);
        list1.addLast(5);
        list1.addLast(6);

        list2.addLast(4);
        list2.addLast(5);
        list2.addLast(6);

        for(int i=1;i<=3;i++){
            org.junit.Assert.assertEquals(list1.removeLast(),list2.removeLast());
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> testL = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                testL.addLast(randVal);
            }
            else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeTest = testL.size();
                org.junit.Assert.assertEquals(size,sizeTest);
            }
            else if(operationNumber == 2){
                if(L.size()==0) break;
                int l1 = L.getLast();
                int l2 = testL.getLast();
                org.junit.Assert.assertEquals(l1,l2);
            }
            else if(operationNumber == 3){
                if(L.size()==0) break;
                int l1 = L.removeLast();
                int l2 = testL.removeLast();
                org.junit.Assert.assertEquals(l1,l2);
            }
        }
    }
}
