
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
import java.io.FileWriter;
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
    	uploadthread1 upload=new uploadthread1(s3);
    	upload.start();    		  
    	
    		Thread.sleep(9000);
    		Socket s2=new Socket(address, 4450);
    		downloadPeer1 peer1=new downloadPeer1(s2);
    		peer1.start();
    					
    	}   
    	
    }	 
 }


class downloadPeer1 extends Thread{
	Socket s=null;
	public downloadPeer1(Socket s)
	{
		this.s=s;
	}
	public void run(){
		ObjectInputStream ois=null;
		InputStream in=null;
		DataInputStream data=null;
		OutputStream out1=null;
		BufferedOutputStream bos=null;
		DataOutputStream dos=null;
		int smblen;
		System.out.println("file bhejo......");
		try {
			ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
			ois=new ObjectInputStream(s.getInputStream());
			in=s.getInputStream();
	        data=new DataInputStream(in);
	        out1=s.getOutputStream();
			File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer1\\");
			File[] Files=file.listFiles();
			ArrayList<String> arr=new ArrayList<>();
			for(int i=0;i<Files.length;i++)
			{
				arr.add(Files[i].getName());
			}
			out.writeObject(arr);
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			int fileSize=(int) ois.readObject();
			System.out.println(fileSize);
			ArrayList<File>files=new ArrayList<File>(fileSize); //store list of filename from client directory
            ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); //store file size from client
            //Start to accept those filename from server
            for (int count=0;count < fileSize;count ++){
            	System.out.println("checking");
                    File ff=new File(data.readUTF());
                    files.add(ff);
            }
             
            for (int count=0;count < fileSize;count ++){
                 
                    sizes.add(data.readInt());
            }
            for (int count =0;count < fileSize ;count ++){                 
               
  
               int len=sizes.get(count);
                         
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
			System.out.println("done");
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

class uploadthread1 extends Thread{
	Socket s=null;
	public uploadthread1(Socket s)
	{
		this.s=s;
	}
	public void run(){
		System.out.println(" upload connection thread done");
		
			ObjectInputStream in=null;
			ObjectOutputStream oos=null;
			 OutputStream out = null;
			    DataOutputStream dos=null;

			
			try {
				in = new ObjectInputStream(s.getInputStream());
				oos=new ObjectOutputStream(s.getOutputStream());
				out = s.getOutputStream();
		        dos=new DataOutputStream(out);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
			while(true)
			{
				ArrayList<String> arr=(ArrayList<String>) in.readObject();
				File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer1\\");
				File[] Files=file.listFiles();
				ArrayList<String> check=new ArrayList<>();
				
				for(int i=0;i<Files.length;i++)
				{
					check.add(Files[i].getName());
				}
				if(arr.equals(check)){
					System.out.println("we have same files!!!");
				}
				else{
					oos.writeObject(Files.length);
					for(int count=0;count<Files.length;count++){
						dos.writeUTF(Files[count].getName());
						System.out.println(Files[count].getName());
					}
					
					for(int count=0;count<Files.length;count++){
						int filesize=(int) Files[count].length();
						dos.writeInt(filesize);
					}
					for(int count=0;count<Files.length;count++){
						int filesize = (int) Files[count].length();
			            byte [] buffer = new byte [filesize];
			                 
			            //FileInputStream fis = new FileInputStream(myFile);  
			            FileInputStream fis = new FileInputStream(Files[count].toString());  
			            BufferedInputStream bis = new BufferedInputStream(fis);  
			         
			            //Sending file name and file size to the server  
			            bis.read(buffer, 0, buffer.length); //This line is important
			             
			            dos.write(buffer, 0, buffer.length);   
			            dos.flush(); 
					}
				}
				
			}
			}
			catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		
		
		
	
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
			 ArrayList<String> arr=new ArrayList<>();
			 File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer1\\");
				File[] Files=file.listFiles();
				for(int i=0;i<fileSize;i++){
					arr.add(Files[i].getName().split("\\.")[0]);
				}
			 
			 PrintWriter pw = new PrintWriter(new FileOutputStream("C:\\Users\\VIBHAV\\workspace\\p2p\\src\\peer1.txt"));
			
			    for (String str : arr)
			        pw.println(str);
			    pw.close();
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