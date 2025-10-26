package org.example.Boletin3.json;

import org.example.Boletin3.json.Loaders.InventarioLoader;
import org.example.Boletin3.json.Modelos.Producto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B3E7 {





    public B3E7() throws IOException {

        List<Producto> listaProductos = InventarioLoader.capturarProductos();
        Scanner sc = new Scanner(System.in);

        System.out.println("Buscar por nombre o 'tag' : ");
        String busq = sc.nextLine();
        System.out.println("Mostrando coincidencias - ");

        for(Producto p : listaProductos){
            if(p.getNombre().contains(busq)){
                p.mostrarUbicacion();
            }
            String[] tags = p.getTags();
            for(String t : tags){
                if(t.contains(busq)){
                    p.mostrarUbicacion();
                }
            }
        }


    }






}
