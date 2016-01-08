/*
 * Selman OZTURK
 * 150112028
 */
import java.util.LinkedList;

//This class waits threads for atomic operations
//which are in a Linked list
//I took it from an open source project on github.
public class CriticalCase extends Thread {
    	public LinkedList list;
    	
    	public CriticalCase(LinkedList list){
    		this.list = list;
    	}
    	
        public void run() {
            Runnable r;
            while (true) {
                synchronized(list) {
                    while (list.isEmpty()) {
                        try{
                        	list.wait();
                        } catch (InterruptedException ignored){}
                    }

                    r = (Runnable) list.removeFirst();
                }

                try {
                    r.run();
                } catch (RuntimeException e) {
                 }
            }
        }
    }