
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

public class FTPClient4 {

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
        s2=new Socket(address, 4448);
        ss3=new ServerSocket(4449);
       
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