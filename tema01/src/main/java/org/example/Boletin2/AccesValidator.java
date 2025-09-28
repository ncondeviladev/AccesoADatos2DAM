package org.example.Boletin2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

public class AccesValidator {

    private final String ruta = "src/main/resources/Boletin2/pass.properties";
    Scanner sc = new Scanner(System.in);


    public AccesValidator() {

        try {
            crearPassProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Menú principal, por ahora solo tiene opcion validar acceso y pasar al menu usuario
     */
    public void menuAccesos() {


        int opcion = -1;
        while (opcion != 0) {
            System.out.println("- GESTIÓN DE ACCESOS -");
            System.out.println("");
            System.out.println("******************");
            System.out.println("1. Validar acceso");
            System.out.println("0. Salir");
            System.out.println("******************");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {

                case 1:
                    try { //Try porque el validar acceso puede devolver excepcion
                        System.out.println("Introduce la contraseña:");
                        String inputPass = sc.nextLine();
                        if (!validarAcceso(inputPass)) {
                            System.out.println("Contraseña incorrecta..");
                        } else {
                            menuUsuario();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    System.out.println("Saliendo..");
                    break;
                default:
                    System.out.println("Opción no válida..");
            }
        }
    }

    /**
     * Menú de usuario que permite cambiar la contraseña
     */
    public void menuUsuario() {

        int opcion = -1;
        while (opcion != 2) {
            System.out.println("- MENÚ USUARIO -");
            System.out.println("");
            System.out.println("******************");
            System.out.println("1. Modificar contraseña");
            System.out.println("2. Salir");
            System.out.println("******************");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    System.out.println("Introduce tu contraseña de nuevo:");
                    try {
                        if (!validarAcceso(sc.nextLine())) {
                            System.out.println("Contraseña inválida..");
                            break;
                        } else {
                            System.out.println("Introduce la nueva contraseña:");
                            modificarPass(sc.nextLine());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Saliendo..");
                    break;
            }
        }
    }

    /**
     * Método que devuelve un password en hash hexadecimal
     *
     * @param input pass para devolver formato hash String
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String toHashSHA1(String input) throws NoSuchAlgorithmException {

        //Creamos instancia de SHA-1, convertimos el pass input string a bytes
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        //Construimos en String los bytes en formato hexadecimal y lo devolvemos
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Método inicial para comprobar archivo de passwords y crearlo con el pass por defecto si no existe
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void crearPassProperties() throws IOException, NoSuchAlgorithmException {

        //Comprobamos si existe o creamos archivo y carpeta en ruta por defecto
        File f = new File(ruta);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();

            //Creamos pass por defecto
            String pass = "S3cret@";
            //Llamamos al metodo que convierte el pass en hash
            String hashPass = toHashSHA1(pass);

            //Clase properties para guardar clave-valor
            Properties prop = new Properties();
            prop.setProperty("pass", hashPass);
            //Abre el archivo y lo escribe
            try (FileOutputStream fos = new FileOutputStream(f)) {
                prop.store(fos, "Archivo passwords");
            }

            System.out.println("Se ha creado el archivo de contraseñas");
        } else {
            System.out.println("Archivo de contraseñas encontrado");
        }
    }

    /**
     * Método que comprueba un password introducido con el almacenado en el archivo
     *
     * @param inputPass Password introducido para el acceso
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private boolean validarAcceso(String inputPass) throws IOException, NoSuchAlgorithmException {

        //Properties para leer el hash y lo convertimos a String
        Properties prop = new Properties();
        prop.load(new FileInputStream(ruta));
        String hashPass = prop.getProperty("pass");

        //Convertimos en hash el pass capturado
        String inputHash = toHashSHA1(inputPass);

        //Comparamos y devolvemos boolean
        return inputHash.equals(hashPass);
    }

    /**
     * Método para comprobar si el formato de contraseña es válido
     *
     * @param inputPass
     * @return
     */
    private boolean validPass(String inputPass) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";
        return inputPass.matches(regex);
    }

    /**
     * Método que modifica la contraseña
     *
     * @param newPass
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private boolean modificarPass(String newPass) throws IOException, NoSuchAlgorithmException {
        //Comprobamos la contraseña
        if (!validPass(newPass)) {
            System.out.println("Formáto de contraseña inválido.. (Mayusc, minusc, num, caract, > 8)");
            return false;
        }

        Properties prop = new Properties();
        prop.setProperty("pass", toHashSHA1(newPass));
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            prop.store(fos, "Contraseña actualizada");
        }
        System.out.println("Contraseña modificada correctamente!");
        return true;
    }


}
