import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PostMethodeHandler {
    private OutputStream out=null;
    public static void writeMessageToBrowser(String message,OutputStream out )
    {
        PrintWriter print = new PrintWriter(out);
        String response1 = "HTTP/1.1 200 OK\r\n" +
                "Content-Length:"+ message.length()+"\r\n" +
                "Content-Type: text/html\r\n\r\n"; 
        print.println(response1+message);
        print.flush();
        print.close();
       
    }
    
    public String postHandlerForm(BufferedReader in)
    {
        StringBuilder builder = new StringBuilder();
        String temp,fromFile = "";
        boolean breakOutLoop=false;
        try {
            while((temp = in.readLine()) != null)
            {
                System.out.println(temp);
                fromFile += temp;
                if (temp.length() == 0)
                {
                    CharSequence sequence = "&Save=Submit";
                    int tempInt;
                    while((tempInt = in.read())!= -1)
                    {
                        char ch = (char)tempInt;
                        builder .append(ch);
                        System.out.println("was got= "+builder.toString());
                        if (builder.toString().contains(sequence))
                        {
                            System.out.println("was got= "+builder.toString());
                            breakOutLoop = true;
                            break;
                        }
                        System.out.println("out1");
                        if (breakOutLoop)
                            break;
                       
                    }
                    System.out.println("out of while loop");
                    break;
                }
                if (breakOutLoop)
                    break;
             }
        } catch (IOException e) {            
            e.printStackTrace();
        }
        String toReturn = builder.toString().replaceAll("liste=", "").replaceAll("&Save=Submit", "");
        return  toReturn.replaceAll("\\+", " ");
 
    }
}
