package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    private class Pointer {
        public T value;
        public Pointer next;
        public Pointer before;
        public Pointer() {
            value = null;
            next = null;
            before = null;
        }
        public Pointer(T x) {
            value = x;
            next = null;
            before = null;
        }
    }

    private Pointer first;
    private Pointer end;
    private int size;

    public LinkedListDeque() {
        first = new Pointer();
        end = new Pointer();
        first.next = end;
        end.before = first;
        size = 0;
    }

    public void addFirst(T item) {
        size += 1;
        Pointer newItem = new Pointer(item);
        Pointer temp = first.next;
        first.next = newItem;
        newItem.next = temp;

        newItem.before = first;
        temp.before = newItem;
    }

    public void addLast(T item) {
        size += 1;
        Pointer newItem = new Pointer(item);
        Pointer temp = end.before;
        temp.next = newItem;
        newItem.next = end;

        newItem.before = temp;
        end.before = newItem;
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Pointer temp = first.next.next;
        Pointer removeItem = first.next;
        T removeItemValue = first.next.value;

        first.next = temp;
        temp.before = first;

        removeItem.before = removeItem.next = null;
        return removeItemValue;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Pointer temp = end.before.before;
        Pointer removeItem = end.before;
        T removeItemValue = removeItem.value;

        end.before = temp;
        temp.next = end;

        removeItem.before = removeItem.next = null;
        return removeItemValue;
    }

    public T get(int index) {
        if (index > size - 1) {
            return null;
        }

        Pointer temp = first.next;
        int nowIndex = 0;
        while (nowIndex < index) {
            temp = temp.next;
            nowIndex++;
        }
        return temp.value;
    }

    public T getRecursive(int index) {
        return get(index);
    }

    public void printDeque() {
        Pointer temp = first.next;
        while (temp != end) {
            System.out.print(temp.value + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int nowPos;
        private Pointer nowPointer;

        public LinkedListDequeIterator() {
            nowPos = 0;
            nowPointer = first.next;
        }

        public boolean hasNext() {
            return nowPos < size;
        }

        public T next() {
            T returnvalue = nowPointer.value;
            nowPointer = nowPointer.next;
            nowPos += 1;
            return returnvalue;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque) {
            LinkedListDeque object = (LinkedListDeque) o;

            if (object.size() != size) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (object.get(i).equals(get(i))) {
                    continue;
                }
                else {
                    return false;
                }
            }
            return true;
        }
        else if (o instanceof ArrayDeque) {
            ArrayDeque object = (ArrayDeque) o;

            if (object.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (object.get(i).equals(get(i))) {
                    continue;
                }
                else {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
}
