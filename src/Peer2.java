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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Peer2 {

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
        peer2ownerthread owner=new peer2ownerthread(s1);
    	owner.start();
        s2=new Socket(address, 4446);
        downloadPeer2 peer2=new downloadPeer2(s2);
		peer2.start();
        ss3=new ServerSocket(4447);
       
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }
    while(true)
    {
    	s3=ss3.accept();
    	System.out.println("peer2 connected to peer3");
    	uploadthread2 upload=new uploadthread2(s3);
    	upload.start();  
    }
}
}

class uploadthread2 extends Thread{
	Socket s=null;
	public uploadthread2(Socket s)
	{
		this.s=s;
	}
	public void run(){
		//System.out.println(" upload connection thread done");
		
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
				System.out.println("Request from peer 3 recieved");
				File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer2\\");
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
						System.out.println("sending chunk"+count+"to peer 3");
						System.out.println("comparing the lists of peer2 and peer3");
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

class downloadPeer2 extends Thread{
	Socket s=null;
	public downloadPeer2(Socket s)
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
		System.out.println("requesting files from peer1");
		try {
			ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
			ois=new ObjectInputStream(s.getInputStream());
			in=s.getInputStream();
	        data=new DataInputStream(in);
	        out1=s.getOutputStream();
			File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer2\\");
			File[] Files=file.listFiles();
			ArrayList<String> arr=new ArrayList<>();
			for(int i=0;i<Files.length;i++)
			{
				arr.add(Files[i].getName());
			}
			out.writeObject(arr);
			out.flush();
			System.out.println("peer 2 sent request to peer 1");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			int fileSize=(int) ois.readObject();
			//System.out.println(fileSize);
			ArrayList<File>files=new ArrayList<File>(fileSize); //store list of filename from client directory
            ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); //store file size from client
            //Start to accept those filename from server
            for (int count=0;count < fileSize;count ++){
            	//System.out.println("checking");
                    File ff=new File(data.readUTF());
                    files.add(ff);
                    System.out.println("downloading chunk " +count+" from peer 1");
                    System.out.println("comaparing the list of peer 2 and peer 1");
            
            }
             
            for (int count=0;count < fileSize;count ++){
                 
                    sizes.add(data.readInt());
            }
            for (int count =0;count < fileSize ;count ++){                 
               
  
               int len=sizes.get(count);
                         
            // System.out.println("File Size ="+len);
             
             out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer2\\" + files.get(count));
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
			System.out.println("files downloaded from peer1");
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


class peer2ownerthread extends Thread{
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
	
	public peer2ownerthread(Socket s)
	{
		this.s=s;
	}
	public void run(){
		//System.out.println("owner connection thread done");
		
		try {
	       ois=new ObjectInputStream(s.getInputStream());
	        br= new BufferedReader(new InputStreamReader(System.in));
	        is=new BufferedReader(new InputStreamReader(s.getInputStream()));
	        out= new ObjectOutputStream(s.getOutputStream());
	        out1=s.getOutputStream();
	        in=s.getInputStream();
	        data=new DataInputStream(in);
	        String line="peer2";
	        out.writeObject(line);
			out.flush();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }
		
		FileOutputStream fos;
		try {
			//int fileSize= data.read();
			int fileSize=(int) ois.readObject();
			//System.out.println(fileSize);
			ArrayList<File>files=new ArrayList<File>(fileSize); 
            ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); 
            
            for (int count=0;count < fileSize;count ++){
            	//System.out.println("checking");
                    File ff=new File(data.readUTF());
                    files.add(ff);
            }
             
            for (int count=0;count < fileSize;count ++){
                 
                    sizes.add(data.readInt());
            }
            for (int count =0;count < fileSize ;count ++){                 
               
  
               len=sizes.get(count);
                         
             //System.out.println("File Size ="+len);
             
             out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer2\\" + files.get(count));
             dos=new DataOutputStream(out1);
             bos=new BufferedOutputStream(out1);
            
             byte[] buffer = new byte[1024]; 
               
             
             bos.write(buffer, 0, buffer.length); 
              
             while (len > 0 && (smblen = data.read(buffer)) > 0) { 
                 dos.write(buffer, 0, smblen); 
                   len = len - smblen;
                   dos.flush();
                 }  
               dos.close(); 
            }   
			//System.out.println("done");
			ArrayList<String> arr=new ArrayList<>();
			 File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\peer2\\");
				File[] Files=file.listFiles();
				for(int i=0;i<fileSize;i++){
					arr.add(Files[i].getName().split("\\.")[0]);
				}
			 
			 PrintWriter pw = new PrintWriter(new FileOutputStream("C:\\Users\\VIBHAV\\workspace\\p2p\\src\\peer2.txt"));
			
			    for (String str : arr)
			        pw.println(str);
			    pw.close();
		} catch (FileNotFoundException e) {
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