/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiclient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ehsan
 */
public class ServerMessenger extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    
    private DataOutputStream output;
    private DataInputStream input;
    

    private Socket connection;
    
    //constructor
    public ServerMessenger(Socket c,DataOutputStream dos){     
    super("Chat messenger Server Part");
    
        System.out.println("servr on");
     connection = c;
     output=dos;
    userText = new JTextField("Enter Your Text");
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
        userText.setFont(font1);
       
    
    userText.addActionListener(
    new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
           
            sendMessage(e.getActionCommand());
            userText.setText("");
            throw new  UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    );
    add(userText , BorderLayout.PAGE_END);
    chatWindow = new CustomTextArea();
    chatWindow.setEditable(false);
    add(new JScrollPane(chatWindow));
   setSize(500,500);
   setVisible(true);
  startRunning();
    }
    //setup server
    public void startRunning()
    {
        try{
            
            while(true){
                try{
                    
                    setupStreams();
                    whileChatting();

                }catch (EOFException e) {

                    showMessage("\nServer ended\n");

                }
                finally{
                   // closeConnection();
                }
             }
        }
        catch(IOException ioException ){
            JOptionPane.showMessageDialog(this, "Client Ternimated the Connection");
        ioException.printStackTrace();
        }
    
    
    }
    //wait for connection, then display connection;
  
    // get streams to send and recieve data
    private void setupStreams() throws IOException{
    
    
    output.flush();
    input = new DataInputStream(connection.getInputStream());
    showMessage("streams are now setup\n");
    
    }
    //during the chat conversation
    private void whileChatting() throws IOException{
        String message = "You are now connected ";
        sendMessage(message);
        
        do{
            try{
                message = (String)input.readUTF();
                showMessage("\n" + message);
            }catch(Exception e){
                showMessage("ERROR\n");
                return;
            }
            

        }while(!message.equals("CLIENT - END"));

    
    }
    //close streams
    private void closeConnection(){
        showMessage("\nconnection closed\n");
       
        try{
            output.close();
            input.close();
            connection.close();
            
        
        }catch(IOException e){
            e.printStackTrace();
        
        }
    
    
    }
    //send message to a chat
    private void sendMessage(String message){
        try{
            output.writeUTF("SERVER - "+ message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }catch(IOException e){
        chatWindow.append("\nError \n");
        }
        
    }
    //updates window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);    
                }
            }
        
        );
        
    
    }
 
            
 class CustomTextArea extends JTextArea {

        private BufferedImage image;

        public CustomTextArea() {
            super(20,20);
            try {
                image = ImageIO.read(new File("a.jpg"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean isOpaque() {
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            if (image != null) {
                int x = getWidth() - image.getWidth();
                int y = getHeight() - image.getHeight();
                g2d.drawImage(image, x, y, this);    
            }
            super.paintComponent(g2d);
            g2d.dispose();
        }           
    
}
}