/*
 * Selman OZTURK
 * 150112028
 */
public class ResponseBuilder {
	/*
	 * This class only consists of one method
	 * This method creates a response string 
	 * according to response code parameter
	 * There are 2 response
	 * 1:	200 OK
	 * 2:   400 Bad Request
	 * 
	 * 
	 * If the response code 200
	 * fills the entire document with 'a' characters
	 * if not only gives an Bad Request page
	 */
	public String getResponse(int responseCode, int uri_number, String connectionType){
		System.out.println("Server Response:");
		String uri_digits = ""+uri_number;
		String head = "<!DOCTYPE html>\r\n<HTML>\r\n<HEAD>\r\n<TITLE>Response</TITLE>\r\n</HEAD>\r\n<BODY>\r\n";//53+17
		String end = "\r\n</BODY>\r\n</HTML>\r\n";//16
		String response = null;
		
		switch (responseCode) {
		case 400:
			response = connectionType+" " + 400 + " Bad Request\r\n";
			response += "Content-Length: " + 99 + "\r\n";
			response += "Content-Type: " + "text/html" + "\r\n";
			response += "Connection: close\r\n\r\n";
			System.out.println(response);
			response += head;
			response+=responseCode+" Bad Request";
			response += end;
	        //System.out.println(response);
	        
	        return response;
		case 200:
			response = connectionType+" " + responseCode + " OK\r\n";
			response += "Content-Length: " + uri_number + "\r\n";
			response += "Content-Type: " + "text/html" + "\r\n";
			response += "Connection: close\r\n\r\n";
			System.out.println(response);
			response += head;
			for(int i =0 ; i < (uri_number - head.length()-end.length()) ; i++){
				response+="a";
			}
			response += end;
	        //System.out.println(response);
	        return response;
		default:
			return "";
		}
	}
	
}
