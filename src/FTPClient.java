
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class FTPClient {

@SuppressWarnings("resource")
public static void main(String args[]) throws IOException{
	
	InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    ServerSocket ss2=null;
    Socket s2=null;
    Socket s3=null;
   
    int port=4445;
  
    

    try {
        s1=new Socket(address,port);
        ss2=new ServerSocket(4446);
//        peer1thread thread=new peer1thread(s3);
//        thread.start();
        //s3= new Socket(address, 4449);
//        peer1thread thread=new peer1thread(new Socket(address, 4449));
//		 thread.start();
//        Runnable backGroundRunnable = new Runnable() {
//            public void run(){
//                 try {
//					Socket s3=new Socket(address, 4449);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }};
//        Thread sampleThread = new Thread(backGroundRunnable);
//        sampleThread.start();

        
    }
   
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }
 while(true){
	 try{
		 s2=ss2.accept();
		 System.out.println("Connection established with peer2");
//		 FileOutputStream fos = new FileOutputStream(
//					"C:\\Users\\VIBHAV\\workspace\\p2p\\src\\Client1\\" + space[1]);
//			InputStream in = s1.getInputStream();
//			DataInputStream data = new DataInputStream(in);
//			long size = data.readLong();
//			byte[] buff = new byte[4096];
//			int bytes = 0;
//			while (size > 0 && (bytes = data.read(buff, 0, (int) Math.min(buff.length, size))) != -1) {
//				fos.write(buff, 0, bytes);
//				size -= bytes;
//			}
//			fos.close();
//			System.out.println("done");
//        
		 
 }
    	catch(Exception e){
            e.printStackTrace();
            System.out.println("Connection Error");

        }
    }
  
}


}

class peer1thread extends Thread{
	Socket s=null;
	public peer1thread(Socket s)
	{
		this.s=s;
	}
	public void run(){
		try {
			s= new Socket(InetAddress.getLocalHost(), 4449);
			System.out.println("connection thread done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}