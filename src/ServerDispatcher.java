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
	byte [] byteArray;
	File file=null;

	/*
	 * gets path to a file and returns a fileinputstream pointing to it
	 */
	FileInputStream getFileInputStream(String FileName) throws IOException 
	{
		try {
			file=new File(FileName);
			byteArray=new byte[(int)file.length()];
			return  new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			out.write("File could not be found".getBytes());
			out.flush();
		}
		return null;
}
	/**
	 * 
	 * @param fis get file InputStream pointing to a 
	 * @return
	 */
	String [] getFileNameFromPath()
	{
		String [] array = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine;
			int a = 0;
			String []str = null;
			String temp="";
		
			int i =0;
			
//			while ((str = in.readLine()) != null) {
//			      System.out.println(i+++str);
//			    }
//			while(true){
//				temp= in.readLine();
//				System.out.println("i= "+i+++" "+temp.length());	
//				if (temp.length() == 0)					
//				{
//					break;
//				}
//				else 
//					{
//					str+=temp;
//					//System.out.println(a+":"+str);
//					}				
//				}
			//System.out.println("hihihihihih");
			//System.out.println("str = " + str);
			//inputLine = in.readLine();
			
			//inputLine=str;
			//array=inputLine.split(" ");   

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	String[] postResponse(){
		String[] array = {};
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(in));
			String inputLine;
			inputLine = inBuffer.readLine();
			array = inputLine.split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  array;
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
		System.out.println("infile"+infile.readLine());
//		while((len=fis.read(byteArray))!=-1)
//		{
//		}
		String response1 = "HTTP/1.1 200 OK\r\n" +
			    "Content-Length: 22\r\n" +
			    "Content-Type: text/html\r\n\r\n";
			    //+"<h1>hihih</h1>";
		String response = "";
		System.out.println("bytearray1"+byteArray.toString());
		
		for (byte b : byteArray) {
			response+=(char)b;
		}
		System.out.println("bytearray2"+byteArray[0]);
		System.out.println("response="+response);
		System.out.println("response+response1="+response1+response);
		
		print.println(response1+response);
		/*print.println(response1);
		print.flush();
		print.println();
		String response = "";
		for (byte b : byteArray) {
			response+=(char)b;
		}
		print.println(response);*/
		print.flush();
		print.close();
		//out.flush();
	}

	/**
	 * 
	 * @param fis
	 * @throws IOException
	 */
	void sendFIleOverSOcketBuffered(FileInputStream fis) throws IOException
	{
		//Writing/sending the file  to the client passing via a buffer
		bis= new BufferedInputStream(fis);
		bis.read(byteArray,0,byteArray.length);
		out.write(byteArray,0,byteArray.length);
		out.flush();
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
				
			}
			
			/*System.out.println("Connection established with"+clientSocket.toString());
            String[] httpHeader = getFileNameFromPath();
            for (String string : httpHeader) {
				
            	System.out.print(string);
			}
            if(httpHeader[0].equalsIgnoreCase("GET")){ // case where is a get request
                FileInputStream fileStream = parseFileName(httpHeader[1]);
                out.write("\n".getBytes());
                if ( fileStream == null){
                    return;
                }
                getResponse(fileStream);
                //sendFIleInfoWithJSON();
            }else if (httpHeader[0].equalsIgnoreCase("POST")){ // case where it is a post request
                 //System.out.println("hihih"+Arrays.binarySearch(httpHeader,1,httpHeader.length,"\r\n")+1);
                 System.out.println(httpHeader[9]);
				String [] str = postResponse();
				System.out.println( str[1]);
            }
*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(bis!=null) bis.close();
				if(clientSocket!=null) clientSocket.close();
				if(out!=null)out.close();
				if(in!=null) in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
