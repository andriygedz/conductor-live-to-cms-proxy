package com.conductor.livetocmsproxy.controller.dto;

public record UpdateArticleDto(String wpHost,
                               long articleId,
                               boolean isPost,
                               String user,
                               String password,
                               String title,
                               String content) {}
