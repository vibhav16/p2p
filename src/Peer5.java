import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class Peer5 {

@SuppressWarnings("resource")
public static void main(String args[]) throws IOException{
	
	InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    ServerSocket ss3=null;
    Socket s2=null;
    Socket s3=null;
   
    int port=4445;
    
    

    try {
        s1=new Socket(address,port);
        peer5ownerthread owner=new peer5ownerthread(s1);
    	owner.start();
        s2=new Socket(address, 4449);
        ss3=new ServerSocket(4450);
       
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }
    while(true)
    {
    	s3=ss3.accept();
    	System.out.println("Connection established with peer1");
    	
    }
}
}
class peer5ownerthread extends Thread{
	BufferedReader br=null;
    BufferedReader is=null;
    ObjectOutputStream out=null;
    OutputStream out1=null;
	Socket s=null;
	public peer5ownerthread(Socket s)
	{
		this.s=s;
	}
	public void run(){
		System.out.println("owner connection thread done");
		
		try {
	       
	        br= new BufferedReader(new InputStreamReader(System.in));
	        is=new BufferedReader(new InputStreamReader(s.getInputStream()));
	        out= new ObjectOutputStream(s.getOutputStream());
	        out1=s.getOutputStream();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(
					"C:\\Users\\VIBHAV\\workspace\\p2p\\src\\peer5\\"+"advanced.pdf");
			InputStream in = s.getInputStream();
			DataInputStream data = new DataInputStream(in);
			long size = data.readLong();
			byte[] buff = new byte[4096];
			int bytes = 0;
			while (size > 0 && (bytes = data.read(buff, 0, (int) Math.min(buff.length, size))) != -1) {
				fos.write(buff, 0, bytes);
				size -= bytes;
			}
			fos.close();
			System.out.println("done");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	}
}