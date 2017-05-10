import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerDispatcher  extends Thread {
	Socket clientSocket=null;
	InputStream in=null;
	OutputStream out=null;
	
	String filePath="G:\\test.txt";
	BufferedInputStream bis=null;
	FileInputStream fis=null;
	public ServerDispatcher(Socket fromClientSocket) throws IOException
	{
		clientSocket=fromClientSocket;
	
		out=clientSocket.getOutputStream();
	}
public void run()
{
	try {
		System.out.println("Connection established with"+clientSocket.toString());
		File file=new File(filePath);
		byte [] byteArray=new byte[(int)file.length()];
		fis= new FileInputStream(file);
		/*
		bis= new BufferedInputStream(fis);
		bis.read(byteArray,0,byteArray.length);
		out.write(byteArray,0,byteArray.length);
		out.flush();
		
		*/
	
		 int len;
		while((len=fis.read(byteArray))!=-1)
		 {
			 out.write(byteArray, 0, len);
		 }
	
		out.flush();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
}

}
