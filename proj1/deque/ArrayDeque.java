package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {

    /*
        remember the write the resize method
        be careful with the first and end
     */

    private T[] array = (T[])new Object[8];
    private int size;
    //first point to the first number, end point to the last number
    private int first;
    private int end;

    public ArrayDeque() {
        size = 0;
        first = 0;
        end = 0;
    }

    public int getFirst() {
        return first;
    }

    public ArrayDeque(T x) {
        first = 0;
        end = 0;
        array[end] = x;
        size = 1;
    }

    public int size() {
        return size;
    }

    public void resize(int newSize) {
        T[] newArray = (T[])new Object[newSize];
        for(int i = 0; i < size; i++) {
            int pos = (first + i) % array.length;
            newArray[i] = array[pos];
        }
        array = newArray;
        first = 0;
        end = size - 1;
    }

    public void addFirst(T item) {
        if(size == array.length - 1) {
            resize((int) (1.5 * size));
        }
        if(size == 0) {
            first = 0;
            end = 0;
            array[first] = item;
            size += 1;
            return;
        }
        size += 1;
        first = (array.length + first - 1) % array.length;
        array[first] = item;
    }

    public void addLast(T item) {
        if(size == array.length - 1) {
            resize((int) (1.5*size));
        }
        if(size == 0) {
            first = 0;
            end = 0;
            array[first] = item;
            size += 1;
            return;
        }
        size += 1;
        end = (end + 1 + array.length) % array.length;
        array[end] = item;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T removeFirst() {
        if(size == 0) {
            return null;
        }
        if(size < array.length / 4){
            resize(array.length / 4);
        }
        size -= 1;

        T returnItem = array[first];
        first = (first + array.length + 1) % array.length;
        return returnItem;
    }

    public T removeLast() {
        if(size == 0) {
            return null;
        }
        if(size < array.length / 4 && array.length > 16) {
            resize(array.length / 4 + 1);
        }
        size -= 1;

        T returnItem = array[end];
        end = (end + array.length - 1) % array.length;
        return returnItem;
    }

    public T get(int index) {
        if(index > size - 1) {
            return null;
        }
        int pos = (first + index) % array.length;
        return array[pos];
    }

    public void printDeque() {
        for(int i=0;i<size;i++) {
            int pos = (first + i) % array.length;
            System.out.print(array[pos]+" ");
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ArrayDeque) {
            ArrayDeque object = (ArrayDeque) o;

            if(object.size != size) {
                return false;
            }

            int first2 = object.getFirst();
            for(int i = 0;i < size;i ++){
                if(object.get(i).equals(get(i))){
                    continue;
                }
                else return false;
            }
            return true;
        }
        else return false;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int nowPos;

        public ArrayDequeIterator() {
            nowPos = first;
        }

        public boolean hasNext() {
            return nowPos != end + 1;
        }

        public T next() {
            T returnItem = array[nowPos];
            nowPos = (nowPos + 1 + array.length) % array.length;
            return returnItem;
        }
    }
}
