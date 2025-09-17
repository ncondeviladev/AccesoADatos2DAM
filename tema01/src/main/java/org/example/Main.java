package org.example;

import org.example.*;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);


    private static void mostrarEjercicios() {
        // E1.existe();
        new E1();
        E2.listar();
        E3.infoDir();
        E4.mostrarArchivo();
    }

    private static void menuTerminal() {

        int opcion;
        do {
            System.out.println("Escoge una opción:");
            System.out.println();
            System.out.println("1 - Mostrar ejercicios 1, 2, 3 y 4 en terminal");
            System.out.println("2 - Gestión de archivos"); //Ejercicio 5

            System.out.println("0 - Salir");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    mostrarEjercicios();
                    break;
                case 2:
                    GestionArchivos gestor = new GestionArchivos();
                    System.out.println("1 - Crear archivo");
                    System.out.println("2 - Listar directorio");
                    System.out.println("3 - Ver Info");
                    System.out.println("4 - Leer archivo de texto");//Ejercicio 6
                    System.out.println("5 - Leer archivo binario");//Ejercicio 7
                    System.out.println("6 - Comparar archivos");//Ejercicio 9
                    System.out.println("7 - Concatenar archivos");//Ejercicio 10
                    System.out.println("8 - Concatenar textos"); //Ejercicio 11

                    opcion = sc.nextInt();
                    sc.nextLine();
                    switch (opcion) {
                        case 1:
                            System.out.println("Introduce directorio destino:");
                            String dirCrear = sc.nextLine();
                            System.out.println("Introduce nombre archivo");
                            String arc = sc.nextLine();
                            if (gestor.crearArchivo(dirCrear, arc))
                                System.out.println("Archivo creado con éxito");
                            break;
                        case 2:
                            System.out.println("Introduce directorio");
                            String dirListar = sc.nextLine();
                            gestor.listarDirectorio(dirListar);
                            break;
                        case 3:
                            System.out.println("Introduce directorio:");
                            String dirInfo = sc.nextLine();
                            System.out.println("Introduce nombre archivo");
                            String arcInfo = sc.nextLine();
                            gestor.verInfo(dirInfo, arcInfo);
                            break;
                        case 4:
                            System.out.println("Introduce ruta del archivo:");
                            String rutaT = sc.nextLine();
                            gestor.leerArchivoTexto(rutaT);
                            break;
                        case 5:
                            System.out.println("Introduce ruta del archivo binario:");
                            String rutaB = sc.nextLine();
                            gestor.mostrarBinario(rutaB);
                            break;
                        case 6:
                            System.out.println("Introcude ruta del primer archivo:");
                            String ruta1 = sc.nextLine();
                            System.out.println("Introcude ruta del segundo archivo:");
                            String ruta2 = sc.nextLine();
                            gestor.compararArchivos(ruta1, ruta2);
                            break;
                        case 7:
                            System.out.println("Introduce la ruta del primer archivo:");
                            String rutC1 = sc.nextLine();
                            System.out.println("Introduce la ruta del segundo archivo:");
                            String rutC2 = sc.nextLine();
                            System.out.println("Introduce la ruta del archivo a crear:");
                            String rutC3 = sc.nextLine();
                            gestor.concat(rutC1, rutC2, rutC3);
                        case 8:
                            System.out.println("Introduce la ruta del primer archivo:");
                            String rutT1 = sc.nextLine();
                            System.out.println("Introduce la ruta del segundo archivo:");
                            String rutT2 = sc.nextLine();
                            System.out.println("Introduce la ruta del archivo a crear:");
                            String rutT3 = sc.nextLine();
                            gestor.concatLineas(rutT1, rutT2, rutT3);
                            break;
                    }
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida...");
            }
        } while (opcion != 0);
    }


    public static void main(String[] args) throws FileNotFoundException {


        menuTerminal();

    }

}