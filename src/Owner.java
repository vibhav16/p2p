
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
        ss2 = new ServerSocket(4445); 
        System.out.println("File Owner Started...");
        int Counter = 1;

    int sizeOfFiles = 1024 * 100;// 100 KB
    byte[] buffer = new byte[sizeOfFiles];
    File f=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\fileOwner\\advanced.pdf");

    String fileName = f.getName();

    try (FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = new BufferedInputStream(fis)) {

    int bytesAmount = 0;
    while ((bytesAmount = bis.read(buffer)) > 0) {
    String filePartName = String.format("%03d.%s", Counter++, fileName);
    File newFile = new File("C:\\Users\\VIBHAV\\workspace\\p2p\\src\\fileOwner\\chunks\\", filePartName);
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
    DataOutputStream dos=null;
      
    
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
        dos=new DataOutputStream(out);

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }


    try {
    	File file=new File("C:\\Users\\VIBHAV\\Workspace\\p2p\\src\\fileOwner\\chunks\\");
		File[] Files=file.listFiles();
		//System.out.println(Files.length);
		int total=Files.length;
		int mod=total%5;
		int firstfour=(total-mod)/5;	
		
			while (true) {
				String response=(String) input.readObject();
				if(response.equals("peer1")){				
				System.out.println("Distributing chunks to peer1");
				oos.writeObject(firstfour);
				for(int count=0;count<firstfour;count++){
					dos.writeUTF(Files[count].getName());
					System.out.println(Files[count].getName());
				}
				
				for(int count=0;count<firstfour;count++){
					int filesize=(int) Files[count].length();
					dos.writeInt(filesize);
				}
				for(int count=0;count<firstfour;count++){
					int filesize = (int) Files[count].length();
		            byte [] buffer = new byte [filesize];
		                 
		             
		            FileInputStream fis = new FileInputStream(Files[count].toString());  
		            BufferedInputStream bis = new BufferedInputStream(fis);      
		           
		            bis.read(buffer, 0, buffer.length); 
		             
		            dos.write(buffer, 0, buffer.length);   
		            dos.flush(); 
				}

				}
				else if(response.equals("peer2")){
					oos.writeObject(firstfour);
					System.out.println("Distributing chunks to peer2");
					for(int count=firstfour;count<2*firstfour;count++){
						dos.writeUTF(Files[count].getName());
						System.out.println(Files[count].getName());
					}
					
					for(int count=firstfour;count<2*firstfour;count++){
						int filesize=(int) Files[count].length();
						dos.writeInt(filesize);
					}
					for(int count=firstfour;count<2*firstfour;count++){
						int filesize = (int) Files[count].length();
			            byte [] buffer = new byte [filesize];
			                 
			            
			            FileInputStream fis = new FileInputStream(Files[count].toString());  
			            BufferedInputStream bis = new BufferedInputStream(fis);  
			         			              
			            bis.read(buffer, 0, buffer.length);			             
			            dos.write(buffer, 0, buffer.length); 			          
			            dos.flush(); 
					}
				}
				else if(response.equals("peer3")){
					oos.writeObject(firstfour);
					System.out.println("Distributing chunks to peer3");
					for(int count=2*firstfour;count<3*firstfour;count++){
						dos.writeUTF(Files[count].getName());
						System.out.println(Files[count].getName());
					}
					
					for(int count=2*firstfour;count<3*firstfour;count++){
						int filesize=(int) Files[count].length();
						dos.writeInt(filesize);
					}
					for(int count=2*firstfour;count<3*firstfour;count++){
						int filesize = (int) Files[count].length();
			            byte [] buffer = new byte [filesize];
			                 
			            //FileInputStream fis = new FileInputStream(myFile);  
			            FileInputStream fis = new FileInputStream(Files[count].toString());  
			            BufferedInputStream bis = new BufferedInputStream(fis);  
			         
			            
			            bis.read(buffer, 0, buffer.length); 			             
			            dos.write(buffer, 0, buffer.length);			          
			            dos.flush(); 
					}
				}
				else if(response.equals("peer4")){
					oos.writeObject(firstfour);
					System.out.println("Distributing chunks to peer4");
					for(int count=3*firstfour;count<4*firstfour;count++){
						dos.writeUTF(Files[count].getName());
						System.out.println(Files[count].getName());
					}
					
					for(int count=3*firstfour;count<4*firstfour;count++){
						int filesize=(int) Files[count].length();
						dos.writeInt(filesize);
					}
					for(int count=3*firstfour;count<4*firstfour;count++){
						int filesize = (int) Files[count].length();
			            byte [] buffer = new byte [filesize];
			                 
			            //FileInputStream fis = new FileInputStream(myFile);  
			            FileInputStream fis = new FileInputStream(Files[count].toString());  
			            BufferedInputStream bis = new BufferedInputStream(fis);  
			         
			             
			            bis.read(buffer, 0, buffer.length); 
			             
			            dos.write(buffer, 0, buffer.length); 
			          
			            dos.flush(); 
					}
				}
				
				else if(response.equals("peer5")){
					oos.writeObject(firstfour+mod);
					System.out.println("Distributing chunks to peer5");
					for(int count=4*firstfour;count<total;count++){
						dos.writeUTF(Files[count].getName());
						System.out.println(Files[count].getName());
					}
					
					for(int count=4*firstfour;count<total;count++){
						int filesize=(int) Files[count].length();
						dos.writeInt(filesize);
					}
					for(int count=4*firstfour;count<total;count++){
						int filesize = (int) Files[count].length();
			            byte [] buffer = new byte [filesize];
			                 
			            //FileInputStream fis = new FileInputStream(myFile);  
			            FileInputStream fis = new FileInputStream(Files[count].toString());  
			            BufferedInputStream bis = new BufferedInputStream(fis);  
			         
			           
			            bis.read(buffer, 0, buffer.length); 
			             
			            dos.write(buffer, 0, buffer.length); 
			          
			            dos.flush(); 
					}
				}
				else{
					System.out.println("No distribution");
				}
			}
			
        
    } catch (IOException e) {

        line=this.getName(); 
        System.out.println("IO Error/ Client "+line+" terminated abruptly");
    }
    catch(NullPointerException e){
        line=this.getName(); 
        System.out.println("Client "+line+" Closed");
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    finally{    
    try{
        System.out.println("Connection Closing..");
        if (is!=null){
            is.close(); 
            System.out.println(" Socket Input Stream Closed");
        }

        if(os!=null){
            os.close();
            System.out.println("Socket Out Closed");
        }
        if (s!=null){
        s.close();
        System.out.println("Socket Closed");
        }

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    
    }
    }
}
