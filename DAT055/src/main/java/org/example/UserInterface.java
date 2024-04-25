import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ChatApplication extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton imageButton;
    private JLabel statusLabel; // status label to display feedback
    private JList<String> userList;

    public ChatApplication() {
        super("Chat Application");
        initializeComponents();
        layoutComponents();
        addListeners();
    }

    private void initializeComponents() {
        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set a custom font for the chat area
        chatArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        messageField = new JTextField(25);
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Consistent font style

        sendButton = new JButton("Send");
        imageButton = new JButton("Send Image");
        
        statusLabel = new JLabel("Ready"); // Initial status message
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.BLUE);

        //TODO populate with model info, link to the model component to display and update the list of active users based on the apps current state
        userList = new JList<>(); 
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(chatArea);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(new JLabel("Active Users"), BorderLayout.NORTH);
        sidePanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(imageButton, BorderLayout.EAST);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        sendButton.addActionListener(e -> sendMessage());
        imageButton.addActionListener(e -> sendImage());
        messageField.addActionListener(e -> sendMessage()); // SEND with ENTER
    }

    private void sendMessage() {
        String message = messageField.getText().trim(); // Retrieve the text from the input field
        if (!message.isEmpty()) {
            // code to send the message to the server or other users
            try {
                statusLabel.setText("Sending..."); // Update status
            //TODO Change with information from network info
           // controller.sendMessage(message); 
            chatArea.append("You: " + message + "\n");
            messageField.setText(""); // Clears the input field after sending
            statusLabel.setText("Message sent"); // update status on send
                
            new Timer(3000, e -> statusLabel.setText("Ready")).start();
        } catch (Exception e) {
            statusLabel.setText("Failed to send message"); // Update status on failure to sendd
            JOptionPane.showMessageDialog(this, "Failed to send message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }


    private void sendImage() {
        JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "bmp")); // Filter for image files

    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
         // Code to send the image to the server or other users
        try {
            statusLabel.setText("Sending image..."); // update status
              //TODO Change with information from network info
           // controller.sendImage(selectedFile); 
           statusLabel.setText("Image sent"); // Update status on successful send

           new Timer(3000, e -> statusLabel.setText("Ready")).start();
        } catch (Exception e) {
            statusLabel.setText("Failed to send image"); // Update status on failure to send
            JOptionPane.showMessageDialog(this, "Failed to send image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
       
      
    }
    }
    private static void showConnectionDialog() {
        JTextField serverField = new JTextField(10);
        JTextField portField = new JTextField(5);
        JTextField usernameField = new JTextField(10);
        
        //GRID
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server IP:"));
        panel.add(serverField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        //confirm or cancel
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect to Chat",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        try {
           //TODO Change with information from network info
           // controller.connectToServer(serverField.getText(), Integer.parseInt(portField.getText()), usernameField.getText()); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            return; // stop the opening of the main window if connection fails
        }
    } else {
        System.exit(0); // close application if user cancels
    }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        showConnectionDialog(); // Show connection dialog first
        ChatApplication chatApp = new ChatApplication();
        chatApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatApp.pack();
        chatApp.setVisible(true);
    });
    }
}
