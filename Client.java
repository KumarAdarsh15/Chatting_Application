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
import java.net.Socket;

public class Client {
    private JFrame clientFrame;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private Socket socket;
    String ip_address;
    private DataInputStream dis;
    private DataOutputStream dos;
    Thread thread = new Thread() {
        public void run() {
            while (true) {
                readMessage();
            }
        }
    };

    Client() {
        ip_address = JOptionPane.showInputDialog("IP ADDRESS");

        if (ip_address != null) {
            if (ip_address.equals("")) {
                JOptionPane.showMessageDialog(clientFrame, "Enter IP ADDRESS");
            } else {
                connectToServer();
                clientFrame = new JFrame("CLIENT");
                clientFrame.setSize(500, 500);
                clientFrame.setLocationRelativeTo(null);    // Center the frame on the screen

                textArea = new JTextArea();
                textArea.setFont(new Font("", Font.PLAIN, 15));
                textArea.setEditable(false);
                scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                clientFrame.add(scrollPane);

                textField = new JTextField();
                textField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(textField.getText());
                        textArea.append(e.getActionCommand() + "\n");
                        textField.setText("");
                    }
                });
                clientFrame.add(textField, BorderLayout.SOUTH);

                clientFrame.setDefaultCloseOperation(clientFrame.EXIT_ON_CLOSE);
                clientFrame.setVisible(true);
            }
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket(ip_address, 1234);
        } catch (Exception e) {
            System.out.println(e);
        }
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
        textArea.append("Server: " + message + "\n");
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
