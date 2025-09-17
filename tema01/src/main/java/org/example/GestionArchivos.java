package org.example;

import java.io.*;

public class GestionArchivos {


    public boolean crearArchivo(String directorio, String archivo) {

        File a = new File(directorio + "/" + archivo);
       try {
            return a.createNewFile();
       } catch (IOException e) {
           System.out.println("Error al crear el archivo " + e.getMessage());
           return false;
       }
    }

    public void listarDirectorio(String directorio){

        File dir = new File(directorio);
        File[] listaArchivos = dir.listFiles();
        if(listaArchivos == null) {
            System.out.println("No se encuentra el directorio");
            return;
        }

        for(File f : listaArchivos){
            System.out.print(f.getName());

            if(f.isDirectory()){
                System.out.print(" d");
            } else System.out.print(" f");

            System.out.println(" " + f.length());

            String permisos = "-";
            permisos += f.canRead() ? "r" : "";
            permisos += f.canWrite() ? "w" : "";
            System.out.print(" " + permisos);
        }
    }

    public void verInfo(String directorio, String archivo){

        File f = new File(directorio + "/" + archivo);
        if(!f.exists()) {
            System.out.println("No se encuentra el archivo");
            return;
        }
        System.out.println("Nombre del archivo: " + f.getName());
        System.out.println("Ruta absoluta: " + f.getAbsolutePath());
        System.out.println("Se puede leer? " + f.canRead());
        System.out.println("Se puede escribir? " + f.canWrite());
        System.out.println("tamaño del archivo: " + f.length());
        if(f.isDirectory()){
            System.out.println("Es un directorio");
        } else System.out.println("Es un archivo");

        }

    public void leerArchivoTexto(String ruta){

        File archivo = new File(ruta);
        if(!archivo.exists() || !archivo.isFile()){
            System.out.println("El archivo no existe..");
            return;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(archivo))) {

            String linea;
            while((linea = reader.readLine()) != null){
                System.out.println(linea);
            }
        }catch(IOException e){
            System.out.println("Error de archivo " + e.getMessage());
        }
    }

    public void mostrarBinario(String ruta){
        File archivo = new File(ruta);

        if(!archivo.exists() || archivo.isFile()){
            System.out.println("El archivo no existe");
            return;
        }

        try(FileInputStream st = new FileInputStream(archivo)) {
            int byt;
            int contador = 0;

            while((byt = st.read()) != -1){
                System.out.printf("%02x ", byt);
                contador++;

                if(contador % 12 == 0){
                    System.out.println();
                }
            }
        } catch(IOException e){
            System.out.println("Error con el archivo.. " + e.getMessage());
        }
    }

    public boolean compararArchivos(String ruta1, String ruta2){

        File f1 = new File(ruta1);
        File f2 = new File(ruta2);

        if(!f1.exists() || !f1.isFile() || f2.exists() || f2.isFile()){
            System.out.println("Alguno de los archivos no existe..");
            return false;
        }

        try(FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2)){

            int byt1 = 0, byt2 = 0;

            while (true){
                byt1 = fis1.read();
                byt2 = fis2.read();

                if(byt1 != byt2){
                    return false;
                } else if (byt1 == -1)
                    break;
            }
        } catch(IOException e){
            System.out.println("Error a leer los archivos: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void concat(String ruta1, String ruta2, String rutaDestino)  {
        File f1 = new File(ruta1);
        File f2 = new File(ruta2);
        File fDest = new File(rutaDestino);
        try{
            fDest.createNewFile();
        } catch (IOException e) {
            System.out.println("Error al crear el tercer archivo. " + e.getMessage());
        }

        if(!f1.exists() || !f1.isFile() || !f2.exists() || !f2.isFile()) {
            System.out.println("Alguno de los archivos no es correcto..");
            return;
        }
        try(FileInputStream fis1 = new FileInputStream(f1);
            FileInputStream fis2 = new FileInputStream(f2);
            FileOutputStream fisDest = new FileOutputStream(fDest)) {

            byte[] buffer = new byte[1024];
            int leido;
            while((leido = fis1.read(buffer)) != -1){
                fisDest.write(buffer);
            }
            while((leido = fis2.read(buffer)) != -1){
                fisDest.write(buffer);
            }


        }catch(IOException e){
            System.out.println("Error con los archivos. " + e.getMessage());
        }
    }


    public void concatLineas(String ruta1, String ruta2, String rutaDestino){
        File f1 = new File(ruta1);
        File f2 = new File(ruta2);
        File fDest = new File(rutaDestino);
        try{
            fDest.createNewFile();
        } catch (IOException e) {
            System.out.println("Error al crear el tercer archivo. " + e.getMessage());
        }
        if(!f1.exists() || !f1.isFile() || !f2.exists() || !f2.isFile()) {
            System.out.println("Alguno de los archivos no es correcto..");
            return;
        }

        try(BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fDest))){

            String linea1, linea2;
            while((linea1 = br1.readLine()) != null && (linea2 = br2.readLine()) != null){
                bw.write(linea1 + linea2);
                bw.newLine();
            }
            while((linea1 = br1.readLine()) != null){
                bw.write(linea1);
                bw.newLine();
            }
            while((linea2 = br2.readLine()) != null){
                bw.write(linea1);
                bw.newLine();
            }
        }catch(IOException e) {
            System.out.println("Error con los archivos. " + e.getMessage());
        }
    }
}

