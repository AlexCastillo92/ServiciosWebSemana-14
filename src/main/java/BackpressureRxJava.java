import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class BackpressureRxJava {

    public static void main(String[] args) {

        Flowable<Long> productor = Flowable
                .interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(
                        500, // tamaño del buffer
                        v -> System.out.println("[BUFFER LLENO] descartado: " + v)
                )
                .take(2000);

        productor
                .observeOn(Schedulers.io())
                .blockingSubscribe(
                        v -> {
                            System.out.println("Procesando: " + v);
                            Thread.sleep(100);
                        },
                        Throwable::printStackTrace,
                        () -> System.out.println("Flujo completado")
                );
    }
}
