package domain;

import java.io.PipedWriter;
import java.io.PrintWriter;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Mesa extends Thread{
    final static int MAX_COCINEROS = 8;
    final static int MAX_MESAS= 100;


    private  int id ;
    private CyclicBarrier inicioConcurrente;
    CountDownLatch esperaFinMesas;
    Semaphore semaforoCocinero = new Semaphore(MAX_COCINEROS);
    private Lock screenLock;
    private PrintWriter flujoSalida;


    public Mesa(CyclicBarrier inicioConcurrente,
                int id, CountDownLatch esperaFinMesas, Semaphore semaforoCocinero,Lock screenLock,PrintWriter flujoSalida) {

        this.inicioConcurrente = inicioConcurrente;
        this.id = id;
        this.esperaFinMesas= esperaFinMesas;
        this.semaforoCocinero = semaforoCocinero;
        this.screenLock = screenLock;
        this.flujoSalida = flujoSalida;

        this.start();
    }
    public Mesa() {

    }	public void run() {
        String mensaje;




        try {
            this.inicioConcurrente.await();
            System.out.println(" Creando mesa "+ id);

            if(semaforoCocinero.tryAcquire()) {
                this.screenLock.lock();
                System.out.println("Estamos dentro del semaforo, la mesa "+ id);
                this.screenLock.unlock();


                semaforoCocinero.release();
                System.out.println("\nLa mesa " + id	+ " ha salido del semaforo");
            }else {
                screenLock.lock();
                mensaje = "Mesa " + id+" esperando ";
                flujoSalida.println(mensaje);
                screenLock.unlock();
                System.out.println("no hemos obtenido semaforo la mesa " +id+" Se espera");
                currentThread().sleep((long) (Math.random()*1000+1000));
            }





            esperaFinMesas.countDown();
            Thread.sleep(1000);

        } catch (InterruptedException | BrokenBarrierException e) {

            e.printStackTrace();
        }


    }

}
