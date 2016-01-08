/*
 * Selman OZTURK
 * 150112028
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Pattern;
/*
 * Connection class
 */
public class Connection implements Runnable{
	public int uri_int=0;
	public Socket socket;
	String commandName, uri, connectionType;
	String header, response;
	InputStream inputStream;
	OutputStream outputStream;
	
	/*
	 * This constructor starts listening from the specified socket.
	 * firstly try to read input stream from socket
	 * after creates a response message
	 * and write this message to output stream of socket    
	 */
	
	public Connection(Socket socket){
		this.socket=socket;
		try {
			inputStream = socket.getInputStream();
		} catch (IOException e) {
			System.err.println("Error reading input stream.");
			e.printStackTrace();
		}
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			System.err.println("Error reading output stream.");
			e.printStackTrace();
		}
	}
	
	/*
	 * This is a function comes from the interface: Runnable
	 * gives 3 commands:
	 * 1:	Read header
	 * 2:	Write a log to console
	 * 3:	Build a response 
	 */
	public void run() {
		try {
			readHeaderFromInputStream(inputStream);
			if(header==null) return;
			System.out.println("Received Request:");
			System.out.println(""+header);
			
			buildResponse();
			
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * This function calls functions to create a response
	 * Firstly, splits header into parts
	 * Then, checks the value of them
	 * if all is OK, builds the response and 
	 * writes the response to output stream
	 * if not, writes a bad request to output stream
	 */
	public void buildResponse(){
		getCommandName();
		getUri();
		getConnectionType();
		if(commandName==null || uri==null || connectionType==null )return;
		//System.out.println("-"+commandName+"\n-"+uri+"\n-"+connectionType);
		if(getUriInteger() && uri_int >=100 && uri_int <=20000){
			try {
				outputStream.write(new ResponseBuilder().getResponse(200,uri_int,connectionType).getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("wrong uri");
			try {
				outputStream.write(new ResponseBuilder().getResponse(400,uri_int,connectionType).getBytes());
				outputStream.flush();
				//String head = "<!DOCTYPE html>\r\n<HTML>\r\n<HEAD>\r\n<TITLE>Response</TITLE>\r\n</HEAD>\r\n<BODY>\r\n400 Bad Request\r\n</BODY>\r\n</HTML>\r\n";
				//outputStream.write(head.getBytes());
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
    	
    }
	
	/*
	 * This function reads the header 
	 * from the input stream 
	 * into a string
	 */
	private void readHeaderFromInputStream(InputStream inStream) throws Throwable {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder builder = new StringBuilder();
        String singleline = null;
        while (true) {
        	singleline = bufferedReader.readLine();
            if (singleline == null || singleline.isEmpty()) {
                break;
            }
            builder.append(singleline+ System.getProperty("line.separator"));
            //break;//only get first request. Ignore browser appended strings
        }
        this.header=builder.toString();
    }
	
	public void getCommandName(){
		try {
			commandName = header.split(" ",0)[0];
		} catch (Exception e) {
			System.err.println("Error getting command name from header");
		}
	}
	
	/*
	 * This is for splitting
	 *  URI from header
	 */
	public void getUri(){
		try {
			uri = header.split(" ",0)[1];
		} catch (Exception e) {
			System.err.println("Error getting uri from header");
		}
		if(uri!=null && uri.length()>0) uri = uri.substring(1);
	}
	
	/*
	 * this is for splitting 
	 * Connection Type from header
	 */
	public void getConnectionType(){
		int indexOfSecondBlank,lineEndIndex;
		indexOfSecondBlank = header.indexOf(" ", 0);
		indexOfSecondBlank = header.indexOf(" ", indexOfSecondBlank+1);
		//System.out.println(indexOfSecondBlank);
		lineEndIndex = header.indexOf("\n");
		//System.out.println(lineEndIndex);
		if( indexOfSecondBlank==-1 || lineEndIndex==-1){
			connectionType=null;
			return;
		}
		connectionType=header.substring(indexOfSecondBlank+1, lineEndIndex-1);
	}
	
	/*
	 * This function checks 
	 * if the value in the URI 
	 * is a number or not
	 */
	public boolean getUriInteger(){
		if(Pattern.matches("[0-9]+", uri) == true ){
			uri_int=Integer.parseInt(uri);
			return true;
		}else{
			return false;
		}
	}
	
}
