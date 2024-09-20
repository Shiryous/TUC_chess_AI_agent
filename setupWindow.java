import src.Client;  // Assuming this import is correct in your environment
import javax.swing.*;
import java.awt.*;

public class setupWindow {

    public static void main(String[] args) {
        int fontSize = 18;
        // Create and configure the main frame (window)
        JFrame frame = new JFrame("Chess Setup");
        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, fontSize));
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, (int)(fontSize*1.5)));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, fontSize));

        String[] agentOptions = {"Random", "Min-Max", "MCTS"};
        // Add components to the frame
        frame.add(createWelcomeLabel(frame));
        frame.add(createPlayerInput("White Player:", "Jane_Doe", agentOptions));
        frame.add(createPlayerInput("Black Player:", "John_Doe", agentOptions));
        frame.add(createButtons());

        // Display the window
        frame.setLocationRelativeTo(null);  // Center the window
        frame.pack();
        frame.setVisible(true);
    }

    // Create the welcome label
    private static JPanel createWelcomeLabel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel("Hello, this is a simple interface to setup the TUC chess environment."));
        panel.add(new JLabel("You can configure the agent each player wants to play"));
        return panel;
    }

    // Create input fields for player names
    private static JPanel createPlayerInput(String label, String defaultText, String[] agentOptions) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        JLabel playerLabel = new JLabel(label);
        JTextField playerField = new JTextField(defaultText);
        playerField.setMaximumSize(new Dimension(300, 10));
        panel.add(playerLabel);
        panel.add(playerField);
        panel.add(createDropdownMenu(agentOptions));
        return panel;
    }

    // Create the dropdown menu and action handler
    private static JPanel createDropdownMenu(String[] options) {
        JPanel panel = new JPanel();
        JComboBox<String> dropdown = new JComboBox<>(options);
        JLabel selectedLabel = new JLabel("Select an option from the dropdown");

        dropdown.addActionListener(e -> {
            String selectedOption = (String) dropdown.getSelectedItem();
            selectedLabel.setText("You selected: " + selectedOption);
        });

        panel.add(dropdown);
        panel.add(selectedLabel);
        return panel;
    }

    // Create buttons and add action listeners
    private static JPanel createButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 10, 10));  // 3 rows, 1 column, 10px padding

        JButton serverButton = new JButton("Start a new game");
        JButton client1Button = new JButton("Join Player 1");
        JButton client2Button = new JButton("Join Player 2");

        client1Button.setEnabled(false);
        client2Button.setEnabled(false);

        serverButton.addActionListener(e -> {
            startServerTask();
            serverButton.setEnabled(false);
            client1Button.setEnabled(true);
            client2Button.setEnabled(true);
        });

        client1Button.addActionListener(e -> {
            startClientTask("Player 1", "Random");
            client1Button.setEnabled(false);
        });

        client2Button.addActionListener(e -> {
            startClientTask("Player 2", "Random");
            client2Button.setEnabled(false);
        });

        panel.add(serverButton);
        panel.add(client1Button);
        panel.add(client2Button);
        return panel;
    }

    // Method to start a new server instance
    private static void startServerTask() {
        TaskStart task = new TaskStart("Server");
        Thread thread = new Thread(task);
        thread.start();
    }

    // Method to start a new thread with the given task
    private static void startClientTask(String playerName, String agentMethod) {
        TaskStart task = new TaskStart(playerName, agentMethod);
        Thread thread = new Thread(task);
        thread.start();
    }
}

// Class to handle the task in a new thread
class TaskStart implements Runnable {
    private String threadName;
    private String agent;

    // This starts the server
    public TaskStart(String name) {
        this.threadName = name;
    }
    // This starts the clients
    public TaskStart(String name, String agentLogic) {
        this.threadName = name;
        this.agent = agentLogic;
    }

    @Override
    public void run() {
        System.out.println(threadName + " is running with method "+ agent);
        if ("Server".equals(threadName)) {
            UDPServer.main(null);
        } else {
            Client.main(new String[]{threadName, agent});
        }
    }
}
