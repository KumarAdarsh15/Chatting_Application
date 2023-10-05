import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class Client {
    private JFrame clientFrame;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private Socket socket;
    String ip_address;

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
                textArea.setFont(new Font("MV Boli", Font.PLAIN, 20));
                textArea.setEditable(false);
                scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                clientFrame.add(scrollPane);

                textField = new JTextField();
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
}
