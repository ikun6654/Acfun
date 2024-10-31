package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

public class json {
    String info;
    public json(String info){
        this.info=info;
    }
    public  String analyze() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(info);

            // 获取 currentVideoInfo 节点
            JsonNode currentVideoInfo = root.path("currentVideoInfo");

            // 获取 ksPlayJson 节点
            String ksPlay = currentVideoInfo.path("ksPlayJson").asText();
            JsonNode ksPlayJsonNode= objectMapper.readTree(ksPlay);

            // 从 ksPlayJson 获取 adaptationSet 数组
            JsonNode adaptationSet = ksPlayJsonNode.path("adaptationSet");

            // 检查 adaptationSet 不为 null 且有元素
            if (adaptationSet != null && adaptationSet.isArray() && adaptationSet.size() > 0) {
                // 获取 adaptationSet 数组的第一个元素
                JsonNode firstAdaptation = adaptationSet.get(0);

                // 从第一个 adaptation 获取 representation 数组
                JsonNode representation = firstAdaptation.path("representation");

                // 检查 representation 不为 null 且有元素
                if (representation != null && representation.isArray() && representation.size() > 0) {
                    // 获取 representation 数组的第一个元素
                    JsonNode firstRepresentation = representation.get(0);

                    // 从第一个 representation 获取 url 字段

                    // 输出最终的 URL
                    //System.out.println("最终的 URL：" + url);
                    return firstRepresentation.path("url").asText();
                } else {
                    System.out.println("Representation 数组为 null 或为空");
                }

            } else {
                System.out.println("AdaptationSet 数组为 null 或为空");
            }

        } catch (JsonMappingException e) {
            e.printStackTrace(); // Handle the exception as needed
        } catch (Exception e) {
            e.printStackTrace(); // Handle other exceptions
        }
        return null;
    }
}
