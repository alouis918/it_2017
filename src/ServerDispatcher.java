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
	String rootPath="D:\\uniHtml";
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
	String[] getFileNameFromPath()
	{
		String [] array = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine;
			inputLine = in.readLine();
			array=inputLine.split(" ");   

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
			fis= getFileInputStream( rootPath+"\\\\todo.html");
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

		while((len=fis.read(byteArray))!=-1)
		{
		}

		print.println();
		String response = "";
		for (byte b : byteArray) {
			response+=(char)b;
		}

		print.println(response);
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
			System.out.println("Connection established with"+clientSocket.toString());
            String[] httpHeader = getFileNameFromPath();

            if(httpHeader[0].equalsIgnoreCase("GET")){ // case where is a get request
                FileInputStream fileStream = parseFileName(httpHeader[1]);
                out.write("\n".getBytes());
                if ( fileStream == null){
                    return;
                }
                getResponse(fileStream);
                //sendFIleInfoWithJSON();
            }else if (httpHeader[0].equalsIgnoreCase("POST")){ // case where it is a post request
                 System.out.println(Arrays.binarySearch(httpHeader,1,httpHeader.length,"\n")+1);

				String [] str = postResponse();
				System.out.println( str[1]);




            }

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
