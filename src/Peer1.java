
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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Peer1 {

@SuppressWarnings("resource")
public static void main(String args[]) throws IOException, InterruptedException, ExecutionException{
	
	InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    ServerSocket ss2=null;
    //Socket s2=null;
    Socket s3=null;
     boolean check=true;
   
    int port=4445;
    try{   	
    	    	
    	s1=new Socket(address,port);
    	peer1ownerthread owner=new peer1ownerthread(s1);
    	owner.start();
    	ss2=new ServerSocket(4446);
    	
    	
    	
    }
    catch(Exception e){
    	System.out.println(e+"lol");
    }
    while(true){
    	s3=ss2.accept();
    	System.out.println("connected to peer 2");
    	uploadthread upload=new uploadthread(s3);
    	upload.start();    		  
    	
    		Thread.sleep(8000);
    		Socket s2=new Socket(address, 4450);
    		downloadPeer peer1=new downloadPeer(s2);
    		peer1.start();
    					
    	}   
    	
    }	 
 }


class downloadPeer extends Thread{
	Socket s=null;
	public downloadPeer(Socket s)
	{
		this.s=s;
	}
	public void run(){
		System.out.println("download connection thread done");
		
	
	}
}

class uploadthread extends Thread{
	Socket s=null;
	public uploadthread(Socket s)
	{
		this.s=s;
	}
	public void run(){
		System.out.println(" upload connection thread done");
		
	
	}
}
class peer1ownerthread extends Thread{
	BufferedReader br=null;
    BufferedReader is=null;
    ObjectOutputStream out=null;
    OutputStream out1=null;
	Socket s=null;
	public peer1ownerthread(Socket s)
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
					"C:\\Users\\VIBHAV\\workspace\\p2p\\src\\peer1\\"+"advanced.pdf");
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