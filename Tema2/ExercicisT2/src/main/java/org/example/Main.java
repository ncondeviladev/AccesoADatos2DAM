package org.example;

import org.example.Boletin1.DAOs.ciclismoDAO;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main app donde se llaman a los metodos de menu, donde estos llaman a su correspondiente metodo del DAO manejando las excepciones sql y las interacciones con el usuario
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ciclismoDAO ciclismoDAO = new ciclismoDAO();



    public static void main(String[] args) {
        menuPrincipal();
    }

    private static void listarEquipos() {
        try {
            ciclismoDAO.listarEquipos();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
            return;
        }
    }


    public static void menuCiclistasPorEquipo() {

        listarEquipos();
        System.out.println("^ Seleccion '0' para mostrar todos los ciclistas por equipo");
        System.out.println("\nIntroduce el ID del equipo deseado:");
        int idEquipo;
        try {
            idEquipo = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, introduce un número.");
            return;
        }
        try {
            ciclismoDAO.ciclistasPorEquipo(idEquipo);
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    private static void listarEtapas() {
        try {
            ciclismoDAO.listarEtapas();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
            return;
        }
    }

    private static void menuVelocidadCiclista() {
        System.out.println("Introduce el ID del equipo: ");
        listarEquipos();
        int idEquipo;
        try {
            idEquipo = sc.nextInt();
            sc.nextLine();
            ciclismoDAO.ciclistasPorEquipo(idEquipo);

            System.out.println("Introduce el ID del ciclista: ");
            int idCiclista;

            idCiclista = sc.nextInt();
            sc.nextLine();

            ciclismoDAO.velocidadMediaCiclista(idCiclista);
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
            return;
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, introduce un número.");
            return;
        }
    }

    private static void menuClasificacionEtapa() {
        System.out.println("Introduce el ID de la etapa: ");
        listarEtapas();

        int idEtapa;
        try {
            idEtapa = sc.nextInt();
            sc.nextLine();

            ciclismoDAO.clasificacionEtapa(idEtapa);
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, introduce un número.");
            return;
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    private static void clasificacionMontana() {
        try {
            ciclismoDAO.clasificacionMontana();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    private static void clasificacionRegularidad() {
        try {
            ciclismoDAO.clasificacionRegularidad();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    private static void clasificacionGeneral() {
        try {
            ciclismoDAO.clasificacionGeneral();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    private static void clasificacionPorEquipo() {
        try {
            ciclismoDAO.clasificacionPorEquipo();
        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }


    private static void menuPrincipal() {

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("");
            System.out.println("- - VUELTA ATLÁNTICA - -");
            System.out.println("Selecciona una opción:");
            System.out.println("1 - Listar equipos");
            System.out.println("2 - Ciclistas por equipo");
            System.out.println("3 - Listar etapas");
            System.out.println("4 - Velocidad media ciclista");
            System.out.println("5 - Clasificación de etapa");
            System.out.println("6 - Clasificación de montaña");
            System.out.println("7 - Clasificación de regularidad");
            System.out.println("8 - Clasificación general");
            System.out.println("9 - Clasificación por equipo");
            System.out.println("0 - Salir");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {

                case 1:
                    listarEquipos();
                    break;
                case 2:
                    menuCiclistasPorEquipo();
                    break;
                case 3:
                    listarEtapas();
                    break;
                case 4:
                    menuVelocidadCiclista();
                    break;
                case 5:
                    menuClasificacionEtapa();
                    break;
                case 6:
                    clasificacionMontana();
                    break;
                case 7:
                    clasificacionRegularidad();
                    break;
                case 8:
                    clasificacionGeneral();
                    break;
                case 9:
                    clasificacionPorEquipo();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;

            }
        }
    }

}