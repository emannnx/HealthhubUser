package com.example.YourUserApp1;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8091").build();
    }

    public String postWithWebClient(Object task) {
        return webClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(task)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<Object> getWithWebClient() {
        return webClient.get()
                .uri("/getAll")
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public String deleteWithWebClient(int id) {
        return webClient.delete()
                .uri("/delete/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // ✅ Added updateTaskWithWebClient method
    public String updateTaskWithWebClient(int id, Object task) {
        return webClient.put()
                .uri("/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(task)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
