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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ehsan
 */
public class Test3 implements Runnable {

    int portNo;
    String dirPath, hostDomain;
    ClientFrame f;

    /**
     * @param args the command line arguments
     */
    Test3(int pr, String pt) {
        portNo = pr;
        dirPath = pt;
        Thread t = new Thread(this);
        t.start();
        
    }
    
    Test3(String pt, ClientFrame fr) {
        
        hostDomain = pt;
        f = fr;
        Thread t = new Thread(this);
        t.start();
        
    }
    
    public static void main(String[] args) throws AWTException {
        // TODO code application logic 
        //new Test2(6066,".\\rec");
        
        new Test3(6066, ".\\rec3");
        
    }
    
    public void foo() throws IOException, AWTException, SocketException {
        
        hostDomain = "192.168.0.112";
        int port = 6066;
        dirPath = ".\\rec3";
        System.out.println(port);
        Socket socket = new Socket(InetAddress.getByName(hostDomain), port);
        
        try {

            f.imgLabel.setBounds(20, 20, 1300, 800);
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
            
            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
            AnimatedGifEncoder anim = new AnimatedGifEncoder();
            anim.start(f.pathName + "out.gif");
            anim.setDelay(300);
            for (int i = 0; i < filesCount && f.clientTerminate == false; i++) {
                if (i == 0) {
                    f.setSize(new java.awt.Dimension(1600, 700));
                    f.jPanel1.setSize(new java.awt.Dimension(1600, 700));
                    //f.imgLabel.setSize(1300, 700);
                    
                    f.imgLabel.setVisible(true);
                    f.endScreenShare.setVisible(true);                    
                    
                }
                
                System.out.println("shuru test3");
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();
                
                files[i] = new File(dirPath + "/" + fileName);
                System.out.println(i);
                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                
                System.out.println(fileName);
                //ImageIcon image = resizeImageIcon(new ImageIcon(dirPath+ "/" + fileName), 720, 640);

                // f. imgLabel.setIcon(resizeImageIcon( image ,1150 , 640 ));
                //f. imgLabel.setIcon(image);
                System.out.println("After imgLabel");
                for (int j = 0; j < fileLength; j++) {
                    bos.write(bis.read());
                }
                
                bos.close();
                
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                };
//                 System.out.println(i);
                if (filesCount != 1) {
                    ImageIcon image = resizeImageIcon(new ImageIcon(dirPath + "/" + fileName), 1245, 700);
                    Image image1 = image.getImage();
                    BufferedImage buffered = (BufferedImage) image1;
                    anim.addFrame(buffered);
                    // f. imgLabel.setIcon(resizeImageIcon( image ,1150 , 640 ));
                    f.imgLabel.setIcon(image);
                    System.out.println("sesh loop");                    
                }
                
                if (filesCount == 1) {
                   f.setVisible(false);
                    new ClientGui(socket, dis);
                }
                
            }
            
            anim.finish();
            System.out.println("pop up er age ki ashci ?");
            socket.close();
            if(filesCount!=1)
            {
            
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Want this video file ?", "GIF file", dialogButton);
            if (dialogResult == 0) {
                System.out.println("Yes option");
            } else {
                System.out.println("No Option");
                try {
                    
                    File file = new File(f.pathName + "out.gif");
                    
                    if (file.delete()) {
                        System.out.println(file.getName() + " is deleted!");
                    } else {
                        System.out.println(file.getName() + "Delete operation is failed.");
                    }
                    
                } catch (Exception e) {
                    
                    e.printStackTrace();
                    
                }
                
            }
            }

           // dis.close();
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
    }

    public static ImageIcon resizeImageIcon(ImageIcon imageIcon, Integer width, Integer height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
        graphics2D.dispose();
        
        return new ImageIcon(bufferedImage, imageIcon.getDescription());
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
