package com.example.tripagent.util;

import com.example.tripagent.common.auth.LoginUser;
import com.example.tripagent.common.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "trip-agent-secret-key";
    private static final long EXPIRE_MILLIS = 24 * 60 * 60 * 1000L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private JwtUtil() {
    }

    public static String generateToken(Long userId,
                                       Long employeeId,
                                       String username,
                                       String role){
       try {
           Map<String, Object> header=new HashMap<>();
           header.put("alg","HS256");
           header.put("typ","JWT");

           Map<String, Object> payload=new HashMap<>();
           payload.put("userId", userId);
           payload.put("employeeId", employeeId);
           payload.put("username", username);
           payload.put("role", role);
           payload.put("expireTime", System.currentTimeMillis() + EXPIRE_MILLIS);

           String headerBase64 = base64UrlEncode(OBJECT_MAPPER.writeValueAsString(header));
           String payloadBase64 = base64UrlEncode(OBJECT_MAPPER.writeValueAsString(payload));

           String content = headerBase64+'.'+payloadBase64;
           String signature = hmacSha256(content, SECRET);

           return content + "." + signature;
       }catch (Exception e){
           throw new RuntimeException("生成Token失败", e);
       }
    }

    private static String hmacSha256(String content, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] bytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private static String base64UrlEncode(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
    public static LoginUser parseToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new BusinessException("Token不能为空");
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new BusinessException("Token格式错误");
            }

            String content = parts[0] + "." + parts[1];
            String expectedSignature = hmacSha256(content, SECRET);

            if (!expectedSignature.equals(parts[2])) {
                throw new BusinessException("Token签名无效");
            }

            String payloadJson = new String(
                    Base64.getUrlDecoder().decode(parts[1]),
                    StandardCharsets.UTF_8
            );

            Map<String, Object> payload = OBJECT_MAPPER.readValue(payloadJson, Map.class);

            Long expireTime = Long.valueOf(payload.get("expireTime").toString());
            if (expireTime < System.currentTimeMillis()) {
                throw new BusinessException("Token已过期");
            }

            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(Long.valueOf(payload.get("userId").toString()));
            loginUser.setEmployeeId(Long.valueOf(payload.get("employeeId").toString()));
            loginUser.setUsername(payload.get("username").toString());
            loginUser.setRole(payload.get("role").toString());

            return loginUser;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Token解析失败");
        }
    }
}
