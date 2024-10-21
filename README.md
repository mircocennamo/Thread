# Creazione di Thread con la classe Thread e interfaccia Runnable
La programmazione concorrente in Java consente di eseguire più attività contemporaneamente, migliorando le prestazioni e l'efficienza delle applicazioni. In Java, puoi creare thread utilizzando la classe Thread o implementando l'interfaccia Runnable. Ecco come farlo:
3:
4:### Creazione di un Thread utilizzando la classe Thread
5:
6:```java
7:public class MyThread extends Thread {
8:    public void run() {
9:        System.out.println("Il thread è in esecuzione.");
10:    }
11:
12:    public static void main(String[] args) {
13:        MyThread thread = new MyThread();
14:        thread.start();
15:    }
16:}
17:```
18:
19:### Creazione di un Thread implementando l'interfaccia Runnable
20:
21:``` java
22:public class MyRunnable implements Runnable {
23:    public void run() {
24:        System.out.println("Il thread è in esecuzione.");
25:    }
26:
27:    public static void main(String[] args) {
28:        Thread thread = new Thread(new MyRunnable());
29:        thread.start();
30:    }
31:}
32:```

# L’INTERFACCIA RUNNABLE
La nostra classe deve implementare l’interfaccia java.lang.Runnable

Può essere utilizzata quando la nostra classe eredita già un’altra classe e quindi non possiamo estendere la classe Thread (ricorda che Java non supporta l’ereditarietà multipla!)
L’interfaccia specifica che bisogna implementare il metodo run()
Per creare un thread, è necessario utilizzare il costruttore della classe Thread passandogli come argomento l’istanza della nostra classe che implementa Runnable


# Ciclo di vita dei Thread


Il ciclo di vita di un thread in Java comprende diversi stati:

1. **Nuovo (New)**: Il thread è stato creato ma non è ancora stato avviato.
2. **Runnable**: Il thread è pronto per essere eseguito ed è in attesa di essere scelto dal thread scheduler.
3. **In esecuzione (Running)**: Il thread è in esecuzione.
4. **Bloccato (Blocked)**: Il thread è bloccato e in attesa di una risorsa.
5. **In attesa (Waiting)**: Il thread è in attesa indefinita di un'altra thread per eseguire un'azione specifica.
6. **Terminato (Terminated)**: Il thread ha terminato la sua esecuzione.

# problemi di concorrenza con l'utilizzo dei thread in Java
Per affrontare i problemi di concorrenza con l'utilizzo dei thread in Java, è importante comprendere i concetti di sincronizzazione e gestione delle risorse condivise. Ecco alcuni punti chiave:

1. **Sincronizzazione**: Utilizza il blocco `synchronized` per garantire che solo un thread alla volta possa eseguire un blocco di codice critico.
2. **Blocco esplicito**: Usa le classi di blocco esplicito come `ReentrantLock` per una gestione più flessibile della sincronizzazione.
3. **Volatile**: Usa la parola chiave `volatile` per variabili che possono essere lette e scritte da più thread.
4. **Evitare il deadlock**: Assicurati di acquisire i blocchi in un ordine coerente per evitare situazioni di stallo.
5. **Thread-safe collections**: Utilizza collezioni thread-safe come `ConcurrentHashMap` per evitare problemi di concorrenza.

Esempio di utilizzo di `synchronized`:

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

Esempio di utilizzo di `ReentrantLock`:

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

Questi sono solo alcuni esempi di come gestire i problemi di concorrenza in Java.

# Esempio di uso della parola chiave volatile in Java
La parola chiave `volatile` in Java viene utilizzata per dichiarare una variabile che può essere letta e scritta da più thread. Questo garantisce che le modifiche apportate alla variabile da un thread siano visibili agli altri thread. Ecco un esempio di utilizzo di `volatile`:

```java 
public class SharedResource {
    private volatile boolean flag = false;

    public void setFlag(boolean value) {
        flag = value;
    }

    public boolean getFlag() {
        return flag;
    }
}
```

In questo esempio, la variabile `flag` è dichiarata come `volatile`, il che significa che le modifiche apportate a essa da un thread saranno immediatamente visibili agli altri thread. Questo è utile quando si lavora con variabili condivise tra più thread. 


# Executor Framework, Future e Callable

L'Executor Framework in Java fornisce un meccanismo per gestire i thread in modo più efficiente. Utilizza le interfacce `Executor`, `ExecutorService` e le classi come `ThreadPoolExecutor` per gestire i thread.

## Utilizzo di ExecutorService, Future e Callable

Ecco un esempio di come utilizzare `ExecutorService`, `Future` e `Callable`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> task = () -> {
            Thread.sleep(1000);
            return "Task completato";
        };

        Future<String> future = executor.submit(task);

        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

# Utilizzo delle classi di concorrenza

## CountDownLatch

`CountDownLatch` è una classe di sincronizzazione che consente a uno o più thread di attendere fino a quando un insieme di operazioni in altri thread non viene completato.

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        // Creazione di un CountDownLatch con un contatore di 3
        CountDownLatch latch = new CountDownLatch(3);

        Runnable task = () -> {
            System.out.println("Task completato");
            latch.countDown();
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();

        latch.await();
        System.out.println("Tutti i task sono completati");
    }
}
```

## CyclicBarrier

`CyclicBarrier` è una classe di sincronizzazione che consente a un insieme di thread di attendere reciprocamente fino a quando tutti i thread non raggiungono una barriera comune.

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("Tutti i task sono pronti"));

        Runnable task = () -> {
            System.out.println("Task completato");
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }
}
```

## ConcurrentHashMap

`ConcurrentHashMap` è una classe thread-safe che fornisce un'implementazione di `Map` che può essere utilizzata in ambienti multithread.

```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    public void increment(String key) {
        map.merge(key, 1, Integer::sum);
    }

    public int getCount(String key) {
        return map.getOrDefault(key, 0);
    }

    public static void main(String[] args) {
        ConcurrentHashMapExample example = new ConcurrentHashMapExample();
        example.increment("key1");
        System.out.println("Count for key1: " + example.getCount("key1"));
    }
}
```
```


