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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private JFrame serverFrame;
    private JLabel heading;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private ServerSocket serverSocket;
    private Socket socket;
    private InetAddress inetAddress;
    private DataInputStream dis;
    private DataOutputStream dos;

    // Thread for reading client messages
    Thread thread = new Thread() {
        public void run() {
            while (true) {
                readMessage();
            }
        }
    };

    public Server() {
        initializeGUI(); // Initialize the GUI
    }

    // Initialize the graphical user interface
    private void initializeGUI() {
        serverFrame = new JFrame("MESSENGER");
        serverFrame.setSize(500, 500);
        ImageIcon image=new ImageIcon("D:\\IntelliJ IDEA\\Chatting Application\\src\\icons_chat.png");
        serverFrame.setIconImage(image.getImage());
        serverFrame.setLocationRelativeTo(null); // Center the frame on the screen

        heading = new JLabel("Server");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("",Font.PLAIN,20));
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        serverFrame.add(heading, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setFont(new Font("", Font.PLAIN, 15));
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        serverFrame.add(scrollPane);

        textField = new JTextField();
        textField.setFont(new Font("",Font.PLAIN,15));
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText()); // Send message when Enter is pressed
                textArea.append(e.getActionCommand() + "\n");
                textField.setText("");
            }
        });
        textField.setEditable(false);
        serverFrame.add(textField, BorderLayout.SOUTH);

        serverFrame.setDefaultCloseOperation(serverFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
    }

    // Start the server
    public void startServer() {
        try {
            serverSocket = new ServerSocket(1234);
            textArea.setText("For Connecting Provide This IP ADDRESS: " + getIpAddress());
            socket = serverSocket.accept();
            textArea.setText("Server Connected With Client\n");
            textArea.append("-----------------------------------------------------------------\n");
            Thread.sleep(1000);
            textArea.setText("");
            textField.setEditable(true);
            setIOStreams();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Get the server's IP address
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

    // Set up input and output streams for communication
    public void setIOStreams() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        thread.start(); // Start the thread for reading messages
    }

    // Send a message to the client
    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Read messages from the client
    public void readMessage() {
        try {
            String message = dis.readUTF();
            showMessage(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Display a received message in the text area and play a chat sound
    public void showMessage(String message) {
        textArea.append("Client: " + message + "\n");
        chatSound();
    }

    // Play a chat sound when a message is received
    public void chatSound() {
        try {
            // Change the file path to the location of your chat sound file
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
