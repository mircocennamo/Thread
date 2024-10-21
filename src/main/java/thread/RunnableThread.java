package thread;



/**
 * @author mirco.cennamo on 21/10/2024
 * @project Thread
 *
 *
 */
public class RunnableThread implements Runnable {
     public static void main(String[] args) {
         Thread thread = new Thread(new RunnableThread());
         System.out.println("Thread nello stato Nuovo.");
         thread.start();
         System.out.println("Thread nello stato Runnable.");
     }
    public void run() {
        System.out.println("Il thread è in esecuzione. " + Thread.currentThread().getName());
    }
}
