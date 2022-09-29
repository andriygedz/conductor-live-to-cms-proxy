package com.conductor.livetocmsproxy.controller.service;

public record Article(Rendered title, Rendered content) {
    public record Rendered(String rendered) {}
}
