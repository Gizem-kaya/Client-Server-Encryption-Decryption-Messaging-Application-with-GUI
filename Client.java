
/**
* The Client/Server Messenger Program 
* developed with java socket programming and swing.
*
* @author  Gizem Kaya
* @version 1.0
* @since   2020-11-17 
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;




public class Client implements ActionListener, Runnable {
	
    Socket sk;
    BufferedReader br;
    PrintWriter pw;
    Gui gui;
    String name;
    
    public Client() {
    	 
    	this.gui = new Gui(this);   
    	
    	
    }
    public void serverConnection() {

    	try {

		String IP = "127.0.0.1";
		sk = new Socket(IP, 1234);

		this.name = JOptionPane.showInputDialog(this.gui.frame, "Enter username:", JOptionPane.INFORMATION_MESSAGE);

		//read
		br = new BufferedReader(new InputStreamReader(sk.getInputStream()));

		//writing
		pw = new PrintWriter(sk.getOutputStream(), true);
		pw.println(this.name); // Send to server side

		new Thread(this).start();

        } catch (Exception e) {
            System.out.println(e + " Socket Connection error");
        }		
    }
    
   
    
    
    public static void main(String[] args) {
        new Client(); //Method call at the same time object creation
        
        
    }

    public void run() {
        String data = null;
        try {
            while ((data = br.readLine()) != null) {
                this.gui.textArea.append(data + "\n"); 		//textArea Decrease the position of the box's scroll bar by the length of the text entered
                this.gui.textArea.setCaretPosition(this.gui.textArea.getText().length());
                
            }
        } catch (Exception e) {
            System.out.println(e + "--> Client run fail");
        }
    }
    

    public void actionPerformed(ActionEvent e) {
        String data = this.gui.text.getText();
        pw.println(data); // Send to server side
        this.gui.text.setText("");
    }
   
}
