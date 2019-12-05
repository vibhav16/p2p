
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

public class Peer3 {
 
	@SuppressWarnings("resource")
	public static void main(String args[]) throws IOException, InterruptedException, ExecutionException{
		
		SharedObject sharedObject = new SharedObject();

		InetAddress address=InetAddress.getLocalHost();
		Socket s1=null;

		//Socket s2=null;
		Socket s3=null;
		boolean check=true;
		int port=Integer.parseInt(args[0]);
		int port2=Integer.parseInt(args[1]);

		try{   	
					
			s1=new Socket(address,port);
			peer3ownerthread owner = new peer3ownerthread(s1, sharedObject);
			owner.start();

			//System.out.println(sharedObject);

			while(sharedObject.numberOfChunks == -1){
								
				// System.out.println("Initialzing...");

			}

			ServerThread3 serverThread2 = new ServerThread3(Integer.parseInt(args[2]));
			serverThread2.start();

			Socket s2 = null;
			
			while(s2 == null || (s2 != null && !s2.isConnected())){

				try{

					s2 = new Socket(address, port2);
					downloadPeer3 peer1=new downloadPeer3(s2, sharedObject.numberOfChunks);
					peer1.start();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					// System.out.println("Unable to create upload thread");
				}

			}	
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}

// class SharedObject {

// 	public volatile Integer numberOfChunks = -1;

// }

class downloadPeer3 extends Thread{
	
	Socket s=null;
	ObjectOutputStream out = null;
	ObjectInputStream ois=null;
    int leftChunks;

	public downloadPeer3(Socket s, int numberOfChunks)
	{
		this.s=s;
		this.leftChunks = numberOfChunks;
	}

	public void run(){
		
		
		System.out.println("requesting files from peer 2");
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			while(leftChunks > 0){

				sendCurrentChunkList();
				downloadChunks();

				System.out.println("Remaining chunks - ------------------------------" + leftChunks);

				Thread.sleep(2000);
			}
			System.out.println("Chunks Downloaded");

			FileOutputStream out1 = null;
			DataOutputStream dos = null;
			File folder = new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\");
			File[] Files = folder.listFiles();

			Arrays.sort(Files);

            String filecheck=Files[0].getName().split("\\.")[2];

			out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\test."+filecheck, true);			dos = new DataOutputStream(out1);
			


			int offset = 0;

			for(File file: Files){

				int fileSize = (int) file.length();

				byte [] buffer = new byte [fileSize];
						
				FileInputStream fis = new FileInputStream(file.toString());  
				BufferedInputStream bis = new BufferedInputStream(fis);      
				
				while (bis.read(buffer, 0, buffer.length) > 0) {

					dos.write(buffer, 0, buffer.length); 
					dos.flush();
				}  

				offset += fileSize;
			}

			dos.close();  

			System.out.println("File Merged");


		} catch (IOException | ClassNotFoundException| InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendCurrentChunkList() throws IOException{
		System.out.println("Sending chunk list to peer 4");
		File file = new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\");
		File[] Files =file.listFiles();
		ArrayList<String> arr = new ArrayList<>();

		for(int i=0;i<Files.length;i++)
		{
			arr.add(Files[i].getName());
		}

		out.writeObject(arr);
		out.flush();

		// System.out.println(arr);
		System.out.println("peer 3 sent request to peer 2");
	}

	private void downloadChunks() throws IOException, ClassNotFoundException{
		System.out.println("Comparing the received list from peer2");
		int fileSize = (int) ois.readObject();
			//System.out.println(fileSize);
		// ArrayList<File>files=new ArrayList<File>(fileSize); 
		// ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); 
		FileOutputStream out1 = null;
		DataOutputStream dos = null;

		for (int count = 0; count < fileSize;count ++){
			//System.out.println("checking");

			String name = ois.readUTF();

			int totalSize = ois.readInt();

			byte[] buffer = new byte[1024]; 

			int smblen;

			out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\" + name);
			dos = new DataOutputStream(out1);

			while (totalSize > 0 && (smblen = ois.read(buffer)) > 0) {

				dos.write(buffer, 0, smblen); 
				totalSize = totalSize - smblen;
				dos.flush();
			}  
			
			dos.close();  

			// files.add(ff);
			System.out.println("Downloaded chunk " +name+" from peer 2");
			// System.out.println("Comparing the list of peer 1 and peer 5");
		}  

		leftChunks -= fileSize;
	}
}


class ServerThread3 extends Thread{

	ServerSocket ss2;

	public ServerThread3(int port) throws Exception{
		ss2 = new ServerSocket(port);
	}

	public void run(){

		while(true){
			
			System.out.println("PEER 4 ACCEPTING");

			try{
				Socket s3=ss2.accept();
				System.out.println("Peer 3 connected to Peer 4");

				uploadthread3 upload=new uploadthread3(s3);
				upload.start();  
			}
			catch(Exception e){
				System.out.println("Unable to connect");
			}
		}
	}
}

class uploadthread3 extends Thread{
	
	Socket s=null;
	ObjectInputStream in=null;
	ObjectOutputStream oos=null;

	public uploadthread3(Socket s)
	{
		this.s=s;
	}

	public void run(){
			
		try{
			in = new ObjectInputStream(s.getInputStream());
			oos=new ObjectOutputStream(s.getOutputStream());
			
			while(true)
			{
				ArrayList<String> currentFileNames=(ArrayList<String>) in.readObject();
				System.out.println("Request received from Peer 4");

				File folder=new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\");
				File[] files=folder.listFiles();
				
				ArrayList<File> sendingFiles=new ArrayList<>();
				
				for(File file: files)
				{
					String fileName = file.getName();

					if(!currentFileNames.contains(fileName))
						sendingFiles.add(file);
				}
			
				System.out.println(sendingFiles.size());

				System.out.println("Distributing chunks to Peer 4");
				oos.writeObject(sendingFiles.size());
				oos.flush();

				for(File file: sendingFiles){

					String fileName = file.getName();
					oos.writeUTF(fileName);
					oos.flush();

					int filesize=(int) file.length();
					oos.writeInt(filesize);
					oos.flush();

					byte [] buffer = new byte [filesize];
						
					FileInputStream fis = new FileInputStream(file.toString());  
					BufferedInputStream bis = new BufferedInputStream(fis);      
				
					bis.read(buffer, 0, buffer.length); 
					
					oos.write(buffer, 0, buffer.length);   
					oos.flush(); 

					// System.out.println(fileName);
				}
			}
		}
		catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}

class peer3ownerthread extends Thread{
	
	SharedObject sharedObject;

	Socket s=null;
	ObjectOutputStream out = null;
	ObjectInputStream ois=null;
	int totalChunks;

	public peer3ownerthread(Socket s, SharedObject sharedObject)
	{
		this.s = s;
		this.sharedObject = sharedObject;
	}

	public void run(){
		System.out.println("Connected to file owner");

		try {

			out = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			requestTotalNumberOfChunks();
			sendChunkRequestToOwner();

			downloadChunks();

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void requestTotalNumberOfChunks() throws IOException{

		System.out.println("Requesting number of chunks");

		String line="chunks_request";
		out.writeUTF(line);
		out.flush();

		totalChunks = ois.readInt();

		//System.out.println(sharedObject);
		// System.out.println(Peer1.numberOfChunks);
	}

	private void sendChunkRequestToOwner() throws IOException{
		String line="peer3";
		out.writeUTF(line);
		out.flush();
	}

	private void downloadChunks() throws IOException, ClassNotFoundException{

		int fileSize = (int) ois.readObject();

		//System.out.println(fileSize);
			//System.out.println(fileSize);
		// ArrayList<File>files=new ArrayList<File>(fileSize); 
		// ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); 
		FileOutputStream out1 = null;
		DataOutputStream dos = null;

		for (int count = 0; count < fileSize;count ++){
			//System.out.println("checking");

			String name = ois.readUTF();

			int totalSize = ois.readInt();

			byte[] buffer = new byte[1024]; 

			int smblen;

			out1 = new FileOutputStream("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\" + name);
			dos = new DataOutputStream(out1);

			while (totalSize > 0 && (smblen = ois.read(buffer)) > 0) {

				dos.write(buffer, 0, smblen); 
				totalSize = totalSize - smblen;
				dos.flush();
			}  
			
			dos.close();  

			// files.add(ff);
			System.out.println("Downloaded chunk " +name+" from Owner");
			// System.out.println("Comparing the list of peer 1 and peer 5");
		}  

		ArrayList<String> arr=new ArrayList<>();
		File file=new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3\\");
		File[] files=file.listFiles();
		for(int i=0;i<fileSize;i++)
		{
			arr.add(files[i].getName());
		}
		PrintWriter pw=new PrintWriter(new FileOutputStream("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\peer3.txt"));
		for(String str: arr)
		{
			pw.println(str);
		}
		pw.close();
		System.out.println("Summary file of peer3 created");


		
		sharedObject.numberOfChunks = totalChunks - fileSize;
	}
}