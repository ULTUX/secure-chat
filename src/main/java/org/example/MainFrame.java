package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.SneakyThrows;
import org.example.json.request.*;
import org.example.json.response.ConversationsResponse;
import org.example.json.response.GetMessages;
import org.example.json.response.Response;
import org.example.model.Conversation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MainFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTextArea wTextArea;
    private JList list;
    private JButton newConversationButton;
    private JTextField textField1;
    private SocketManager socketManager;

    private Map<String, String> messagesMap = new HashMap<>();
    private int selectedI = 0;

    public MainFrame(String email, String password) throws Exception {
        super("~DR. Stalowy");
        this.socketManager = new SocketManager();
        boolean connected = false;
        while (!connected) {
            try {
                socketManager.connectToServer();
                connected = true;
            } catch (Exception ignored) {

            }
        }

        loginUser(email, password);

        wTextArea.setEditable(false);

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        newConversationButton.addActionListener(this);
        configureJList();
        loadConversations();

        pack();
        setSize(new Dimension(500, 400));
        setVisible(true);

        textField1.addKeyListener(new KeyAdapter() {
            @SneakyThrows
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    var message = textField1.getText();
                    textField1.setText("");
                    var messaageReq = new SendMessageRequest(Requests.SEND_MESSAGE.toString(), new SendMessageRequest.SendMessageRequestBody(
                            list.getSelectedValue().toString(), message
                    ));
                    var response = socketManager.sendRequest(messaageReq, Response.class);
                    loadMessages(list.getSelectedValue().toString());
                }
            }
        });
        messageLoaderThread();
    }

    private void loadConversations() throws Exception {
        var request = new Request();
        request.setAction(Requests.GET_CONVERSATION_LIST.toString());

        var conversations = socketManager.sendRequest(request, ConversationsResponse.class);
        var newMap = (conversations.getBody().conversations()
                .stream().collect(Collectors.toMap(Conversation::getName, s -> "")));
        var diff = Sets.difference(newMap.keySet(), messagesMap.keySet());
        if (!diff.isEmpty() || messagesMap.keySet().isEmpty()) {
            messagesMap.putAll(newMap);
            refreshList();
        }

    }

    @SneakyThrows
    private void configureJList() {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> {
            try {
                selectedI = list.getSelectedIndex();
                loadMessages((String) list.getSelectedValue());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
    }

    private void messageLoaderThread() {
        new Thread(() -> {
            while (true) {
                try {
                    loadConversations();
                    loadMessages(list.getSelectedValue().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    list.setSelectedIndex(0);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void loadMessages(String conv) throws Exception {
        wTextArea.setText(getMessagesForConversation(conv));
    }


    private String getMessagesForConversation(String conversation) throws Exception {
        var messagesRequest = new ConversationMessagesRequest(Requests.GET_MESSAGES_FOR_CONVERSATION.toString(), new ConversationMessagesRequest.ConversationMessagesRequestBody(conversation));
        var response = socketManager.sendRequest(messagesRequest, GetMessages.class);
        var string = new StringBuilder();
        response.getBody().messages().forEach(m -> string.append("[").append(OffsetDateTime.parse((m.getDateTime())).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))).append("] ").append(m.getAuthor()).append(": ").append(m.getContent()).append("\n"));
        return string.toString();
    }

    private void loginUser(String email, String password) throws Exception {
        var request = new RegisterRequest();
        request.setBody(new RegisterRequest.RegisterRequestBody(email, password));
        System.out.println(socketManager.sendRequest(request, Response.class));

        var login = new LoginRequest();
        login.setBody(new LoginRequest.LoginRequestBody(email, password));
        System.out.println(socketManager.sendRequest(login, Response.class));
    }

    @SneakyThrows
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
//        String email = JOptionPane.showInputDialog(null, "Podaj email", "Logowanie", JOptionPane.QUESTION_MESSAGE);
//        String password = JOptionPane.showInputDialog(null, "Podaj haslo", "Logowanie", JOptionPane.QUESTION_MESSAGE);
        new MainFrame("a", "a");
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(newConversationButton)) {
            var request = new CreateConversationRequest();
            String name = JOptionPane.showInputDialog(null, "Podaj nazwe konwersacji", "Konwersacja", JOptionPane.QUESTION_MESSAGE);
            var users = Lists
                    .newArrayList(JOptionPane.showInputDialog(null, "Podaj liste uzytkownikow", "Konwersacja", JOptionPane.QUESTION_MESSAGE).split(", "))
                    .stream().toList();
            request.setBody(new CreateConversationRequest.CreateConversationRequestBody(name, users));
            var conversationResp = socketManager.sendRequest(request, Response.class);
            if (conversationResp.getStatus() == 200) {
                messagesMap.put(name, "");
            } else
                printErrorMessage("Could not create conversation: " + conversationResp.getResponse());

        }
    }

    private void refreshList() {
        list.setListData(messagesMap.keySet().toArray(new String[0]));
        list.setSelectedIndex(0);
    }

    private void printErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 2, new Insets(2, 2, 2, 2), -1, -1));
        list = new JList();
        mainPanel.add(list, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        newConversationButton = new JButton();
        newConversationButton.setText("New conversation");
        mainPanel.add(newConversationButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        mainPanel.add(textField1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        wTextArea = new JTextArea();
        wTextArea.setEditable(true);
        wTextArea.setEnabled(true);
        wTextArea.setText("");
        scrollPane1.setViewportView(wTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}