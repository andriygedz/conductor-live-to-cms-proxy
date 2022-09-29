package com.conductor.livetocmsproxy.controller;

import com.conductor.livetocmsproxy.controller.dto.ArticleDto;
import com.conductor.livetocmsproxy.controller.dto.GetArticleDto;
import com.conductor.livetocmsproxy.controller.dto.UpdateArticleDto;
import com.conductor.livetocmsproxy.controller.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping("/article")
    public ArticleDto getArticle(GetArticleDto getArticleDto){
        return proxyService.getArticle(getArticleDto);
    }

    @PostMapping("/article")
    public void updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        proxyService.updateArticle(updateArticleDto);
    }
}
