package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cm;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        cm = c;
    }
     public T max() {
        if (size() == 0) {
            return null;
        }
        T returnItem = get(0);
        for (int i = 1; i < size(); i++) {
            if (cm.compare(returnItem, get(i)) < 0) {
                returnItem = get(i);
            }
        }
        return returnItem;
     }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T returnItem = get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(returnItem, get(i)) < 0) {
                returnItem = get(i);
            }
        }
        return returnItem;
    }
}
