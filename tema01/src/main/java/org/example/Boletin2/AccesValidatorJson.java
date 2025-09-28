package org.example.Boletin2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AccesValidatorJson {

    private final String ruta = "src/main/resources/Boletin2/usuarios.json";
    private final Scanner sc = new Scanner(System.in);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final HashMap<String, String> usuarios;


    public AccesValidatorJson() {

        usuarios = new HashMap<>();
        try {
            crearUsuariosJson();
            cargarUsuarios();
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
            System.out.println("2. Crear usuario");
            System.out.println("3. Eliminar usuario");
            System.out.println("0. Salir");
            System.out.println("******************");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {

                case 1:
                    try { //Try porque el validar acceso puede devolver excepcion
                        System.out.println("Introduce tu usuario:");
                        String user = sc.nextLine();
                        System.out.println("Introduce tu contraseña:");
                        String inputPass = sc.nextLine();
                        if (!validarAcceso(user, inputPass)) {
                            System.out.println("Contraseña incorrecta..");
                        } else {
                            menuUsuario();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try{
                        System.out.println("Introduce nombre de usuario:");
                        String nom = sc.nextLine();
                        System.out.println("Introduce contraseña:");
                        String pass = sc.nextLine();
                        if(!validPass(pass)){
                            System.out.println("Formato de contraseña inválido.. (Mayusc, minusc, num, caract, > 8)");
                        } else {
                            agregarUsuario(nom, pass);
                            System.out.println("Usuario creado con éxito");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try{
                        System.out.println("Introduce nombre de usuario a eliminar:");
                        String nom = sc.nextLine();
                        System.out.println("Introduce su contraseña:");
                        String pass = sc.nextLine();
                        if(!validarAcceso(nom, pass)){
                            System.out.println("Error en la validación de usuario");
                        } else {
                            eliminarUsuario(nom);
                            System.out.println("Usuario eliminado con éxito");
                        }
                    }catch (Exception e){
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
                    System.out.println("Introduce tu usuario de nuevo:");
                    String user = sc.nextLine();
                    System.out.println("Introduce tu contraseña de nuevo:");
                    String inputPass = sc.nextLine();
                    try {
                        if (!validarAcceso(user, inputPass)) {
                            System.out.println("Contraseña inválida..");
                            break;
                        } else {
                            System.out.println("Introduce la nueva contraseña:");
                            String newPass = sc.nextLine();
                            modificarPass(user, newPass);
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
     * Método inicial para comprobar archivo de usuarios y crearlo con el pass por defecto si no existe
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void crearUsuariosJson() throws IOException, NoSuchAlgorithmException {

        //Comprobamos si existe o creamos archivo y carpeta en ruta por defecto
        File f = new File(ruta);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();

            //Creamos usuario y pass por defecto
            String user = "user";
            String pass = "S3cret@";

            //Llamamos al metodo que convierte el pass en hash y lo introducimos en la lista
            String hashPass = toHashSHA1(pass);
            usuarios.put(user, hashPass);

            try(BufferedWriter wr = new BufferedWriter(new FileWriter(f))){
                gson.toJson(usuarios, wr);
            }


            System.out.println("Se ha creado el archivo de usuarios");
        } else {
            cargarUsuarios();
            System.out.println("Archivo de usuarios encontrado");
        }
    }

    /**
     * Método que comprueba un usuario y password introducido con el almacenado en el archivo
     *
     * @param inputPass Password introducido para el acceso
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private boolean validarAcceso(String user, String inputPass) throws IOException, NoSuchAlgorithmException {

        //Comprobamos que el usuario existe
        if(!usuarios.containsKey(user)){
            System.out.println("Usuario no encontrado");
            return false;
        }

        //Convertimos en hash el pass capturado
        String inputHash = toHashSHA1(inputPass);

        //Comparamos y devolvemos boolean
        return inputHash.equals(usuarios.get(user));
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
    private boolean modificarPass(String user,String newPass) throws IOException, NoSuchAlgorithmException {

        //Comprobamos si el usuario existe
        if (!usuarios.containsKey(user)) {
            System.out.println("Usuario no encontrado");
            return false;
        }

        //Comprobamos la contraseña
        if (!validPass(newPass)) {
            System.out.println("Formáto de contraseña inválido.. (Mayusc, minusc, num, caract, > 8)");
            return false;
        }

        //Añadimos a la lista el usuario y su pass convertido en hash
        usuarios.put(user, toHashSHA1(newPass));

        //Sobrescribimos el json completo
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(ruta))) {
            gson.toJson(usuarios, wr);
        }
        System.out.println("Contraseña cambiada con éxito");
        return true;
    }

    /**Método para cargar los usuarios del archivo a la lista interna al inicio del programa
     * @throws IOException
     */
    private void cargarUsuarios() throws IOException {
        File f = new File(ruta);
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                HashMap<String, String> usuariosCargados = gson.fromJson(br, usuarios.getClass());
                if(usuariosCargados != null){
                    usuarios.clear();
                    usuarios.putAll(usuariosCargados);
                } else {
                    System.out.println("Error al cargar los usuarios");
                }
            }
        }
    }

    /**Método para agregar usuario
     * @param user
     * @param pass
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void agregarUsuario(String user, String pass) throws IOException, NoSuchAlgorithmException {

        //Convertimos pass a hash y los añadimos al hashmap, sobrescribimos el archivo
        usuarios.put(user, toHashSHA1(pass));
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(ruta))) {
            gson.toJson(usuarios, wr);
        }
    }

    /**Método para eliminar usuarios
     * @param user
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void eliminarUsuario(String user) throws IOException, NoSuchAlgorithmException {
        if(!usuarios.containsKey(user)) {
            System.out.println("No se encuenta el usuario");
        } else {
            usuarios.remove(user);
            try (BufferedWriter wr = new BufferedWriter(new FileWriter(ruta))) {
                gson.toJson(usuarios, wr);
            }
            System.out.println("Usuario eliminado");
        }
    }



}
