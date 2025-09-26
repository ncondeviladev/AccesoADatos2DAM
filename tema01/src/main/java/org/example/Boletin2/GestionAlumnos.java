package org.example.Boletin2;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class GestionAlumnos {

    File listaAlumnos;

    public GestionAlumnos(String ruta) throws IOException {

        listaAlumnos = new File(ruta);
        if (!listaAlumnos.exists()) {
            listaAlumnos.createNewFile();
            System.out.println("Se ha creado el archivo ListaAlumnos.txt");
        }
    }

    /**
     * Método para añadir alumnos al archivo creado previamente con una lista de Alumnos como argumento
     *
     * @param alumnos
     * @throws IOException
     */
    public void insertarAlumnos(Alumno[] alumnos) throws IOException {

        try (BufferedWriter wr = new BufferedWriter(new FileWriter(listaAlumnos, true))) { //true para que añada y no sobreescriba
            for (Alumno alumno : alumnos) {

                if (!alumnoExiste(alumno)) {

                    wr.write(alumno.getNia() + "," + alumno.getNombre() + "," + alumno.getApellido1() + "," + alumno.getApellido2() + "," + alumno.getFechaNacimiento().toString());
                    wr.newLine();
                }
            }
            System.out.println("Alumnos añadidos");
        }
    }

    /**
     * Método para capturar la lista de alumnos del archivo de texto.
     *
     * @return
     * @throws IOException
     */
    public ArrayList<Alumno> leerAlumnos() throws IOException {
        ArrayList<Alumno> alumnos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(listaAlumnos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datosAlumno = linea.split(",");
                if (datosAlumno.length == 5) {
                    Alumno alumno = new Alumno(datosAlumno[0], datosAlumno[1], datosAlumno[2], datosAlumno[3], LocalDate.parse(datosAlumno[4]));
                    alumnos.add(alumno);
                }
            }
        }
        return alumnos;
    }

    /**
     * Método para eliminar alumno del archivo, crea lista interna desde archivo busca y elimina y sobreescribe el archivo de nuevo.
     *
     * @param alumno
     * @throws IOException
     */
    public void eliminarAlumnos(Alumno alumno) throws IOException {

        ArrayList<Alumno> alumnos = leerAlumnos();
        alumnos.removeIf(a -> a.getNia().equals(alumno.getNia()));

        try (BufferedWriter wr = new BufferedWriter(new FileWriter(listaAlumnos))) {
            for (Alumno a : alumnos) {
                wr.write(a.getNia() + "," + a.getNombre() + "," + a.getApellido1() + "," + a.getApellido2() + "," + a.getFechaNacimiento().toString());
                wr.newLine();
            }
        }
        System.out.println("Alumno eliminado");
    }

    /**
     * Método para crear y añadir una serie de alumnos para hacer pruebas.
     *
     * @throws IOException
     */
    public void crearAlumnos() throws IOException {
        Alumno[] alumnos = new Alumno[5];
        alumnos[0] = new Alumno("1001", "María", "Pérez", "Gómez", LocalDate.of(2004, 5, 12));
        alumnos[1] = new Alumno("1002", "Juan", "López", "Martínez", LocalDate.of(2003, 8, 23));
        alumnos[2] = new Alumno("1003", "Lucía", "Fernández", "Ruiz", LocalDate.of(2005, 1, 3));
        alumnos[3] = new Alumno("1004", "Carlos", "Sánchez", "Ortega", LocalDate.of(2002, 11, 30));
        alumnos[4] = new Alumno("1005", "Elena", "Torres", "Morales", LocalDate.of(2004, 7, 19));

        insertarAlumnos(alumnos);
    }

    /**
     * Método para buscar y comprobar si existe un aluno para evitar duplicados
     *
     * @param a
     * @return
     * @throws IOException
     */
    public boolean alumnoExiste(Alumno a) throws IOException {

        ArrayList<Alumno> listaAlumnos = leerAlumnos();

        for (Alumno alumno : listaAlumnos) {
            if (alumno.getNia().equals(a.getNia()) &&
                    alumno.getNombre().equalsIgnoreCase(a.getNombre()) &&
                    alumno.getApellido1().equalsIgnoreCase(a.getApellido1()) &&
                    alumno.getApellido2().equalsIgnoreCase(a.getApellido2()) &&
                    alumno.getFechaNacimiento().equals(a.getFechaNacimiento())) {
                System.out.println("El alumno ya existe");
                return true;
            }
        }
        return false;
    }


}

