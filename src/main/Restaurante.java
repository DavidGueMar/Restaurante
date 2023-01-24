package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




import domain.Mesa;
import domain.Metre;

public class Restaurante {

    public static void main(String[] args) throws IOException, InterruptedException {
        final int MAX_MESAS = 100;
        final int MAX_COCINEROS=8;
        Mesa[] mesa = new Mesa[MAX_MESAS];

        //variables de sincronizacion
        CyclicBarrier inicioConcurrente = new CyclicBarrier(MAX_MESAS);
        CountDownLatch esperaFinMesas = new CountDownLatch(MAX_MESAS);
        Semaphore semaforoCocinero = new Semaphore(MAX_COCINEROS);
        Lock screenLock = new ReentrantLock();

        //pipes de comunicacion

        PipedWriter emisor = new PipedWriter();
        PipedReader receptor = new PipedReader(emisor);
        PrintWriter flujoSalida = new PrintWriter(emisor);
        BufferedReader flujoEntrada = new BufferedReader(receptor);





        System.out.println("Iniciando simulacion");

        //inicializamos el metre
        Metre metre = new Metre(flujoEntrada);

        for (int i = 0; i < MAX_MESAS ; i++) {
            mesa [i] = new Mesa( inicioConcurrente, i, esperaFinMesas, semaforoCocinero,screenLock,flujoSalida );

        }

        esperaFinMesas.await();
        System.out.println("Mesas creadas");
        metre.terminar();

    }

}
