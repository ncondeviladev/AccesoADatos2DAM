package org.example.Boletin2;

import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;

public class CalculadorPrimoMultiHilo implements Runnable {

    private String ruta = "src/main/java/resources/Boletin2/primosHilos.txt";
    private final int inicioHilo1 = 0, finHilo1 = 1000;
    private final int inicioHilo2 = 1001, finHilo2 = 2000;
    private final int inicioHilo3 = 2001, finHilo3 = 3000;
    private final int inicioHilo4 = 3001, finHilo4 = 4000;


    /**Comprueba y crea en caso de que no exista el archivo de guardado
     * @throws IOException
     */
    private void comprobarArchivo() throws IOException {
        File f = new File(ruta);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
            System.out.println("Archivo creado");
        } else {
            try(BufferedWriter wr = new BufferedWriter(new FileWriter(f, false))){}// false para que sobrescriba en vacio el archivo
        }
    }

    /**
     * Método para iniciar la clase, declara los hilos con sus rangos de búsqueda y los inicia
     */
    @Override
    public void run() {
        Thread h1 = new Thread(() -> calcularPrimos(inicioHilo1, finHilo1), "Hilo 1");
        Thread h2 = new Thread(() -> calcularPrimos(inicioHilo2, finHilo2), "Hilo 2");
        Thread h3 = new Thread(() -> calcularPrimos(inicioHilo3, finHilo3), "Hilo 3");
        Thread h4 = new Thread(() -> calcularPrimos(inicioHilo4, finHilo4), "Hilo 4");

        h1.start();
        h2.start();
        h3.start();
        h4.start();
        //Join para asegurarse de que run termine una vez los hilos hayan acabado
        try {
            h1.join();
            h2.join();
            h3.join();
            h4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**Método que busca los numeros primos entre un rango y los guarda en un archivo
     * @param inicio
     * @param fin
     */
    private void calcularPrimos(int inicio, int fin){
        for(int i = inicio; i <= fin; i++){
            if(esPrimo(i)){
                try {
                    guardarPrimo(i);
                    System.out.println(Thread.currentThread().getName() + " - " + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Comprueba si un entero es primo o no
     * @param n
     * @return
     */
    private boolean esPrimo(int n){
        if(n < 2) return false;
        for(int i = 2; i <= Math.sqrt(n); i++){
            if(n % i == 0) return false;
        }
        return true;
    }

    /**Método que se le da un entero y lo guarda en el archivo de primos
     * @param n
     * @throws IOException
     */
    private synchronized void guardarPrimo(int n) throws IOException {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(ruta, true))){
            wr.write(n + "\n");
            wr.newLine();
        }
    }
}
