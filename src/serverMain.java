/*
 * Selman OZTURK
 * 150112028
 * Some parts of that project created with the benefits
 * from open source projects on forums(StackOverFlow,Github).
 */
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.regex.Pattern;


public class serverMain {
    private int port = 0;
    private ServerSocket socketServer;
    
    private int numOfThreads;
    private CriticalCase[] threads;
    private LinkedList list;
    
    /*
     * Constructor of class
     * Takes an argument which is port number
     * and tries to listen from this port
     * if port already taken
     * a Server Binding Exception occurs
     * I can not handled this problem
     * PLEASE BE SURE THAT THE PORT IS NOT ALREADY TAKEN
     */
	public serverMain(int portNumber){
		this.port=portNumber;
		try{
            socketServer = new ServerSocket(this.port);
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("IO Exception while connecting to socket at port: "+this.port);
            System.exit(-1);
        }
		
        System.out.println("Server starting on port: " + port);
	}
	
	/*
	 * 
	 * This function runs threads
	 * and start listening from specified port
	 * when a request comes
	 * it creates a new socket
	 * and resume connection on that socket
	 */
	public void startListening() throws Throwable {
    	list = new LinkedList();
        startThreads(Runtime.getRuntime().availableProcessors());
        while (true) {
            Socket socket = socketServer.accept();
            execute(new Connection(socket));
        }
    }
	//This is for send start command to threads
	public void startThreads(int nThreads){
		this.numOfThreads = nThreads;
        threads = new CriticalCase[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new CriticalCase(list);
            threads[i].start();
        }
	}
	
	//This function adds our connection into a Linked list
	public void execute(Runnable r) {
        synchronized(list) {
            list.addLast(r);
            list.notify();
        }
    }
	
	
	/*
	 * Main function of class
	 * Takes one argument which is port number of server
	 * if argument's length are create than 1 or argument is not a number
	 * gives an error message and terminates itself
	 * if a user don't gives any argument starts at default port: 8080
	 */
	public static void main(String[] args) {
		int portNum=0;
		
		if(args.length>1){
			System.out.println("You must enter only one argument: Port Number");
			System.exit(-1);
		}
		else if(args.length==0){
			System.out.println("No argument entered. Server will start with default port: 8080");
			portNum=8080;
		}
		else if( Pattern.matches("[0-9]+", args[0] ) == true ){
			portNum = Integer.parseInt(args[0]);
		}
		else{
			System.out.println("Wrong argument! Use only a number.");
			System.exit(-1);
		}
		
		
		serverMain server = new serverMain(portNum);
		try {
			server.startListening();
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Error Listening port. ");
		}

	}

}
