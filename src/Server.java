import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(1234);
		Socket clientSocket;
		while(true)
			{
			System.out.println("Server Started");
			clientSocket=serverSocket.accept();
			ServerDispatcher SD=new ServerDispatcher(clientSocket);
			SD.start();			
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(serverSocket!=null)
				try {
					serverSocket.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}

}
