package com.arbitrage.common.kakao;

import com.arbitrage.common.kakao.dto.ReissueResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;



@Service
@RequiredArgsConstructor
public class KakaoMSGService {
    private final RestTemplate restTemplate;

    @Value("${kakao.url.sendme}")
    private String sendMeUrl;
    @Value("${kakao.url.oauth}")
    private String oauthUrl;
    @Value("${kakao.key.restapi}")
    private String restApi;
    public void sendMeMSG(String text, String linkURL, String accessToken){
        HashMap<String, Object> map = new HashMap<>();      //APPLICATION_FORM_URLENCODED는 HashMap으로 해야 자동 변환해줌
        ObjectMapper objectMapper = new ObjectMapper();

        map.put("object_type","text");
        map.put("text",text);
        map.put("button_title","확인");

        Map<String,String> linkmap = new HashMap<>();
        linkmap.put("web_url",linkURL);
        linkmap.put("mobile_web_url",linkURL);

        map.put("link",linkmap);

        String  templateObjectJSON;
        try {
            templateObjectJSON = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MultiValueMap<String, String> templateObject = new LinkedMultiValueMap<>();
        templateObject.add("template_object",templateObjectJSON);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","Bearer " + accessToken);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(templateObject,headers);
        System.out.println(templateObject.toString());
        String response = restTemplate.postForObject(sendMeUrl,entity,String.class);

        System.out.println(response);

    }
    public String accessTokenReissue(){
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","refresh_token");
        map.add("client_id",restApi);
        map.add("refresh_token","x7sWtJUD1PJkFRSUkYb70EN72gdu1lpflqcKKcjZAAABjoDx06bokopMIboAuA");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);
        ReissueResponse response = restTemplate.postForObject(oauthUrl,entity, ReissueResponse.class);
//        System.out.println(response.getAccess_token());
        return response.getAccess_token();
    }


}
