/***********
 一、数据来源
 网址：https://www.acfun.cn/v/ac43323102
 二、代码实现
 发送请求->获取数据->解析数据->保存数据
 第一次请求 ->获取m3u8连接/视频标题
 1、模拟浏览器对url发送请求
 2、获取数据
 3、解析数据
 ************/
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    String url;
    String downloadDirectory;
    String title;
    int nThread;
    private final ExecutorService executorService;
    public  boolean finish=false;
    public Main(String url, String downloadDirectory,String title,int nThread) {
        this.url = url;
        this.downloadDirectory = downloadDirectory;
        this.executorService = Executors.newFixedThreadPool(10);
        this.title=title;
        this.nThread= nThread;
    }

    public void download() {
        System.out.println("请输入视频网址");

        sentget sent = new sentget(url);
        String response = sent.GET();

        // 对源码分析的到info和title
        re_find in = new re_find(response);
        //String title = in.findTitle();
        String info = in.findVideoInfo();


        // 将得到的info进行分析都得到m3u8文件地址
        json js = new json(info);
        String m3u8_url = js.analyze();
        // System.out.println(m3u8_url);

        // 将得到m3u8地址再次发送，拿到m3u8文件内容
        sentget data = new sentget(m3u8_url);
        String m3u8_data = data.GET();
        // System.out.println(m3u8_data);

        // 通过m3u8文件内容拿到ts地址
        re_find one = new re_find(m3u8_data);
        List<String> tsList = one.findTs();
        String baseUrl = "https://ali-safety-video.acfun.cn/mediacloud/acfun/acfun_video/";

        List<String> fullUrls = new ArrayList<>();
        List<String> fullPaths = new ArrayList<>();

        for (int i = 0; i < tsList.size(); i++) {
            String ts = tsList.get(i);
            String fullUrl = baseUrl + ts;

            String fileName = String.format("%03d.ts", i);
            String fullPath = downloadDirectory + "\\" + fileName;

            fullUrls.add(fullUrl);
            fullPaths.add(fullPath);
        }

        writeToFile written = new writeToFile();
        written.downloadFiles(fullUrls, fullPaths, 2);

        // 将得到的ts文件合并
        new TsMergeFiles(downloadDirectory, title).mergeTs();

        // 将合并后的ts文件删除
        new DeleteTsFiles(downloadDirectory).deleteTsFiles();


    }

    public void execute() {
        executorService.execute(this::download);
        finish=true;
    }

    public void pause() {
        executorService.shutdownNow(); // 停止执行器服务
    }

}

