package org.example.Boletin2.E5;

import java.io.Serializable;

public class TresEnRaya implements Serializable {

    private static final long versionSerial = 1L;
    private char[][] tablero;
    private char turno;
    private int victoriasX;
    private int victoriasY;

    public TresEnRaya(TresEnRaya juego){
        tablero = juego.getTablero();
    }

    public TresEnRaya() {
        tablero = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = ' ';
            }
        }
        turno = 'X';
        victoriasX = 0;
        victoriasY = 0;

        tablero[0][2] = (char) 'X';
        tablero[1][2] = (char) 'O';
        tablero[2][0] = (char) 'X';


        System.out.println(this);
    }

    public char[][] getTablero() {
        return tablero;
    }

    public char getTurno() {
        return turno;
    }

    public int getVictoriasX() {
        return victoriasX;
    }

    public int getVictoriasY() {
        return victoriasY;
    }

    public void setTurno(char turno){
        this.turno = turno;
    }


    /**
     * Prueba para añadir movimientos
     */
    public void pruebaTablero(){
        tablero[2][1] = (char) 'X';
        tablero[1][1] = (char) 'O';
    }

    /**
     * Método para mostrar el estado del juego
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turno de: ").append(getTurno()).append("\n");
        sb.append("Tablero:\n");

        for (int i = 0; i < tablero.length; i++) {
            sb.append(" ");
            for (int j = 0; j < tablero[i].length; j++) {
                sb.append(tablero[i][j] == '\0' ? ' ' : tablero[i][j]); // muestra la ficha o espacio vacío
                if (j < tablero[i].length - 1) sb.append(" | "); // separador vertical
            }
            sb.append("\n");
            if (i < tablero.length - 1) sb.append("---+---+---\n"); // separador horizontal
        }

        sb.append("Victorias X: ").append(getVictoriasX()).append("\n");
        sb.append("Victorias Y: ").append(getVictoriasY()).append("\n");

        return sb.toString();
    }
}