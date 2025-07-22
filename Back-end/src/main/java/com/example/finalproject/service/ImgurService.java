package com.example.finalproject.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class ImgurService {

    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "702a20757f3b296"; //

    public String uploadToImgur(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Tạo body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("image", base64Image);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Client-ID " + CLIENT_ID);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<ImgurResponse> response = restTemplate.postForEntity(
                    IMGUR_UPLOAD_URL,
                    request,
                    ImgurResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().getLink();
            }

            throw new RuntimeException("❌ Upload to Imgur failed");
        } catch (Exception e) {
            throw new RuntimeException("❌ Imgur upload error: " + e.getMessage(), e);
        }
    }


    static class ImgurResponse {
        private ImgurData data;
        private boolean success;
        private int status;

        public ImgurData getData() {
            return data;
        }

        public void setData(ImgurData data) {
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    static class ImgurData {
        private String link;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
