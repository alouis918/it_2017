import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class Client {

	
	
	public static void main(String[] args) {
		InputStream clientIn =null;
		byte [] receivedBytes =new byte[1024*1000];
		BufferedOutputStream bos= null;
		FileOutputStream fos=null;
		String fileToReceive="C:\\Users\\PaulArthur\\Desktop\\SoSe17\\Internet Techno\\Übung\\files\\testReceived.txt";
		// TODO Auto-generated method stub
		
		try(Socket clientSocket = new Socket("127.0.0.1",1234))
		{
			System.out.println("Download started");
			int byteRead = 0;
			int currentByte=0;
			clientIn =clientSocket.getInputStream();
			fos=new FileOutputStream(fileToReceive);
			bos=new BufferedOutputStream(fos);
			byteRead=clientIn.read(receivedBytes,0,receivedBytes.length);
			currentByte=byteRead;

			while(byteRead!=-1)
			{
			byteRead=clientIn.read(receivedBytes,currentByte,receivedBytes.length-currentByte);
			if(byteRead>=0)currentByte=+byteRead;		
				
			}
			bos.write(receivedBytes,0,currentByte);
			bos.flush();
			System.out.println("Download completed");
			
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(bos!=null) bos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
