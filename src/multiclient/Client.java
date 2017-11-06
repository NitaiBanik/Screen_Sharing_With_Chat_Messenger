/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiclient;

/**
 *
 * @author Ehsan
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ehsan
 */
public class Client extends JFrame {

    private JTextField userText;

    private boolean ipClicked = false;
    private JTextArea chatWindow;
    private DataOutputStream output;
    private DataInputStream input;
    private String message = "",en="ABC";
    private String serverIP;
    public Socket connection;
    ///constructor

    public Client(Socket c,DataInputStream dis) {
        super("Chat Messenger Client Part");
//        serverIP = host;
//        ipClicked= false;
        connection = c;
        input = dis;
        System.out.println("client on");

        userText = new JTextField("Enter Your Text");
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        userText.setFont(font1);

        userText.setEditable(true);
        userText.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );

        add(userText, BorderLayout.PAGE_END);
        chatWindow = new CustomTextArea();
        chatWindow.setEditable(false); 
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);
        startRunning();
    }

    //check ip input

    public boolean checkIP() {

        return ipClicked;

    }

    //setup server

    public void startRunning() {
        try {
            System.out.println("1");
            setupStreams();
            System.out.println("2");
            whileChatting();
        } 
        catch(Exception e){
            System.out.println("close"+e);
        
        }finally {
           // closeConnection();

        }

    }
    //connct to server

    // get streams to send and recieve data
    private void setupStreams() throws IOException {
        try{Thread.sleep(1000);}
        catch(Exception e){}
        output = new DataOutputStream(connection.getOutputStream());
        output.flush();
 
        showMessage("Streams are now setup\n");

    }

    //during the chat conversation

    private void whileChatting() throws IOException {
 
        do {
            try {
                message = (String) input.readUTF();
                 showMessage("\n" + message);
            } catch (Exception e) {
                showMessage("\nServer Closed the connection\n");

            }

        } while (!message.equals("SERVER - END"));

    }

    //close streams

    private void closeConnection() {
        showMessage("\nconnection closed\n");

        try {
            output.close();
            input.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    //send message to a chat

    private void sendMessage(String message) {
        try {
            if(message.equals(en)){JOptionPane.showMessageDialog(this, "Client Ternimated the Connection");closeConnection();}
            
            System.out.println(message + " "+ en);
                 
           
            output.writeUTF("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (IOException e) {
            chatWindow.append("\nerror \n");
        }

    }

    //updates window

    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );

    }

   

}

class CustomTextArea extends JTextArea {

    private BufferedImage image;

    public CustomTextArea() {
        super(20, 20);
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
