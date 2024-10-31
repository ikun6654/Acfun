/********************************************************
                                1、对视频的标题进行匹配
                                2、匹配window.videoInfo
                                3、匹配ts文件地址
***********************************************************/
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class re_find {
    private final String input;

    public re_find(String input) {
        this.input = input;
    }

    private String findPattern(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            System.out.println("未找到匹配的内容");
            return null;
        }
    }
    private List<String> findAllPatterns(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        List<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        if (matches.isEmpty()) {
            System.out.println("未找到匹配的内容");
        }

        return matches;
    }

    public String findTitle() {
        String regex = ",\"title\":\"([^\"]*)\",";
        return findPattern(regex);
    }

    public String findVideoInfo() {
        String regex = "window\\.videoInfo\\s*=\\s*(.*?);";
        return findPattern(regex);
    }

    public List<String> findTs() {
        String regex = ",(.*?)#EXT";
        return findAllPatterns(regex);
    }
}
