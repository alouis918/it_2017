import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class GetMethodeHandler {
    public static String rootPath="src\\serverDirectory";
    private OutputStream out=null;
    
    public GetMethodeHandler(OutputStream out)
    {
        this.out = out;        
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
        if (searchForSubString(fileName, ".html"))
        {
            fileName = fileName.replaceAll(".html", "");
            
        }
         
        switch (fileName.replaceAll("/", ""))
        {
        case "/":
            fis= getFileInputStream( rootPath+"\\\\index.html");
            break;
        case "todoadd":
            fis= getFileInputStream( rootPath+"\\\\todoadd.html");
            break;
        case "todoget":
            writeMessageToBrowser( ServerDispatcher.todoListContent, out );
            return null;
        case "canvas":
            fis= getFileInputStream( rootPath+"\\\\canvas.html");
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
    
    /*
     * gets path to a file and returns a fileinputstream pointing to it
     */
    FileInputStream getFileInputStream(String FileName) throws IOException 
    {
        File file = null;
        try {
            file=new File(FileName);            
            return  new FileInputStream(file);
        } catch (FileNotFoundException e) {
            out.write("File could not be found".getBytes());
            out.flush();
        }
        return null;
    }
    
   public void getResponse(FileInputStream fis) throws IOException
    {
        int len;
        PrintWriter print = new PrintWriter(out);
        BufferedReader infile = new BufferedReader(new InputStreamReader(fis));
        String fromFile = "",temp ;
        while((temp = infile.readLine()) != null)
        {
           fromFile += temp; 
        }
        
        String HEADER = "HTTP/1.1 200 OK\r\n" +
                "Content-Length:"+ fromFile.length()+"\r\n" +
                "Content-Type: text/html\r\n\r\n"; 
        print.println(HEADER+fromFile);
        
        print.flush();
        print.close();
        //out.flush();
    }
   
   public static void writeMessageToBrowser(String message,OutputStream out )
   {
       
       PrintWriter print = new PrintWriter(out);
       
       System.out.println("infile= "+message);
       
         String form = "<!DOCTYPE html>\n"
               +"<html lang=\"en\">\n"
               +"<head>\n"
               +"\t<meta charset=\"UTF-8\">"
               +"\t<title>ToDo List</title>\n"
               +"</head>\n"+
               "<body>\n"+"<p>ToDo</p>\n"+"<textarea  readonly>\n"+message+"</textarea>"+"</body></html>";
      
       
       String response1 = "HTTP/1.1 200 OK\r\n" +
               "Content-Length:"+form.length()+"\r\n" +
               "Content-Type: text/html\r\n\r\n"; 
       print.println(response1+form);
       print.flush();
       print.close();
       
   }
   
   private boolean searchForSubString(String originalString, CharSequence sequence)
   {
       return originalString.contains(sequence);
   }

    
}
