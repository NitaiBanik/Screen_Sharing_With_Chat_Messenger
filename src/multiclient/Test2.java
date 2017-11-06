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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ehsan
 */
public class Test2 implements Runnable {

    int portNo;
    String dirPath;
    DataInputStream dis;

    /**
     * @param args the command line arguments
     */
    Test2(int pr, String pt) {
        portNo = pr;
        dirPath = pt;
        Thread t = new Thread(this);
        t.run();

    }

    public static void main(String[] args) throws AWTException {
        // TODO code application logic 
        new Test2(6066, ".\\rec");

        //  new Test2(6065,".\\rec3");
    }

    public void foo() throws IOException, AWTException, SocketException {

        String hostDomain = "localhost";
        int port = portNo;
        System.out.println(port);

        Socket socket = new Socket(InetAddress.getByName(hostDomain), port);

        try {
            
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            dis = new DataInputStream(bis);

            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
            AnimatedGifEncoder anim = new AnimatedGifEncoder();
            anim.start("out.gif");
            anim.setDelay(100);
            for (int i = 0; i < filesCount; i++) {
                System.out.println("shuru");
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                files[i] = new File(dirPath + "/" + fileName);
                System.out.println(fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (int j = 0; j < fileLength; j++) {
                    bos.write(bis.read());
                }

                bos.close();

            }
            if (filesCount == 1) {
                new Client(socket, dis);
            }
            //dis.close();

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    @Override
    public void run() {
        try {
            foo();
            //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
