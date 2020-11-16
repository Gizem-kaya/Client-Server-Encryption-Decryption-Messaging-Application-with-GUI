/**
* The Client/Server Messenger Program 
* developed with java socket programming and swing.
*
* @author  Gizem Kaya
* @version 1.0
* @since   2020-11-17 
*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class Gui {
	
	JFrame frame = new JFrame("Cyrpto Messenger");			// Title of the frame
	JPanel mainPanel = new JPanel();						// Main panel
	JPanel serverPanel = new JPanel();						// Server Panel
	JPanel methodPanel = new JPanel();						// Methods box panel
	JPanel modePanel = new JPanel();						// Modes box panel
	JPanel textsPanel = new JPanel();
	JPanel textInputPanel = new JPanel();
	JPanel textEncryptedPanel = new JPanel();
	
	JTextArea textArea = new JTextArea();	
	JTextField text = new JTextField(15);
	JTextArea cryptedText = new JTextArea(3,15);
	
	String data;
	String encryptedData = "";
	
	public Gui(Client client){
		
		/*
		 * Creation of the objects
		 */
		
		JButton connectButton = new JButton("Connect");				//Button Connect
		JButton disconnectButton = new JButton("Disconnect");		// Button Disconnect
		JButton encryptButton = new JButton("Encrypt");				//Button Connect
		JButton sendButton = new JButton("Send");
				
		
		JLabel footer = new JLabel("Not Connected");
		
		JRadioButton methodAes = new JRadioButton("AES");			
		JRadioButton methodDes = new JRadioButton("DES");
		JRadioButton modeCBC = new JRadioButton("CBC");
		JRadioButton modeOFB = new JRadioButton("OFB");
		
		
		disconnectButton.setEnabled(false);			// When Gui opens, dc button will be disabled.
 		connectButton.setEnabled(true);				// When Gui opens, c button will be enabled.
 		sendButton.setEnabled(false);				
 		encryptButton.setEnabled(true);		
 		methodAes.setSelected(true);
 		modeCBC.setSelected(true);
		
		ButtonGroup Methods = new ButtonGroup();   // To ensure one of the radio button is selected.
		Methods.add(methodAes);
		Methods.add(methodDes);

		ButtonGroup Modes = new ButtonGroup();   // To ensure one of the radio button is selected.
		Modes.add(modeCBC);
		Modes.add(modeOFB);
   
        textArea.setEditable(false);
		cryptedText.setEditable(false);
		
		/*
		 * Titles and sizes of the panels
		 */
        
		methodPanel.setBorder(new TitledBorder("Method"));		
		methodPanel.setPreferredSize(new Dimension(100, 100));
		modePanel.setBorder(new TitledBorder("Mode"));		
		modePanel.setPreferredSize(new Dimension(100, 100));
		serverPanel.setBorder(new TitledBorder("Server"));		
		serverPanel.setPreferredSize(new Dimension(560, 130));		
		textsPanel.setPreferredSize(new Dimension(560, 120));
		textInputPanel.setBorder(new TitledBorder("Text"));
		textInputPanel.setPreferredSize(new Dimension(200,80));
		textEncryptedPanel.setBorder(new TitledBorder("Crypted Text"));
		textEncryptedPanel.setPreferredSize(new Dimension(200, 90));
		sendButton.setPreferredSize(new Dimension(70, 40));
		
	
		
		/*
		 * Add functions
		 */
		
		serverPanel.add(connectButton);
		serverPanel.add(disconnectButton);	
		
		methodPanel.add(methodAes);
		methodPanel.add(methodDes);	
		
		modePanel.add(modeCBC);
		modePanel.add(modeOFB);
		
		serverPanel.add(methodPanel);
		serverPanel.add(modePanel);
	
		textInputPanel.add(text);
		textEncryptedPanel.add(cryptedText);
		
		textsPanel.add(textInputPanel);
		textsPanel.add(textEncryptedPanel);
		textsPanel.add(encryptButton);
		textsPanel.add(sendButton);
		textsPanel.add(footer);
		
		mainPanel.add(serverPanel);
		mainPanel.add(textArea);
		mainPanel.add(textsPanel);
		
		
		/*
		 * ActionListeners
		 * 
		 */
		
		connectButton.addActionListener(new ActionListener(){  
 			public void actionPerformed(ActionEvent e){         		       		
 		 		connectButton.setEnabled(false);
 		 		disconnectButton.setEnabled(true);
 		 		client.serverConnection();
 		 		footer.setText("Connected: " + client.name);
 		     }  
 		});  
	
 		disconnectButton.addActionListener(new ActionListener(){  
 			public void actionPerformed(ActionEvent e){         		       		
 				disconnectButton.setEnabled(false);	
 				connectButton.setEnabled(true);
 				frame.dispose();
 		     }  
 		}); 
 		
 		encryptButton.addActionListener(new ActionListener(){  
 			public void actionPerformed(ActionEvent e){
 				if(client.name != null) {
 	 				encryptButton.setEnabled(false);
 	 				sendButton.setEnabled(true); 			
 	 				data = client.name + "> " + text.getText();		// TAKE THE ORIGINAL TEXT and CONCATENATE WITH USERNAME	 				
 	 				if (methodAes.isSelected() ) {
 	 					if(modeCBC.isSelected()) 
 	 						encryptedData = client.name + "> encrypted with AES+CBC";	// DO ENCRYPTION			
 	 					else 
 	 						encryptedData = client.name + "> encrypted with AES+OFB";	// DO ENCRYPTION
 	 					
 	 				}else {
 	 					if(modeCBC.isSelected()) 
 	 						encryptedData = client.name + "> encrypted with DES+CBC";	// DO ENCRYPTION			
 	 					else 
 	 						encryptedData = client.name + "> encrypted with DES+OFB";	// DO ENCRYPTION
 	 					
 	 				}
 	 							
 	 				cryptedText.append(encryptedData + "\n"); //textArea Decrease the position of the box's scroll bar by the length of the text entered
 	                cryptedText.setCaretPosition(cryptedText.getText().length());
 	 		        text.setText("");
 	 		      
 				}
 				else {
 					connectButton.setEnabled(false);
 	 		 		disconnectButton.setEnabled(true);
 	 		 		client.serverConnection();
 	 		 		footer.setText("Connected: " + client.name);
 				}
 		        
 		 		
 		     }  
 		});  
	
 		sendButton.addActionListener(new ActionListener(){  
 			public void actionPerformed(ActionEvent e){         		       		
	 				sendButton.setEnabled(false);	
	 				encryptButton.setEnabled(true);
	 				client.pw.println(encryptedData);	//  SEND ENCRYPTED TEXT TO SERVER
	 				client.pw.println(data); 			//  SEND TEXT TO SERVER	
	 				data = "";
	 				encryptedData = "";
		 		    cryptedText.setText("");
	 		} 
					 
 		}); 
		
		
		/*
		 * Add all to the frame
		 */
 		
 		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel);
		frame.setSize(600, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		text.requestFocus(); 			// Place cursor at run time
		
	}

}
