import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class BackpressureRxJava {

    public static void main(String[] args) throws Exception {

        // Productor rápido
        Flowable<Long> productor = Flowable
                .interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(
                        500,
                        v -> System.out.println("[BUFFER LLENO] descartado: " + v)
                );

        System.out.println("Suscribiendo consumidor lento...");

        productor
                .observeOn(Schedulers.io())
                .subscribe(
                        v -> {
                            System.out.println("Procesando: " + v);
                            Thread.sleep(100);
                        },
                        Throwable::printStackTrace
                );

        Thread.sleep(10000);
    }
}
