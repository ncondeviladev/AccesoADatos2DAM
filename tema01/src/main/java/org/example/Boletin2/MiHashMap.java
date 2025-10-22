package org.example.Boletin2;

public class MiHashMap<K, V> {

    private int size;
    private Node<K, V>[][] data;

    public MiHashMap(int capacity) {
        data = new Node[capacity][capacity];
        size = 0;
    }

    private int hash(K key) {
        int hash = key.hashCode();
        hash = hash * 31;
        hash = hash & 0x7FFFFFFF;
        return hash % data.length;
    }

    public V put(K key, V value) {
        int column = hash(key);
        int i;
        for (i = 0; i < data[column].length; i++) {
            Node<K, V> node = data[column][i];
            if (node == null || node.getKey().equals(key)) {
                break;
            }
        }
        if (i == data[column].length) {
            resize();
        }
        Node<K, V> oldValue = data[column][i];
        data[column][i] = new Node<>(key, value);
        return oldValue.getValue();
    }

    public V get(K key) {
        int column = hash(key);
        for (int i = 0; i <data[0].length; i++){
            Node<K, V> node = data[column][i];
            if(node == null){
                return null;
            }
            if(node.getKey().equals(key)){
                return node.getValue();
            }
        }
        return null;
    }
    public void remove(K key) {
        int column = hash(key);
        for (int i = 0; i < data[column].length; i++) {
            Node<K, V> node = data[column][i];
            if (node == null){
                return;
            }
            if (node.getKey().equals(key)) {
                data[column][i] = null;
            }

        }
    }

    private void resize() {
        throw new RuntimeException("Aun no implementado");
    }

    static class Node<K, V> {
        private final K key;
        private final V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }


        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }


}
