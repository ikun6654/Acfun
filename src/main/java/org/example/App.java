package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final List<DownloadTaskPanel> downloadPanels = new ArrayList<>();
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        frame = new JFrame("Video Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
        JLabel urlLabel = new JLabel("视频网址:");
        JTextField urlInput = new JTextField(20);
        urlInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        urlInput.setPreferredSize(new Dimension(200, 20)); // 设置文本框宽度
        urlPanel.add(urlLabel);
        urlPanel.add(urlInput);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel("视频标题：");
        JTextField titleInput = new JTextField(10);
        titleInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleInput.setPreferredSize(new Dimension(200, 20)); // 设置文本框宽度
        titlePanel.add(titleLabel);
        titlePanel.add(titleInput);

        JPanel directoryPanel = new JPanel();
        directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
        JLabel directoryLabel = new JLabel("保存地址:");
        JTextField directoryInput = new JTextField(20);
        directoryInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        directoryInput.setPreferredSize(new Dimension(200, 20)); // 设置文本框宽度
        JButton chooseDirectoryButton = new JButton("...");
        chooseDirectoryButton.addActionListener(e -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = directoryChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = directoryChooser.getSelectedFile();
                directoryInput.setText(selectedDirectory.getAbsolutePath());
            }
        });
        directoryPanel.add(directoryLabel);
        directoryPanel.add(directoryInput);
        directoryPanel.add(chooseDirectoryButton);

        JPanel threadPanel = new JPanel();
        threadPanel.setLayout(new BoxLayout(threadPanel, BoxLayout.X_AXIS));
        JLabel threadLabel = new JLabel("下载线程数：");
        JTextField threadInput = new JTextField(20);
        threadInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        threadInput.setPreferredSize(new Dimension(200, 20)); // 设置文本框宽度
        threadPanel.add(threadLabel);
        threadPanel.add(threadInput);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        JButton controlButton = new JButton("下载");
        controlButton.addActionListener(e -> {
            String videoUrl = urlInput.getText();
            String downloadDirectory = directoryInput.getText();
            String title = titleInput.getText();

            try {
                int nThread = Integer.parseInt(threadInput.getText());
                if (!videoUrl.isEmpty() && !downloadDirectory.isEmpty()) {
                    DownloadTaskPanel downloadTaskPanel = new DownloadTaskPanel(videoUrl, downloadDirectory, title, nThread);
                    downloadPanels.add(downloadTaskPanel);
                    mainPanel.add(downloadTaskPanel);
                    executeDownloadTask(downloadTaskPanel);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "视频网址和保存地址不能为空");
                }
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(frame, "无效的线程输入。请输入有效的整数。");
            }
        });
        controlPanel.add(controlButton);

        mainPanel.add(urlPanel);
        mainPanel.add(titlePanel);
        mainPanel.add(directoryPanel);
        mainPanel.add(threadPanel);
        mainPanel.add(controlPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(scrollPane);
        frame.setSize(450, 300);
        frame.setVisible(true);
    }

    private static void executeDownloadTask(DownloadTaskPanel taskPanel) {
        Thread thread = new Thread(taskPanel.getDownloadTask());
        thread.start();
    }

    private static class DownloadTaskPanel extends JPanel {
        private final String videoUrl;
        private final String title;
        private final String downloadDirectory;
        private int nThread;

        public DownloadTaskPanel(String videoUrl, String downloadDirectory, String title, int nThread) {
            this.videoUrl = videoUrl;
            this.downloadDirectory = downloadDirectory;
            this.title = title;
            this.nThread = nThread;

            initUI();
        }

        private void initUI() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            add(new JLabel("任务下载中：" + title));
        }

        public Runnable getDownloadTask() {
            return new DownloadTask(videoUrl, downloadDirectory, title, nThread);
        }

        private static class DownloadTask implements Runnable {
            private String videoUrl;
            private String title;
            private String downloadDirectory;
            private int nThread;

            public DownloadTask(String videoUrl, String downloadDirectory, String title, int nThread) {
                this.videoUrl = videoUrl;
                this.downloadDirectory = downloadDirectory;
                this.title = title;
                this.nThread = nThread;
            }

            @Override
            public void run() {
                Main download = new Main(videoUrl, downloadDirectory, title, nThread);
                download.download();
            }
        }
    }
}
