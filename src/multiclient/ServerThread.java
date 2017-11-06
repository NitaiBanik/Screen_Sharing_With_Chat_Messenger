/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiclient;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Ehsan
 */
public class ServerThread extends Thread {

    Socket socket;
    Home tst;

    int in;
    String directory = ".\\rec2\\";

    ServerThread(Socket s, String dir, Home h,int counter) {
        socket = s;
        directory = dir;
        tst = h;
        in = counter;
    }

    public void run() {
        try {

            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);
            BufferedImage screencapture;

            File file;

            File[] files = new File(directory).listFiles();
            int counter = 0;
            if (tst.messengerMode[in]) {
                counter = 1;
            } else {
                counter = 5000;
            }
            dos.writeInt(counter);
            System.out.println("counter " + counter);
            for (int i = 0; i < counter; i++) {

                try {
                    Thread.sleep(300);

                } catch (Exception e) {
                }
                screencapture = new Robot().createScreenCapture(
                        new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                if (tst.screenShareOn[in]) {
                    file = new File(directory + "screencapture" + Integer.toString(i) + ".jpg");
                } else {
                    {
                        file = new File(directory + "THEEND.jpg");
                    }
                    ImageIO.write(screencapture, "jpg", file);
                    long length = file.length();
                    dos.writeLong(length);

                    String name = file.getName();
                    dos.writeUTF(name);

                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    int theByte = 0;

                    while ((theByte = bis.read()) != -1) {
                        bos.write(theByte);
                    }
                    bos.flush();
                    dos.flush();
                    bis.close();
                    System.out.println("messenger");
                    //new ServerMessenger(socket);

                    break;
                }

                ImageIO.write(screencapture, "jpg", file);
                long length = file.length();
                dos.writeLong(length);

                String name = file.getName();
                //Charset charset = Charset.forName("ISO-8859-1");
                //dos.write(name.getBytes(charset));
                dos.writeUTF(name);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                int theByte = 0;
                while ((theByte = bis.read()) != -1) {
                    bos.write(theByte);
                }

                bis.close();
            }
//            if(!tst.messengerMode)
//            dos.close();
            if(counter==1)
            new ServerGui(socket, dos);
        } catch (IOException ex) {
            {
            JOptionPane.showMessageDialog(tst, "Client Ternimated the Connection");
           
        }
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
