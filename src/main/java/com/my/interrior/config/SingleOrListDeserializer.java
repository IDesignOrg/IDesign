package com.my.interrior.config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.my.interrior.three.SaveProjectRequest;

//author: 한민욱 0920 1441
//대시보드에서 room일 경우 point에서 배열을 보내고 다른 chair 같은 것들은 객체로 받아와짐.
//단일 객체랑 배열일 때 모두 처리하기 위해 만듦.
//SaveProjectRequest.java에서 @JsonDeserialize(using = SingleOrListDeserializer.class) 사용.
public class SingleOrListDeserializer extends JsonDeserializer<List<SaveProjectRequest.PointRequest>> {

    @Override
    public List<SaveProjectRequest.PointRequest> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        List<SaveProjectRequest.PointRequest> list = new ArrayList<>();

        // JSON이 배열이면
        if (node.isArray()) {
            for (JsonNode element : node) {
                list.add(p.getCodec().treeToValue(element, SaveProjectRequest.PointRequest.class));
            }
        } else {
            // JSON이 단일 객체이면
            list.add(p.getCodec().treeToValue(node, SaveProjectRequest.PointRequest.class));
        }

        return list;
    }
}
