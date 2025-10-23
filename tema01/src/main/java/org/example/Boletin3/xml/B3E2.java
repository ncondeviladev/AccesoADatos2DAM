package org.example.Boletin3.xml;

import org.example.Boletin3.xml.Loaders.EmpleadoLoader;
import org.example.Boletin3.xml.Modelos.Empleado;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B3E2 {

    //Clase interna para almacenar estadísticas por departamento
    static class EstadisticasDepartamento {
        double sumaSalarios = 0.0;
        int numEmpleados = 0;

        public void agregarEmpleado(double salario) {
            this.sumaSalarios += salario;
            this.numEmpleados++;
        }
    }


    public static void ejecutar() throws IOException, ParserConfigurationException, SAXException, NullPointerException{

        //Recuperamos la lista de empleados con empleadoLoader
        List<Empleado> empleados = EmpleadoLoader.capturarEmpleados();
        if(empleados.isEmpty()) {
            System.out.println("No se han encontrado empleados");
            return;
        }
        //Creamos una lista Map de departamentos
        Map<String, EstadisticasDepartamento> departamentos = new HashMap<>();

        //Recorremos la lista de empleados y capturamos su departameto y salario
        for (Empleado empleado : empleados) {
            String departamento = empleado.getDepartamento();
            double salario = empleado.getSalario();

            //Comprobamos si su departamento existe en la lista Map
            if (!departamentos.containsKey(departamento)) {
                //Si no existe lo creamos y añadimos los valores capturados y lo aadimos a la lista Map
                EstadisticasDepartamento estadisticasDepartamento = new EstadisticasDepartamento();
                estadisticasDepartamento.agregarEmpleado(salario);
                departamentos.put(departamento, estadisticasDepartamento);
            } else {
                //Si existe solo añadimos los valores
                departamentos.get(departamento).agregarEmpleado(salario);
            }
        }
        //Mostramos los resultados
        System.out.println("--- Resumen por Departamento ---");
        System.out.println("Departamento           | Nº Empleados | Salario Medio");
        System.out.println("-----------------------|--------------|---------------");
        for (Map.Entry<String, EstadisticasDepartamento> entry : departamentos.entrySet()) {

            String departamento = entry.getKey();
            EstadisticasDepartamento estadisticas = entry.getValue();
            double mediaSalarios = estadisticas.sumaSalarios / estadisticas.numEmpleados;
            //Imprime con formato
            System.out.printf("%-22s | %-12d | %.2f € \n",
                    departamento,
                    estadisticas.numEmpleados,
                    mediaSalarios);
        }
    }
}
