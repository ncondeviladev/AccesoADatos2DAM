package org.example.Boletin2;

import java.io.*;
import java.util.Scanner;

public class CalculadorPrimo {

    private String ruta = "src/main/resources/Boletin2/primos.txt";


    public CalculadorPrimo() { }

    /**Comprueba y crea en caso de que no exista el archivo de guardado
     * @throws IOException
     */
    private void comprobarArchivo() throws IOException {
        File f = new File(ruta);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
            System.out.println("Archivo creado");
        }
    }

    /** Lee el último número primo guardado en el archivo y lo devuelve
     * @return
     * @throws IOException
     */
    private int leerUltimoPrimo() throws IOException {

        int ultimo = 1;

        try(BufferedReader rd = new BufferedReader(new FileReader(ruta))){
            String linea;
            while((linea = rd.readLine()) != null){
                if(!linea.isBlank()) {
                    ultimo = Integer.parseInt(linea.trim());
                }
            }
        }
        return ultimo;
    }

    /**Método que comprueba si un entero es primo
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

    /**Calcula el siguiente número primo desde el último capturado
     * @param ultimoPrimo
     * @return
     */
    private int siguientePrimo(int ultimoPrimo) throws IOException{
        int siguiente = ultimoPrimo + 1;
        while(!esPrimo(siguiente)){
            siguiente++;
        }
        guardarPrimo(siguiente);
        return siguiente;
    }

    /**Guarda un número primo en el archivo
     * @param n
     * @throws IOException
     */
    private void guardarPrimo(int n) throws  IOException {
        try(BufferedWriter wr = new BufferedWriter(new FileWriter(ruta, true))){
            wr.write(n + "\n");
        }
    }


    /**Genera una cantidad de números primos determinada por argumento y lo guarda en el archivo
     * @param n
     * @throws IOException
     */
    public void generarPrimos(int n) throws IOException{
        comprobarArchivo();
        int ultimoPrimo = leerUltimoPrimo();
        for(int i = 0; i < n; i++){
            ultimoPrimo = siguientePrimo(ultimoPrimo);
            guardarPrimo(ultimoPrimo);
            System.out.println("Número primo - " + ultimoPrimo);
        }

    }

    /**Menú del ejercicio de números primos
     * @throws IOException
     */
    public void menuPrimos() throws IOException {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;
        while(opcion != 0){

            System.out.println("");
            System.out.println("- - GENERADOR DE NÚMEROS PRIMOS - -");
            System.out.println("1 - Generar primos hastan 'n' veces..");
            System.out.println("2 - Mostrar último primo");
            System.out.println("3 - Generar el siguiente primo");
            System.out.println("0 - Salir");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion){
                case 1:
                    System.out.println("Introduce la cantidad de primos a generar:");
                    int n = sc.nextInt();
                    generarPrimos(n);
                    break;
                case 2:
                    System.out.println("El último número primo es: " + leerUltimoPrimo());
                    break;
                case 3:
                    System.out.println("El siguiente número primo es: " + siguientePrimo(leerUltimoPrimo()));
                    break;
                case 0:
                    System.out.println("Saliendo..");
                    break;
            }
        }
    }



}
