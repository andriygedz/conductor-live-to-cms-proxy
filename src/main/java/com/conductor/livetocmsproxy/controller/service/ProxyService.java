package com.conductor.livetocmsproxy.controller.service;

import com.conductor.livetocmsproxy.controller.dto.ArticleDto;
import com.conductor.livetocmsproxy.controller.dto.GetArticleDto;
import com.conductor.livetocmsproxy.controller.dto.UpdateArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
public class ProxyService {
    private final RestTemplate restTemplate;

    @SneakyThrows
    public ArticleDto getArticle(GetArticleDto getArticleDto) {
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
        HttpHeaders headers = createHeaders(updateArticleDto.user(), updateArticleDto.user());
        String url = createUpdateArticleUri(updateArticleDto);
        ResponseEntity<Void> entity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Void.class);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Fail to communicate with service" + entity.getStatusCodeValue());
        }
    }

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
