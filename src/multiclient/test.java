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
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ehsan
 */
public class test implements Runnable {
  Socket socket;
    public static void main(String args[]) {

        test t = new test();

    }

    public void run() {
        try {
            bar();
        } catch (AWTException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    test() {
        Thread t = new Thread(this);

        //frame = new JFrame();
        //frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        t.start();
    }
    //JFrame frame;

    public void bar() throws AWTException, IOException {
       

            ServerSocket serverSocket = new ServerSocket(6066);
           socket = serverSocket.accept();
          // new ServerThread(socket, ".\\rec2\\").start();
           socket = serverSocket.accept();
      //     new ServerThread(socket, ".\\rec4\\").start();
           
           
            

    }

    public static ImageIcon resizeImageIcon(ImageIcon imageIcon, Integer width, Integer height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
        graphics2D.dispose();

        return new ImageIcon(bufferedImage, imageIcon.getDescription());
    }

}
