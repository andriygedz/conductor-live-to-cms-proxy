package com.conductor.livetocmsproxy.controller.service;

import com.conductor.livetocmsproxy.controller.dto.ArticleDto;
import com.conductor.livetocmsproxy.controller.dto.GetArticleDto;
import com.conductor.livetocmsproxy.controller.dto.UpdateArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class ProxyService {
    private final RestTemplateBuilder restTemplateBuilder;

    @SneakyThrows
    public ArticleDto getArticle(GetArticleDto getArticleDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<Article> entity = restTemplate.getForEntity(createGetArticleUri(getArticleDto), Article.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            Article article = entity.getBody();
            return new ArticleDto(article.title().rendered(), article.content().rendered());
        }
        throw new IllegalArgumentException("Fail to communicate with service" + entity.getStatusCodeValue());
    }

    @SneakyThrows
    private String createGetArticleUri(GetArticleDto getArticleDto) {
        URL url = new URL(getArticleDto.wpHost());
        String apiMethod = getArticleDto.isPost() ? "posts" : "pages";
        return url.getProtocol() + "://" + url.getHost() + "/wp-json/wp/v2/" + apiMethod + "/" + getArticleDto.articleId();
    }

    @SneakyThrows
    private String createUpdateArticleUri(UpdateArticleDto updateArticleDto) {
        URL url = new URL(updateArticleDto.wpHost());
        String apiMethod = updateArticleDto.isPost() ? "posts" : "pages";
        return url.getProtocol() + "://" + url.getHost() + "/wp-json/wp/v2/" + apiMethod + "/" + updateArticleDto.articleId();
    }

    public void updateArticle(UpdateArticleDto updateArticleDto){
        ArticleDto requestBody = new ArticleDto(updateArticleDto.title(), updateArticleDto.content());
        String url = createUpdateArticleUri(updateArticleDto);
        RestTemplate restTemplate = restTemplateBuilder.basicAuthentication(updateArticleDto.user(), updateArticleDto.password()).build();
        ResponseEntity<Void> entity = restTemplate.postForEntity(url, requestBody, Void.class);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Fail to communicate with service" + entity.getStatusCodeValue());
        }
    }
}
