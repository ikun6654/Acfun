package org.example;

import java.io.File;

public class DeleteTsFiles {
    String path;
    public DeleteTsFiles(String path){
        this.path=path;
    }

    public  void deleteTsFiles() {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("指定路径不存在");
            return;
        }

        if (!file.isDirectory()) {
            System.out.println("指定路径不是一个目录");
            return;
        }

        deleteTsFilesRecursively(file);
    }

    private static void deleteTsFilesRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteTsFilesRecursively(file);
                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".ts")) {
                    if (file.delete()) {
                        //System.out.println("已删除文件: " + file.getAbsolutePath());
                    } else {
                        //System.out.println("无法删除文件: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
