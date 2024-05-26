package org.example;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Files;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.text.*;
import java.io.*;

/**
 * Grafiskt användargränssnitt för chattapplikationen.
 * Denna klass ärver från JFrame och implementerar Observer för att hantera uppdateringar från ChatHistory.
 * Det hanterar all användarinteraktion genom olika grafiska komponenter som textfält, knappar och dialogrutor.
 */
public class UserInterface extends JFrame implements Observer {
    private JTextPane chatPane;
    private JTextField messageInputField;
    private JButton sendButton, imageButton;
    private JLabel statusLabel;
    private JList<String> userList;
    private IController controller;
    private ChatHistory chatHistory;
    private StyledDocument document;


    /**
     * Konstruerar användargränssnittet och initierar alla nödvändiga komponenter och lyssnare.
     * Registrerar även klassen som en observatör till ChatHistory för att få meddelandeuppdateringar.
     *
     * @param controller En controller som hanterar alla användaråtgärder som att skicka meddelanden och bilder.
     * @param chatHistory En ChatHistory som denna gränssnitt lyssnar på för att få meddelandeuppdateringar.
     */
    public UserInterface(IController controller, ChatHistory chatHistory) {
        super("Chat Application");
        this.controller = controller;
        this.chatHistory = chatHistory;
        chatHistory.addObserver(this);

        initializeComponents();
        layoutComponents();
        addListeners();
        showConnectionDialog();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeComponents() {
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        document = chatPane.getStyledDocument();

        messageInputField = new JTextField("Type Your Message");
        messageInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        sendButton = new JButton("Send");
        imageButton = new JButton("Send Image");

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.BLUE);

        userList = new JList<>();
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(chatPane);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);

        JPanel sidePanel = new JPanel(new BorderLayout());
        //sidePanel.add(new JLabel("Active Users"), BorderLayout.NORTH);      // Detta för aktiv användarlista? om vi ens ska ha det
        //sidePanel.add(new JScrollPane(userList), BorderLayout.CENTER);

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
        messageInputField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (messageInputField.getText().equals("Type Your Message")) {
                    messageInputField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (messageInputField.getText().isEmpty()) {
                    messageInputField.setText("Type Your Message");
                }
            }
        });

        sendButton.addActionListener(e -> {
            if (!messageInputField.getText().equals("Type Your Message")) {
                controller.sendMessage(messageInputField.getText().trim());
                messageInputField.setText("");
            }
        });

        messageInputField.addActionListener(e -> {
            if (!messageInputField.getText().equals("Type Your Message")) {
                controller.sendMessage(messageInputField.getText().trim());
                messageInputField.setText("");
            }
        });

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {

                    // Now, we convert the image to a byte array before sending
                    byte[] imageData = Files.readAllBytes(selectedFile.toPath());
                    controller.sendImage(selectedFile);  // This handles sending the image and adding it to history

                    // Insert "You:" and the image into the chat pane directly
                    document.insertString(document.getLength(), "You:\n", null);
                    insertImageToChat(imageData);  // Display the image after "You:"
                    statusLabel.setText("Image sent");
                    new javax.swing.Timer(3000, event -> statusLabel.setText("Ready")).start();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(this, "Failed to load image: " + ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to update chat pane: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertImageToChat(byte[] imageData) {
        ImageIcon icon = new ImageIcon(imageData);
        // Scale the image. Adjust the width to 300 and height to -1 to maintain aspect ratio.
        Image img = icon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        try {
            document.insertString(document.getLength(), "\n", null);
            Style style = document.addStyle("ImageStyle", null);
            StyleConstants.setIcon(style, icon);
            document.insertString(document.getLength(), " ", style); // Insert the image
            document.insertString(document.getLength(), "\n", null); // Move to the next line after the image
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void showConnectionDialog() {
        JTextField serverField = new JTextField(10);
        JTextField usernameField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server IP:"));
        panel.add(serverField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Connect to Chat",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.connectToServer(serverField.getText(), usernameField.getText().trim());
        } else {
            System.exit(0);  // Close application if user cancels
        }
    }


    /**
     * Uppdaterar användargränssnittet baserat på notifikationer från observabla objekt (ChatHistory).
     *
     * @param o Det observabla objektet (ChatHistory).
     * @param arg Det meddelande som har skickats eller tagits emot.
     */
    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (arg instanceof Message) {
                    Message message = (Message) arg;

                    // Update to check who sent the message
                    if (message instanceof TextMessage) {
                        document.insertString(document.getLength(), message.toString() + "\n", null);
                    } else if (message instanceof MMS) {
                        byte[] imageData = ((MMS) message).getImageData();
                        insertImageToChat(imageData);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}