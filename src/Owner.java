
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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owner {
@SuppressWarnings("resource")
public static void main(String args[]){

    Socket s=null;
    ServerSocket ss2=null;
    try{
        ss2 = new ServerSocket(Integer.parseInt(args[0])); 
        System.out.println("File Owner Started...");
        int Counter = 1;

    int sizeOfFiles = 1024 * 100;// 100 KB
    byte[] buffer = new byte[sizeOfFiles];
    File f=new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\fileOwner\\test.pdf");

    String fileName = f.getName();

    try (FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = new BufferedInputStream(fis)) {

    int bytesAmount = 0;
    while ((bytesAmount = bis.read(buffer)) > 0) {
    String filePartName = String.format("%03d.%s", Counter++, fileName);
    File newFile = new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\fileOwner\\chunks\\", filePartName);
    try (FileOutputStream out = new FileOutputStream(newFile)) {
    out.write(buffer, 0, bytesAmount);
    
    }
    }
    System.out.println("File broken into chunks");
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
			System.out.println("Waiting...");
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
    ObjectOutputStream oos=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    OutputStream out = null;
    ObjectInputStream input=null;
    Socket s=null;
    // DataOutputStream dos=null;
      
    
    public ServerThread(Socket s) {
		this.s=s;
    }
    
    public void run() {

		try{
			oos=new ObjectOutputStream(s.getOutputStream());
			is= new BufferedReader(new InputStreamReader(s.getInputStream()));
			os=new PrintWriter(s.getOutputStream());
			input=new ObjectInputStream(s.getInputStream());
			out = s.getOutputStream();
			// dos=new DataOutputStream(out);

		}catch(IOException e){
			System.out.println("IO error in server thread");
		}


		try {
			File folder=new File("C:\\Users\\VIBHAV\\Desktop\\CNfinal\\CNfinal\\src\\fileOwner\\chunks\\");
			File[] Files=folder.listFiles();

			Arrays.sort(Files);

			//System.out.println(Files.length);
			int total=Files.length;
			int mod=total%5;
			int firstfour=(total-mod)/5;	

			while (true) {
				
				String chunksRequest = input.readUTF();
				if(chunksRequest.equals("chunks_request")){	

					System.out.println("Received Chunks request");

					oos.writeInt(total);
					oos.flush();
				}

				String response= input.readUTF();

				if(response.equals("peer1")){

					System.out.println("Distributing chunks to " + response);
					oos.writeObject(firstfour);
					oos.flush();

					int startIndex = 0;
					int lastFileIndex = firstfour;

					for(int count=startIndex; count<lastFileIndex; count++){
						String fileName = Files[count].getName();
						oos.writeUTF(fileName);
						oos.flush();

						int filesize=(int) Files[count].length();
						oos.writeInt(filesize);
						oos.flush();

						byte [] buffer = new byte [filesize];
							
						FileInputStream fis = new FileInputStream(Files[count].toString());  
						BufferedInputStream bis = new BufferedInputStream(fis);      
					
						bis.read(buffer, 0, buffer.length); 
						
						oos.write(buffer, 0, buffer.length);   
						oos.flush(); 

						System.out.println(fileName);
					}
				}
				else if(response.equals("peer2")){

					System.out.println("Distributing chunks to " + response);
					oos.writeObject(firstfour);
					oos.flush();

					int startIndex = firstfour;
					int lastFileIndex = 2 * firstfour;

					for(int count=startIndex; count<lastFileIndex; count++){
						String fileName = Files[count].getName();
						oos.writeUTF(fileName);
						oos.flush();

						int filesize=(int) Files[count].length();
						oos.writeInt(filesize);
						oos.flush();

						byte [] buffer = new byte [filesize];
							
						FileInputStream fis = new FileInputStream(Files[count].toString());  
						BufferedInputStream bis = new BufferedInputStream(fis);      
					
						bis.read(buffer, 0, buffer.length); 
						
						oos.write(buffer, 0, buffer.length);   
						oos.flush(); 

						System.out.println(fileName);
					}

				}
				else if(response.equals("peer3")){

					System.out.println("Distributing chunks to " + response);
					oos.writeObject(firstfour);
					oos.flush();

					int startIndex = 2*firstfour;
					int lastFileIndex = 3 * firstfour;

					for(int count=startIndex; count<lastFileIndex; count++){
						String fileName = Files[count].getName();
						oos.writeUTF(fileName);
						oos.flush();

						int filesize=(int) Files[count].length();
						oos.writeInt(filesize);
						oos.flush();

						byte [] buffer = new byte [filesize];
							
						FileInputStream fis = new FileInputStream(Files[count].toString());  
						BufferedInputStream bis = new BufferedInputStream(fis);      
					
						bis.read(buffer, 0, buffer.length); 
						
						oos.write(buffer, 0, buffer.length);   
						oos.flush(); 

						System.out.println(fileName);
					}

				}
				else if(response.equals("peer4")){

					System.out.println("Distributing chunks to " + response);
					oos.writeObject(firstfour);
					oos.flush();

					int startIndex = 3*firstfour;
					int lastFileIndex = 4 * firstfour;

					for(int count=startIndex; count<lastFileIndex; count++){
						String fileName = Files[count].getName();
						oos.writeUTF(fileName);
						oos.flush();

						int filesize=(int) Files[count].length();
						oos.writeInt(filesize);
						oos.flush();

						byte [] buffer = new byte [filesize];
							
						FileInputStream fis = new FileInputStream(Files[count].toString());  
						BufferedInputStream bis = new BufferedInputStream(fis);      
					
						bis.read(buffer, 0, buffer.length); 
						
						oos.write(buffer, 0, buffer.length);   
						oos.flush(); 

						System.out.println(fileName);
					}

				}
				else if(response.equals("peer5")){

					System.out.println("Distributing chunks to " + response);
					oos.writeObject(firstfour+mod);
					oos.flush();

					int startIndex = 4*firstfour;
					int lastFileIndex = total;

					for(int count=startIndex; count<lastFileIndex; count++){
						String fileName = Files[count].getName();
						oos.writeUTF(fileName);
						oos.flush();

						int filesize=(int) Files[count].length();
						oos.writeInt(filesize);
						oos.flush();

						byte [] buffer = new byte [filesize];
							
						FileInputStream fis = new FileInputStream(Files[count].toString());  
						BufferedInputStream bis = new BufferedInputStream(fis);      
					
						bis.read(buffer, 0, buffer.length); 
						
						oos.write(buffer, 0, buffer.length);   
						oos.flush(); 

						System.out.println(fileName);
					}
				}
				else{
					System.out.println("No distribution");
				}

				System.out.println("Sent all chunks");
				break;
			}
			
		} catch (IOException e) {

			line=this.getName(); 
			e.printStackTrace();
			System.out.println("IO Error/ Client "+line+" terminated abruptly");
		}
		catch(NullPointerException e){
			line=this.getName(); 
			System.out.println("Client "+line+" Closed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    
	}
}
