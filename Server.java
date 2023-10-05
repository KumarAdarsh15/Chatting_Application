import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {
    private JFrame serverFrame;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private ServerSocket serverSocket;
    private InetAddress inetAddress;

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
                showMessage(e.getActionCommand());
                //showMessage(textField.getText());
            }
        });
        serverFrame.add(textField, BorderLayout.SOUTH);

        serverFrame.setDefaultCloseOperation(serverFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
    }

    public void waitingForClient() {
        try {
            serverSocket = new ServerSocket(1234);
            textArea.setText("For Connecting Provide This IP ADDRESS: " + getIpAddress());
            serverSocket.accept();
            textArea.setText("Connected\n");
            textArea.append("-----------------------------------------------------------------\n");
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

    public void showMessage(String message) {
        textArea.append("Server: "+message+"\n");
        textField.setText("");
    }
}
