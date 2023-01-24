package domain;

import java.io.BufferedReader;
import java.io.IOException;

public class Metre extends Thread{
    private BufferedReader flujoEntrada;
    private boolean terminado;

    public Metre(BufferedReader flujoEntrada) {
        this.flujoEntrada = flujoEntrada;
        this.terminado = false;
        this.start();
    }

    public void run() {
        String mensaje;
        while(!terminado) {
            try {
                mensaje = flujoEntrada.readLine();
                System.out.println("Recibido ------->" + mensaje);

            } catch (IOException e) {
                this.terminado= true;
                System.out.println("Se ha terminado ");
                //e.printStackTrace();
            }
        }

    }

    public void terminar() {
        this.terminado = true;
    }

}
