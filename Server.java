/**
* The Client/Server Messenger Program 
* developed with java socket programming and swing.
*
* @author  Gizem Kaya
* @version 1.0
* @since   2020-11-17 
*/


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.NoSuchPaddingException;

public class Server {

    ServerSocket server;
    Socket sk;
    InetAddress addr;

    ArrayList<ServerThread> list = new ArrayList<ServerThread>();

    public Server() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {
        	
       	 	File logFile = new File("log.txt");			// Create a log file
            if (!logFile.createNewFile()) {				// If it already exists, delete the old file and create a new one.
                logFile.delete();
                logFile.createNewFile();
              }

       	 	
        	addr = InetAddress.getByName("127.0.0.1");
        	            
            server = new ServerSocket(1234,50,addr);
            // key and initialization vector creation
            Crypto crypto = Crypto.getInstance();

            /**
             * 
             * Key and iv declarations to the log file
             * 
             */
            String desInfo = "************** DES  **************\nKEY    " + crypto.get_DES_key()+ "\nIV  " + crypto.get_DES_IV() + "\n";
            String aesInfo = "\n************** AES  **************\nKEY    " + crypto.get_AES_key()+ "\nIV  " + crypto.get_AES_IV() + "\n";
            try {     
                BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt",true));
                bw.write(desInfo + aesInfo + "\n");
                bw.close();
            } catch(IOException e){
                e.printStackTrace();
            }

            // DES key and initialization vector declaration to console
            System.out.printf(desInfo);
            // AES key and initialization vector declaration to console
            System.out.printf(aesInfo);

            System.out.println("\n Waiting for Client connection");   
            while(true) {
                sk = server.accept();
                System.out.println(sk.getInetAddress() + " connect");

                //Thread connected clients to ArrayList
                ServerThread st = new ServerThread(this);
                addThread(st);
                st.start();
            }
        } catch(IOException e) {
            System.out.println(e + "-> ServerSocket failed");
        }
    }

    public void addThread(ServerThread st) {
        list.add(st);
    }

    public void removeThread(ServerThread st){
        list.remove(st); //remove
    }

    public void broadCast(String message){
        for(ServerThread st : list){
            st.pw.println(message);
        }
    }

    public static void main(String[] args)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        new Server();
    }
}

class ServerThread extends Thread {
	
    Server server;
    PrintWriter pw;
    String name;

    public ServerThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // read
            BufferedReader br = new BufferedReader(new InputStreamReader(server.sk.getInputStream()));

            // writing
            pw = new PrintWriter(server.sk.getOutputStream(), true);
            name = br.readLine();
            //server.broadCast("**["+name+"] Entered**");

            String data;
            while((data = br.readLine()) != null ){
                if(data == "/list"){
                    pw.println("a");
                }
                server.broadCast(data);	// MESSAGE
 
                try {            						// CREATE LOG.TXT OR APPEND TO LOG.TXT

                    FileWriter fileWritter = new FileWriter("log.txt",true);
                    BufferedWriter bw = new BufferedWriter(fileWritter);
                    bw.write(data + "\n");
                    bw.close();
                    
                 } catch(IOException e){
                    e.printStackTrace();
                 }
                
            }
        } catch (Exception e) {

            server.removeThread(this);
            //server.broadCast(name + "is left");
            System.out.println(server.sk.getInetAddress()+ name + "is left");
            System.out.println(e + "---->");
        }
        
        
        
    }
}