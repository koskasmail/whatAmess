package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class FolderMonitor extends JFrame {

    private JTextField folderPathField;
    private JLabel statusLabel;
    private Timer timer;

    public FolderMonitor() {
        setTitle("Network Folder Monitor");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        folderPathField = new JTextField(30);
        statusLabel = new JLabel("Enter the network folder path and click 'Start Monitoring'");

        JButton startButton = new JButton("Start Monitoring");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startMonitoring();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(folderPathField, BorderLayout.NORTH);
        panel.add(startButton, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        add(panel);
    }

    private void startMonitoring() {
        if (timer != null) {
            timer.cancel();
        }

        String folderPath = folderPathField.getText();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            statusLabel.setText("Invalid folder path. Please enter a valid network folder path.");
            return;
        }

        statusLabel.setText("Monitoring folder: " + folderPath);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkFolder(folder);
            }
        }, 0, 5 * 60 * 1000);
    }

    private void checkFolder(File folder) {
        File[] files = folder.listFiles();
        int fileCount = (files != null) ? files.length : 0;
        SwingUtilities.invokeLater(() -> statusLabel.setText("Number of files in folder: " + fileCount));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FolderMonitor().setVisible(true);
            }
        });
    }
}
