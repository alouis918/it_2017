import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.BindException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerDispatcher  extends Thread
{
	Socket clientSocket=null;
	InputStream in=null;
	OutputStream out=null;	
	String rootPath="src\\serverDirectory";
	BufferedInputStream bis=null;
	FileInputStream fis=null;
	File file=null;
	public static String todoListContent="";
	
	public String getTodoListContent() {
        return todoListContent;
    }

	/**
	 * Constructor
	 * @param fromClientSocket
	 * @throws IOException
	 */
	public ServerDispatcher(Socket fromClientSocket) throws IOException
	{
		clientSocket=fromClientSocket;
		in=clientSocket.getInputStream();
		out=clientSocket.getOutputStream();
	}

	void sendFIleInfoWithJSON() throws IOException
	{
		JSONObject json=new JSONObject();
		json.addDouble("lastmodified", file.lastModified());
		json.addDouble("length", file.length());		
		out.write(json.getString().getBytes());
	}
	
	public void run()
	{
	    BufferedReader inBufferReader =null;
		try {
		     inBufferReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println("Connection established with"+clientSocket.toString());
			
			String [] str = inBufferReader.readLine().split(" ");
			
			if(str[0].equalsIgnoreCase("GET"))
			{
			    GetMethodeHandler getHandler = new GetMethodeHandler(out);
			    System.out.println(str[1]+"Came out");
			    FileInputStream fileStream = getHandler.parseFileName(str[1]);
			    if ( fileStream != null){
			        getHandler.getResponse(fileStream);
                }
			}
			else
			{
			    StringBuilder builder = new StringBuilder();
			    PostMethodeHandler postHandle = new PostMethodeHandler();
			    String firstSplit = postHandle.postHandlerForm(inBufferReader);
			    todoListContent = firstSplit;
			    GetMethodeHandler getHandler = new GetMethodeHandler(out);
		        FileInputStream fileStream = getHandler.parseFileName("/");
		        if ( fileStream == null){
		            return;
		        }
		        getHandler.getResponse(fileStream);
			}
		} catch (FileNotFoundException  | NullPointerException | BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(bis!=null) bis.close();
				if(clientSocket!=null) clientSocket.close();
				if(out!=null)out.close();
				if(in!=null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
