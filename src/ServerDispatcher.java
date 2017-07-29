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
import java.net.Socket;
import java.util.Arrays;


public class ServerDispatcher  extends Thread
{
	Socket clientSocket=null;
	InputStream in=null;
	OutputStream out=null;	
	String rootPath="src\\serverDirectory";
	BufferedInputStream bis=null;
	FileInputStream fis=null;
	//byte [] byteArray;
	File file=null;

	/*
	 * gets path to a file and returns a fileinputstream pointing to it
	 */
	FileInputStream getFileInputStream(String FileName) throws IOException 
	{
		try {
			file=new File(FileName);
			//byteArray=new byte[(int)file.length()];
			return  new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			out.write("File could not be found".getBytes());
			out.flush();
		}
		return null;
}
	
	/**
	 * parse a file name given in the form \filename
	 * @param fileName Name of the file to be returned
	 * @return fileInputStrem to pointing to the file.
	 * @throws IOException
	 */
	FileInputStream parseFileName(String fileName) throws IOException
	{
		FileInputStream fis=null;
		switch (fileName)
		{
		case "/":
			fis= getFileInputStream( rootPath+"\\\\index.html");
			break;
		default:
		{
			String temp=fileName.replace("/", "\\\\");  
			fis=getFileInputStream( rootPath+temp);
			break;
		}

		}
		return fis;
	}
	/**
	 * send the file over the socket
	 * @param fis
	 * @throws IOException
	 */
	void getResponse(FileInputStream fis) throws IOException
	{
		int len;
		PrintWriter print = new PrintWriter(out);
		BufferedReader infile = new BufferedReader(new InputStreamReader(fis));
		String fromFile = "",temp ;
		while((temp = infile.readLine()) != null)
		{
		   fromFile += temp; 
		}
		//System.out.println("infile"+fromFile);
		
		String response1 = "HTTP/1.1 200 OK\r\n" +
			    "Content-Length:"+ fromFile.length()+"\r\n" +
			    "Content-Type: text/html\r\n\r\n"; 
		print.println(response1+fromFile);
		
		print.flush();
		print.close();
		//out.flush();
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
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println("Connection established with"+clientSocket.toString());
			String [] str = in.readLine().split(" ");
			
			if(str[0].equalsIgnoreCase("GET"))
			{
				 FileInputStream fileStream = parseFileName(str[1]);
				 if ( fileStream == null){
	                 return;
	             }
	             getResponse(fileStream);
			}
			else
			{
			    String temp,fromFile = "";
		        while((temp = in.readLine()) != null)
		        {
		            System.out.println(temp);
		            fromFile += temp;
		            if (temp.length() == 0)
		            {
		                int tempInt;
		                while((tempInt = in.read())!= -1)
		                {
		                    char ch = (char)tempInt;
		                    System.out.println(ch);
		                }
		                System.out.println("out of while loop");
		                break;
		            }
		         }
		        System.out.println("out");
		       // System.out.println("out");
		       //in.readLine();in.readLine();in.readLine();
		        //System.out.println("post fromfile: "+ fromFile+in.readLine());
		        //System.out.println(builder.toString());
			}
			
			
		} catch (FileNotFoundException e) {
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
