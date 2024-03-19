package com.sparta.trello.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtMessage {

    public void messageToClient(HttpServletResponse response, int httpstatus, String message,
        String messageType)
        throws IOException {
        response.setStatus(httpstatus);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 메시지를 JSON 형식으로 클라이언트에게 반환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", httpstatus);
        responseMap.put(messageType, message);

        String jsonResponse = objectMapper.writeValueAsString(responseMap);

        response.getWriter().write(jsonResponse);
    }
}
