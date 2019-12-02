
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.io.ObjectInputStream;
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
    InputStream in=null;
	DataInputStream data=null;
	BufferedInputStream buff=null;
	BufferedOutputStream bos=null;
	boolean flag=true;
	DataOutputStream dos=null;
	ObjectInputStream ois=null;
	int len;
	int smblen;
	public peer1ownerthread(Socket s)
	{
		this.s=s;
	}
	public void run(){
		System.out.println("owner connection thread done");
		
		try {
	       ois=new ObjectInputStream(s.getInputStream());
	        br= new BufferedReader(new InputStreamReader(System.in));
	        is=new BufferedReader(new InputStreamReader(s.getInputStream()));
	        out= new ObjectOutputStream(s.getOutputStream());
	        out1=s.getOutputStream();
	        in=s.getInputStream();
	        data=new DataInputStream(in);
	        buff=new BufferedInputStream(in);
	        String line="peer1";
	        out.writeObject(line);
			out.flush();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }
		
		FileOutputStream fos;
		int flag=00;
		
		
		
		try {
		
			
			//int fileSize= data.read();
			int fileSize=(int) ois.readObject();
			System.out.println(fileSize);
			ArrayList<File>files=new ArrayList<File>(fileSize); //store list of filename from client directory
            ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); //store file size from client
            //Start to accept those filename from server
            for (int count=0;count < fileSize;count ++){
                    File ff=new File(data.readUTF());
                    files.add(ff);
            }
             
            for (int count=0;count < fileSize;count ++){
                 
                    sizes.add(data.readInt());
            }
            for (int count =0;count < fileSize ;count ++){                 
               
  
               len=sizes.get(count);
                         
             System.out.println("File Size ="+len);
             
             out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer1\\" + files.get(count));
             dos=new DataOutputStream(out1);
             bos=new BufferedOutputStream(out1);
            
             byte[] buffer = new byte[1024]; 
               
             
             bos.write(buffer, 0, buffer.length); //This line is important
              
             while (len > 0 && (smblen = data.read(buffer)) > 0) { 
                 dos.write(buffer, 0, smblen); 
                   len = len - smblen;
                   dos.flush();
                 }  
               dos.close();  //It should close to avoid continue deploy by resource under view
            }   
//			
//			fos = new FileOutputStream(
//					"C:\\Users\\VIBHAV\\workspace\\p2p\\src\\peer1\\advanced.pdf");
//			InputStream in = s.getInputStream();
//			DataInputStream data = new DataInputStream(in);
//			long size = data.readLong();
//			byte[] buff = new byte[4096];
//			int bytes = 0;
//			while (size > 0 && (bytes = data.read(buff, 0, (int) Math.min(buff.length, size))) != -1) {
//				fos.write(buff, 0, bytes);
//				size -= bytes;
//			}
//			fos.close();
//			flag++;
			System.out.println("done");
//			System.out.println(flag);
		}
			catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	}
}