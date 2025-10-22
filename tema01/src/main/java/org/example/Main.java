package org.example;


import org.example.Boletin1.*;
import org.example.Boletin2.Alumno;
import org.example.Boletin2.E1;
import org.example.Boletin3.xml.B3E2;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;


public class Main {


    private static void mostrarEjercicios() {
        // E1.existe();
        new E1();
        E2.listar();
        E3.infoDir();
        E4.mostrarArchivo();
    }

    public static void probarHashMap() {
        HashMap<String, Integer> map = new HashMap<>(10);
        map.put("uno", 1);
        map.put("dos", 2);
        map.put("tres", 3);
        map.put("cuatro", 4);

        System.out.println("Tamaño del HashMap: " + map.getSize());
        System.out.println();


        System.out.println("Mostramos el primer elemento");
        System.out.println("Numero 1 : " + map.get("uno"));
        System.out.println();

        System.out.println("Mostramos todos los elementos");
        map.mostrar();
        System.out.println("Eliminamos el último elemento");
        map.remove("cuatro");
        System.out.println();
        map.mostrar();
    }

    private static void menuTerminalB1() {

        Scanner sc = new Scanner(System.in);
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


    public static void crearAlumnosAleatorios ( int cantidad){
        java.util.HashMap<String, Alumno> alumnos = new java.util.HashMap<>(20);

        com.github.javafaker.Faker faker = new com.github.javafaker.Faker(Locale.of("ES"));
        Bombo bombo = new Bombo(1000, 9_999_999);
        for (int i = 0; i < cantidad; i++) {
            String nia = String.format("%08d", i);
            String nombre = faker.name().firstName();
            String apellido1 = faker.name().lastName();
            String apellido2 = faker.name().lastName();
            Date fechaRandom = faker.date().birthday(18, 50);
            LocalDate fechaNacimiento = fechaRandom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            alumnos.put(nia, new Alumno(nia, nombre, apellido1, apellido2, fechaNacimiento));
        }
        for(Alumno a : alumnos.values()) {
            System.out.println(a);
        }

    }

    public static void main(String[] args) throws IOException {
        //Boletin 1 --------------
        //menuTerminalB1();
        //probarHashMap();

        //Boletin 2 --------------
        //E1
        //E1.crearDni(FileUtils.convertirRutaEnFile("src/main/resources/Boletin2/dni.txt"));

        //E2
        /* GestionAlumnos gestorAlumnos = new GestionAlumnos("src/main/resources/Boletin2/ListaAlumnos.txt");
        gestorAlumnos.crearAlumnos();
        Alumno a1 = new Alumno("1040", "MariCarmen", "Jimenez", "Peiro",  LocalDate.of(1995, 5, 10));
        Alumno a2 = new Alumno("1238", "Paco", "Vila", "Arjona", LocalDate.of(1991, 12, 21));
        Alumno[] nuevosAlumnos = new Alumno[]{a1, a2};
        gestorAlumnos.insertarAlumnos(nuevosAlumnos);
        gestorAlumnos.eliminarAlumnos(a1);
        System.out.println(a1.toString());
        System.out.println(a2.toString()); */


        //E3
        /*AccesValidator av = new AccesValidator();
        av.menuAccesos();*/

        //E4
        /*AccesValidatorJson avj = new AccesValidatorJson();
        avj.menuAccesos();*/

        //E5
        /*TresEnRaya tresR = new TresEnRaya();
        tresR.pruebaTablero();
        GameStorage gs = new GameStorage("src/main/resources/tresEnRaya.dat");

        try {
            gs.guardarPartida(tresR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TresEnRaya nuevoTres = null;
        try {
            nuevoTres = new TresEnRaya(gs.cargarPartida());
            nuevoTres.setTurno('O');
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(nuevoTres.toString());*/

        //E6
        //CalculadorPrimo prims = new CalculadorPrimo();
        //prims.menuPrimos();

        //E7
        //System.out.println("Cores - " + Runtime.getRuntime().availableProcessors());
        //CalculadorPrimoMultiHilo primoHilos = new CalculadorPrimoMultiHilo();
        //primoHilos.run();

        //crearAlumnosAleatorios(10);


        // BOLETIN 3 =====================================================

        //E1
        /*
        * try {
            B3E1.ejecutar();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo empleados.xml");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("Error al configurar el parser XML");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("El XML tiene errores de formato");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al leer el archivo");
            e.printStackTrace();
        }*/

        //E2
        try{
            B3E2.ejecutar();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo empleados.xml");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("Error al configurar el parser XML");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("El XML tiene errores de formato");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al leer el archivo");
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Error NullPointer");
            e.printStackTrace();
        }

    }


}
