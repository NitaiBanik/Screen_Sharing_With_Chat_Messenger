/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiclient;

import java.awt.AWTException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ehsan
 */
public class Server extends Thread {

    Home tst;

    Server(Home obj) {
        tst = obj;

    }

    public void run() {
        try {
            startServer();
        } catch (AWTException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void startServer() throws AWTException, IOException {
        ServerSocket serverSocket = new ServerSocket(6066);
        while (tst.serverOn[0]) {
            System.out.println("haha");

            tst.socket = serverSocket.accept();
            

            ///new ServerThread(tst.socket, ".\\server"+ Integer.toString(tst.clientNumber) +"\\").start();
            if (tst.freePos[0] == false) {
                tst.freePos[0]= true;
                tst.clientlabel1.setVisible(true);
                tst.screenShare1.setVisible(true);
                tst.Message1.setVisible(true);
                 System.out.println("haha2");
                while(!tst.screenShareOn[0] ){
                    System.out.println(tst.screenShareOn[0]);
               }

                           //System.out.println("haha"+tst.screenShareOn);
                new ServerThread(tst.socket, ".\\rec2\\", tst,0).start();
                //new MessengerThreadServer(tst.socket).start();

            }
            else if (tst.freePos[1] == false) {
                tst.freePos[1]=true;
                tst.clientLabel2.setVisible(true);
                tst.screenShare2.setVisible(true);
                tst.Message2.setVisible(true);
                System.out.println("client dhukse");
                  while(!tst.screenShareOn[1] ){
                    System.out.println(tst.screenShareOn[1]);
                }
                  System.out.println("client dhukse");
                
                System.out.println("client2");
                new ServerThread(tst.socket, ".\\rec4\\", tst,1).start();
                

            }
tst.clientNumber++;
tst.clientNumber%=2;

        }

    }

}
