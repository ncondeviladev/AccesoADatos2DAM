package org.example.Boletin2.E5;

import java.io.*;

public class GameStorage {

    private final String ruta;

    public GameStorage(String ruta) throws IOException{
        this.ruta = ruta;

        File f = new File(ruta);
        if(!f.exists()){
            f.getParentFile().mkdirs();
            f.createNewFile();
            System.out.println("Archivo de guardado creado");
        }
    }


    /** método para guardar partida en archivo binario
     * @param partida
     * @throws IOException
     */
    public void guardarPartida(TresEnRaya partida) throws IOException{

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))){
            oos.writeObject(partida);
            System.out.println("Partida guardada");
        }
    }

    /**
     * método para cargar partida desde archivo binario
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public TresEnRaya cargarPartida() throws IOException, ClassNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))){
            return (TresEnRaya) ois.readObject();
        }
    }






}
