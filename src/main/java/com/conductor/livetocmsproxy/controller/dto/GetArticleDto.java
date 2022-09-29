package com.conductor.livetocmsproxy.controller.dto;

public record GetArticleDto(String wpHost,
                            long articleId,
                            boolean isPost){}
