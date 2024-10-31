package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class writeToFile {

    public  void downloadFiles(List<String> urls, List<String> saveFiles, int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        int totalTs = urls.size(); // 总的ts文件数
        // 提交下载任务
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {

            futures.add(executorService.submit(new DownloadTask(urls.get(i), saveFiles.get(i))));
        }

         //等待所有任务完成
        try {
            for (int i = 0; i < futures.size(); i++) {
                boolean result = futures.get(i).get();

                int progress = ((i +1)*100)/ totalTs;
                if (progress == 100) {
                    // 在这里触发弹出窗口的逻辑
                    SwingUtilities.invokeLater(() -> {
                        // 创建并显示弹出窗口的代码
                        Component frame = null;
                        JOptionPane.showMessageDialog(frame, "任务已完成！");
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    static class DownloadTask implements Callable<Boolean> {
        private String httpUrl;
        private String saveFile;

        public DownloadTask(String httpUrl, String saveFile) {
            this.httpUrl = httpUrl;
            this.saveFile = saveFile;
        }
        public Boolean call() {
            return httpDownload(httpUrl, saveFile);
        }
    }

    public static boolean httpDownload(String httpUrl, String saveFile) {
        int byteRead;
        URL url;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        }

        try {
            // 获取链接
            URLConnection conn = url.openConnection();
            // 输入流
            InputStream inStream = conn.getInputStream();
            // 写入文件
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[4096];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}