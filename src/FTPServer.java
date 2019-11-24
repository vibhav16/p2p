import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FTPServer {
@SuppressWarnings("resource")
public static void main(String args[]){

    Socket s=null;
    ServerSocket ss2=null;
    try{
        ss2 = new ServerSocket(4445); 
        System.out.println("Server Listening...");
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

    int sizeOfFiles = 1024 * 100;// 1KB
    byte[] buffer = new byte[sizeOfFiles];
    File f=new File("C:\\Users\\VIBHAV\\Desktop\\ctci.pdf");

    String fileName = f.getName();

    //try-with-resources to ensure closing stream
    try (FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = new BufferedInputStream(fis)) {

    int bytesAmount = 0;
    while ((bytesAmount = bis.read(buffer)) > 0) {
    //write each chunk of data into separate file with different number in name
    String filePartName = String.format("%03d.%s", partCounter++, fileName);
    File newFile = new File("C:\\Users\\VIBHAV\\workspace\\p2p\\src\\chunks\\", filePartName);
    try (FileOutputStream out = new FileOutputStream(newFile)) {
    out.write(buffer, 0, bytesAmount);
    //System.out.println("chunks");
    }
    }
    } catch (FileNotFoundException e1) {
    	// TODO Auto-generated catch block
    	e1.printStackTrace();
    } catch (IOException e1) {
    	// TODO Auto-generated catch block
    	e1.printStackTrace();
    }


    }
    catch(IOException e){
    e.printStackTrace();
    System.out.println("Server error");

    }
    
    
    while(true){
        try{
            s= ss2.accept();
            System.out.println("Connection Established");
            ServerThread st=new ServerThread(s);
            st.start();

        }

    catch(Exception e){
        e.printStackTrace();
        System.out.println("Connection Error");

    }
    }

}

}

class ServerThread extends Thread{  

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    OutputStream out = null;
    ObjectInputStream input=null;
    Socket s=null;
    
   File directory=new File("C:\\Users\\VIBHAV\\workspace\\cn\\src\\Server\\");
    List<String> list=new ArrayList<>();
    
    

    public ServerThread(Socket s){
        this.s=s;
    }
//
//    public void run() {
//    try{
//        is= new BufferedReader(new InputStreamReader(s.getInputStream()));
//        os=new PrintWriter(s.getOutputStream());
//        input=new ObjectInputStream(s.getInputStream());
//        out = s.getOutputStream();
//
//    }catch(IOException e){
//        System.out.println("IO error in server thread");
//    }
//
//
//    try {
//			while (true) {
//					FileOutputStream fos = new FileOutputStream(
//							"C:\\Users\\VIBHAV\\workspace\\cn\\src\\Server\\");
//					InputStream in = s.getInputStream();
//					DataInputStream data = new DataInputStream(in);
//					long size = data.readLong();
//					byte[] buff = new byte[4096];
//					int bytes = 0;
//					while (size > 0 && (bytes = data.read(buff, 0, (int) Math.min(buff.length, size))) != -1) {
//						fos.write(buff, 0, bytes);
//						size -= bytes;
//					}
//					fos.close();
//
//				}
//			
//        
//    } catch (IOException e) {
//
//        line=this.getName(); //reused String line for getting thread name
//        System.out.println("IO Error/ Client "+line+" terminated abruptly");
//    }
//    catch(NullPointerException e){
//        line=this.getName(); //reused String line for getting thread name
//        System.out.println("Client "+line+" Closed");
//    } catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//    finally{    
//    try{
//        System.out.println("Connection Closing..");
//        if (is!=null){
//            is.close(); 
//            System.out.println(" Socket Input Stream Closed");
//        }
//
//        if(os!=null){
//            os.close();
//            System.out.println("Socket Out Closed");
//        }
//        if (s!=null){
//        s.close();
//        System.out.println("Socket Closed");
//        }
//
//        }
    
    }
