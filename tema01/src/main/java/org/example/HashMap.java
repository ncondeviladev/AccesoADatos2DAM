package org.example;

public class HashMap<K, V> {

    private K[] claves;
    private V[] valores;
    private int size;

    @SuppressWarnings("unchecked") //evita la advertencia de compilación por el cast.
    public HashMap(int capacidad) {
        claves = (K[]) new Object[capacidad];
        valores = (V[]) new Object[capacidad];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public void put(K clave, V valor) {
        for (int i = 0; i < size; i++) {
            if (claves[i].equals(clave)) {
                valores[i] = valor;
                return;
            }
        }
        if (size >= claves.length) resize();
        claves[size] = clave;
        valores[size] = valor;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        K[] nuevasClaves = (K[]) new Object[claves.length * 2];
        V[] nuevosValores = (V[]) new Object[valores.length * 2];

        for (int i = 0; i < size; i++) {
            nuevasClaves[i] = claves[i];
            nuevosValores[i] = valores[i];
        }
        claves = nuevasClaves;
        valores = nuevosValores;
    }

    public V get(K clave) {
        for (int i = 0; i < size; i++) {
            if (claves[i].equals(clave)) {
                return valores[i];
            }
        }
        return null;
    }

    public int getSize() {
        return this.size;
    }

    public void remove(K clave) {
        for (int i = 0; i < size; i++) {
            if (claves[i].equals(clave)) {
                for (int j = i; j < size - 1; j++) {
                    claves[j] = claves[j + 1];
                    valores[j] = valores[j + 1];
                }
                claves[size - 1] = null;
                valores[size - 1] = null;
                size--;
                return;
            }
        }
    }

    public void mostrar() {
        for (int i = 0; i < size; i++) {
            System.out.println(claves[i] + " -> " + valores[i]);
        }
    }

    public boolean containsKey(K clave) {
        for (int i = 0; i < size; i++) {
            if (claves[i].equals(clave)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(V valor) {
        for (int i = 0; i < size; i++) {
            if (valores[i] == valor) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            claves[i] = null;
            valores[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public K[] keySet() {
        K[] resultado = (K[]) new Object[size];
        for (int i = 0; i < size; i++) {
            resultado[i] = claves[i];
        }
        return resultado;
    }

    @SuppressWarnings("unchecked")
    public V[] valueSet() {
        V[] resultado = (V[]) new Object[size];
        for (int i = 0; i < size; i++) {
            resultado[i] = valores[i];
        }
        return resultado;
    }
}



