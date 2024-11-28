package bstmap;

import java.security.Key;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K, V> {
    private static class BSTNode<K extends Comparable<K>,V>{
        private K key;
        private V value;
        private BSTNode<K,V> leftChild;
        private BSTNode<K,V> rightChild;

        public BSTNode(){
            value = null;
            leftChild = rightChild =null;
        }

        public BSTNode(K k, V v){
            key = k;
            value = v;
            leftChild = rightChild =null;
        }
    }

    private BSTNode<K,V>  head;
    private int size;

    public BSTMap(){
        head = new BSTNode<>();
        size = 0;
    }

    @Override
    public void clear() {
        head = new BSTNode<>();
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        BSTNode<K,V> node = head.leftChild;
        while(node != null){
            if(node.key.compareTo(key) == 0){
                return true;
            }
            if(node.leftChild == null && node.rightChild != null){
                node = node.rightChild;
                continue;
            }
            if(node.leftChild != null && node.rightChild == null){
                node = node.leftChild;
                continue;
            }
            if(node.leftChild == null && node.rightChild == null){
                return false;
            }
            if(node.leftChild != null && node.rightChild != null){
                if(key.compareTo(node.key) < 0){
                    node = node.leftChild;
                }
                else{
                    node = node.rightChild;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        BSTNode<K,V> node = head.leftChild;
        while(node != null){
            if(node.key.compareTo(key) == 0){
                return node.value;
            }
            if(node.leftChild == null && node.rightChild != null){
                node = node.rightChild;
                continue;
            }
            if(node.leftChild != null && node.rightChild == null){
                node = node.leftChild;
                continue;
            }
            if(node.leftChild == null && node.rightChild == null){
                return null;
            }
            if(node.leftChild != null && node.rightChild != null){
                if(key.compareTo(node.key) < 0){
                    node = node.leftChild;
                }
                else{
                    node = node.rightChild;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if(containsKey(key)){
            return;
        }
        size++;
        if(head.leftChild == null){
            head.leftChild = new BSTNode<>(key, value);
            return;
        }
        BSTNode<K,V> node = head.leftChild;
        while(node != null){
            if(key.compareTo(node.key) < 0){
                if(node.leftChild == null){
                    node.leftChild = new BSTNode<>(key, value);
                    return;
                }
                else{
                    node = node.leftChild;
                }
            }
            else{
                if(node.rightChild == null){
                    node.rightChild = new BSTNode<>(key, value);
                    return;
                }
                else{
                    node = node.rightChild;
                }
            }
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("keySet cannot be used");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("remove cannot be used");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove cannot be used");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("iterator cannot be used");
    }
}
