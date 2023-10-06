import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private JFrame serverFrame;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private ServerSocket serverSocket;
    private Socket socket;
    private InetAddress inetAddress;
    private DataInputStream dis;
    private DataOutputStream dos;

    Thread thread = new Thread() {
        public void run() {
            while (true) {
                readMessage();
            }
        }
    };

    Server() {
        serverFrame = new JFrame("SERVER");
        serverFrame.setSize(500, 500);
        serverFrame.setLocationRelativeTo(null);    // Center the frame on the screen

        textArea = new JTextArea();
        textArea.setFont(new Font("", Font.PLAIN, 15));
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        serverFrame.add(scrollPane);

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText());
                textArea.append(e.getActionCommand() + "\n");
                textField.setText("");
                /*textArea.append("Server: " + textField.getText() + "\n");
                textField.setText("");*/
            }
        });
        textField.setEditable(false);
        serverFrame.add(textField, BorderLayout.SOUTH);

        serverFrame.setDefaultCloseOperation(serverFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
    }

    public void waitingForClient() {
        try {
            serverSocket = new ServerSocket(1234);
            textArea.setText("For Connecting Provide This IP ADDRESS: " + getIpAddress());
            socket = serverSocket.accept();
            textArea.setText("Connected\n");
            textArea.append("-----------------------------------------------------------------\n");
            textField.setEditable(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getIpAddress() {
        String ip_address = "";
        try {
            inetAddress = InetAddress.getLocalHost();
            ip_address = inetAddress.getHostAddress();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ip_address;
    }

    public void setIOStreams() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        thread.start();
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readMessage() {
        try {
            String message = dis.readUTF();
            showMessage(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showMessage(String message) {
        textArea.append("Client: " + message + "\n");
        chatSound();
    }

    public void chatSound() {
        try {
            File file = new File("D:\\IntelliJ IDEA\\Chatting Application\\src\\chat_sound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
