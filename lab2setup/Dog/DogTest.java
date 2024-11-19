package Dog;

import static org.junit.Assert.*;
import org.junit.Test;
//import org.junit.jupiter.api.Test;

public class DogTest {
    @org.junit.jupiter.api.Test
    public void testSmall() {
        Dog d = new Dog(3);
        assertEquals("yip", d.noise());
    }

    @Test
    public void testLarge() {
        Dog d = new Dog(20);
        assertEquals("bark", d.noise());
    }
}
