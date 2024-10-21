package thread;



/**
 * @author mirco.cennamo on 21/10/2024
 * @project Thread
 *
 *
 */
public class MainThread extends Thread {
    public static void main(String[] args) {
        MainThread thread = new MainThread();
        System.out.println("Thread nello stato Nuovo.");
        thread.start();
        System.out.println("Thread nello stato Runnable.");
    }

    @Override
    public void run() {
        System.out.println("Thread in esecuzione " + Thread.currentThread().getName());
           /* try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            */


    }

}
