import src.Client;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class setupWindow {
    public static void main(String[] args) {
        // Create a new JFrame (window)
        JFrame frame = new JFrame("Chess Setup");
        
        // Set the layout manager (optional)
        frame.setLayout(new FlowLayout());

        // Set the default close operation to exit the program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window (width, height)
        int frameWidth = 400;
        int frameHeight = 500;
        frame.setSize(frameWidth, frameHeight);

        // Create a JLabel (text label) and add it to the frame
        JLabel label = new JLabel("Hello, this is a simple interface to setup the TUC chess environment.");
        frame.add(label);

        // Create a JButton (button)
        JButton serverButton = new JButton("Start a new game");        
        JButton client1Button = new JButton("Player 1");        
        JButton client2Button = new JButton("Player 2");    

        serverButton.setPreferredSize(new Dimension(200, 100)); // Width: 200, Height: 100
        client1Button.setPreferredSize(new Dimension(200, 100)); // Width: 200, Height: 100
        client2Button.setPreferredSize(new Dimension(200, 100)); // Width: 200, Height: 100

        // Add an action listener to the button
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskStart serverTask = new TaskStart("Server");
                Thread serverThread = new Thread(serverTask);
                serverThread.start();
                serverButton.setEnabled(false);
            }
        });
        client1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskStart player1Task = new TaskStart("Player 1");
                Thread player1Thread = new Thread(player1Task);
                player1Thread.start();
                client1Button.setEnabled(false);
            }
        });
        client2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskStart player2Task = new TaskStart("Player 2");
                Thread player2Thread = new Thread(player2Task);
                player2Thread.start();
                client2Button.setEnabled(false);
            }
        });

        // Add the button to the frame
        frame.add(serverButton);
        frame.add(client1Button);
        frame.add(client2Button);
        
        frame.setLocationRelativeTo(null);
        //frame.pack();
        // Set the window's visibility to true
        frame.setVisible(true);
    }
}
class TaskStart implements Runnable {
    private String threadName;

    public TaskStart(String name){
        this.threadName = name;
    }

    @Override
    public void run() {
        System.out.println(threadName + " is running" );
        if (threadName == "Server"){
            UDPServer.main(null);
        }
        else {
            Client.main(null);
        }   
    }
}