# Creazione di Thread con la classe Thread e interfaccia Runnable
La programmazione concorrente in Java consente di eseguire più attività contemporaneamente, migliorando le prestazioni e l'efficienza delle applicazioni. In Java, puoi creare thread utilizzando la classe Thread o implementando l'interfaccia Runnable. Ecco come farlo:

### Creazione di un Thread utilizzando la classe Thread

```java
public class MyThread extends Thread {
    public void run() {
       System.out.println("Il thread è in esecuzione.");
    }

   public static void main(String[] args) {
        MyThread thread = new MyThread();
       thread.start();
    }
}
```

### Creazione di un Thread implementando l'interfaccia Runnable

``` java
public class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Il thread è in esecuzione.");
   }

    public static void main(String[] args) {
       Thread thread = new Thread(new MyRunnable());
        thread.start();
    }
}
```

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




## Importanza del testing

Il testing è una parte cruciale dello sviluppo software che garantisce che il codice funzioni come previsto e che i bug vengano identificati e risolti prima che il software venga rilasciato. Ecco alcuni motivi per cui il testing è importante:

1. **Qualità del software**: Assicura che il software soddisfi i requisiti e funzioni correttamente.
2. **Manutenzione**: Facilita la manutenzione del codice, rendendo più semplice l'individuazione e la correzione dei bug.
3. **Affidabilità**: Aumenta la fiducia nel software, riducendo il rischio di errori in produzione.
4. **Documentazione**: I test possono servire come documentazione del comportamento previsto del codice.

# Test Driven Development (TDD)

Il Test Driven Development (TDD) è una metodologia di sviluppo software in cui i test vengono scritti prima del codice di produzione. Il processo TDD segue questi passaggi:

1. **Scrivi un test**: Scrivi un test che fallisce per una nuova funzionalità che desideri implementare.
2. **Scrivi il codice**: Scrivi il codice minimo necessario per far passare il test.
3. **Refactoring**: Migliora il codice mantenendo i test passanti.

Esempio di TDD in Java:

```java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
}

class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

In questo esempio, il test per il metodo `add` viene scritto prima di implementare il metodo stesso. Questo assicura che il codice di produzione soddisfi i requisiti del test.
```



```markdown
# Scrittura di test unitari con JUnit, Assert e gestione delle eccezioni nei test

JUnit è una delle librerie più utilizzate per scrivere test unitari in Java. Ecco alcuni esempi di come scrivere test unitari utilizzando JUnit, Assert e come gestire le eccezioni nei test.

## Esempio di test unitario con JUnit e Assert

```java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
}

class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

## Gestione delle eccezioni nei test

Per verificare che un metodo lanci una specifica eccezione, puoi utilizzare l'annotazione `@Test` con il parametro `expected`.

```java
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class ExceptionTest {
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        Calculator calculator = new Calculator();
        calculator.divide(1, 0);
    }
}

class Calculator {
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return a / b;
    }
}
```

## Test parametrizzati

JUnit 4 e JUnit 5 supportano i test parametrizzati, che consentono di eseguire lo stesso test con diversi set di dati.

### Esempio con JUnit 4

```java
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParameterizedTest {
    private int input;
    private int expected;

    public ParameterizedTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 1, 2 }, { 2, 4 }, { 3, 6 }
        });
    }

    @Test
    public void testMultiplyByTwo() {
        Calculator calculator = new Calculator();
        assertEquals(expected, calculator.multiplyByTwo(input));
    }
}

class Calculator {
    public int multiplyByTwo(int input) {
        return input * 2;
    }
}
```

### Esempio con JUnit 5

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ParameterizedTest {
    @ParameterizedTest
    @CsvSource({
        "1, 2",
        "2, 4",
        "3, 6"
    })
    public void testMultiplyByTwo(int input, int expected) {
        Calculator calculator = new Calculator();
        assertEquals(expected, calculator.multiplyByTwo(input));
    }
}

class Calculator {
    public int multiplyByTwo(int input) {
        return input * 2;
    }
}
```



# Introduzione al mocking

Il mocking è una tecnica utilizzata nei test unitari per simulare il comportamento di oggetti complessi o dipendenze esterne. Questo consente di isolare il codice in test e di verificare il comportamento di una singola unità di codice senza dover dipendere da altre parti del sistema.

## Utilizzo di Mockito per creare mock e stub

Mockito è una delle librerie più popolari per il mocking in Java. Consente di creare mock e stub in modo semplice e intuitivo.

### Creazione di un mock

Per creare un mock con Mockito, puoi utilizzare il metodo `mock`:

```java
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.util.List;

public class MockExample {
    @Test
    public void testMock() {
        // Crea un mock di List
        List<String> mockedList = mock(List.class);

        // Definisci il comportamento del mock
        when(mockedList.get(0)).thenReturn("Hello, World!");

        // Usa il mock nel test
        System.out.println(mockedList.get(0));  // Stampa "Hello, World!"
    }
}
```

### Creazione di uno stub

Uno stub è un tipo di mock che ha un comportamento predefinito per alcuni metodi. Puoi utilizzare il metodo `when` per definire il comportamento dello stub:

```java
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.util.List;

public class StubExample {
    @Test
    public void testStub() {
        // Crea un mock di List
        List<String> mockedList = mock(List.class);

        // Definisci il comportamento dello stub
        when(mockedList.size()).thenReturn(10);

        // Usa lo stub nel test
        System.out.println(mockedList.size());  // Stampa "10"
    }
}
```

### Verifica delle interazioni

Mockito consente anche di verificare le interazioni con i mock, assicurandosi che i metodi siano stati chiamati con i parametri corretti:

```java
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.util.List;

public class VerifyExample {
    @Test
    public void testVerify() {
        // Crea un mock di List
        List<String> mockedList = mock(List.class);

        // Usa il mock
        mockedList.add("one");
        mockedList.clear();

        // Verifica che il metodo add sia stato chiamato con "one"
        verify(mockedList).add("one");

        // Verifica che il metodo clear sia stato chiamato
        verify(mockedList).clear();
    }
}
```

Questi sono solo alcuni esempi di come utilizzare Mockito per creare mock e stub nei test unitari. Mockito offre molte altre funzionalità avanzate per il mocking, come la gestione delle eccezioni, la verifica delle chiamate multiple e molto altro.
```