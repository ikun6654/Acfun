package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class sentget {
    String url;
    public sentget(String url) {
        this.url=url;
    }
    //GET请求
    public String  GET(){
        try {
            // 创建URL对象
            URL obj = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // 读取响应内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 打印响应内容
            //System.out.println("Response Content: " + response.toString());
            return response.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
