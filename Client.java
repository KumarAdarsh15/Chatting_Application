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
    private JLabel heading;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private Socket socket;
    String ip_address;
    private DataInputStream dis;
    private DataOutputStream dos;

    // Thread for reading server messages continuously
    Thread thread = new Thread(){
        public void run(){
            while (true){
                readMessage();
            }
        }
    };

    public Client() {
        initializeGUI();  // Initialize the graphical user interface
    }

    // Method to initialize the graphical user interface
    private void initializeGUI(){
        ip_address = JOptionPane.showInputDialog("IP ADDRESS");  // Prompt for server IP address

        if (ip_address != null) {
            if (ip_address.equals("")) {
                JOptionPane.showMessageDialog(clientFrame, "Enter IP ADDRESS");  // Show an error message if no IP address is provided
            } else {
                connectToServer();  // Establish a connection to the server
                clientFrame = new JFrame("MESSENGER");
                clientFrame.setSize(500, 500);
                ImageIcon image=new ImageIcon("D:\\IntelliJ IDEA\\Chatting Application\\src\\icons_chat.png");
                clientFrame.setIconImage(image.getImage());
                clientFrame.setLocationRelativeTo(null);

                heading = new JLabel("Client");
                heading.setHorizontalAlignment(SwingConstants.CENTER);
                heading.setFont(new Font("",Font.PLAIN,20));
                heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                clientFrame.add(heading, BorderLayout.NORTH);

                textArea = new JTextArea();
                textArea.setFont(new Font("", Font.PLAIN, 15));
                textArea.setEditable(false);
                scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                clientFrame.add(scrollPane);

                textField = new JTextField();
                textField.setFont(new Font("",Font.PLAIN,15));
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

    // Method to establish a connection to the server
    public void connectToServer() {
        try {
            socket = new Socket(ip_address, 1234);
        } catch (Exception e) {
            showError("Error in getting IP address: " + e.getMessage());
        }
    }

    // Method to set up input and output streams for communication
    public void setIOStreams() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            showError("Error in setting up IO streams: " + e.getMessage());
        }
        thread.start();  // Start the message reading thread
    }

    // Method to send a message to the server
    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (Exception e) {
            showError("Error in sending message: " + e.getMessage());
        }
    }

    // Method to read and display a message from the server
    public void readMessage() {
        try {
            String message = dis.readUTF();
            showMessage(message);
        } catch (Exception e) {
            showError("Error in reading message: " + e.getMessage());
        }
    }

    // Method to display a message in the text area
    public void showMessage(String message) {
        textArea.append("Server: " + message + "\n");
        chatSound();
    }

    // Method to play a chat sound
    public void chatSound() {
        try {
            File file = new File("D:\\IntelliJ IDEA\\Chatting Application\\src\\chat_sound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            showError("Error playing chat sound: " + e.getMessage());
        }
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(clientFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
