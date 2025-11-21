import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class BackpressureRxJava {

    public static void main(String[] args) {

        // Productor rápido: emite un valor cada 1 ms
        Flowable<Long> productor = Flowable
                .interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(
                        500,
                        v -> System.out.println("[BUFFER LLENO] descartado: " + v)
                )
                // Para que el flujo termine y no se quede infinito
                .take(2000);

        System.out.println("Suscribiendo consumidor lento...");

        productor
                .observeOn(Schedulers.io()) // procesar en otro hilo
                .blockingSubscribe(
                        v -> {
                            // Consumidor lento
                            System.out.println("Procesando: " + v + " en " + Thread.currentThread().getName());
                            try {
                                Thread.sleep(100); // simula trabajo pesado
                            } catch (InterruptedException e) {
                                System.err.println("Hilo interrumpido");
                                Thread.currentThread().interrupt();
                            }
                        },
                        error -> {
                            System.err.println("Ocurrió un error en el flujo:");
                            error.printStackTrace();
                        },
                        () -> System.out.println("Flujo completado correctamente")
                );

        System.out.println("Programa terminado.");
    }
}
