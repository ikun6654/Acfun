package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TsMergeFiles {

    private String dir;
    private String fileName;

    public TsMergeFiles(String dir, String fileName) {
        this.dir = dir;
        this.fileName = fileName;
    }

    public void mergeTs() {
        try {
            // 获取目录中的所有TS文件
            File[] tsFiles = new File(dir).listFiles((dir, name) -> name.endsWith(".ts"));

            if (tsFiles == null || tsFiles.length == 0) {
                System.out.println("没有找到TS文件");
                return;
            }

            // 创建合并后的文件
            File outputFile = new File(dir + File.separator + fileName + ".mp4");

            // 如果文件存在，则删除它
            if (outputFile.exists()) {
                outputFile.delete();
            } else {
                // 如果文件不存在，则创建一个新文件
                outputFile.createNewFile();
            }

            // 使用 NIO 进行文件合并
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                 FileChannel outChannel = fileOutputStream.getChannel()) {

                // 遍历 TS 文件，依次写入合并后的文件
                for (File tsFile : tsFiles) {
                    try (FileInputStream fileInputStream = new FileInputStream(tsFile);
                         FileChannel inChannel = fileInputStream.getChannel()) {

                        // 将输入通道的数据传输到输出通道
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                    }
                }

                System.out.println("合并完成: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
