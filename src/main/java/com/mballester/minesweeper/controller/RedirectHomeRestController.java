package com.mballester.minesweeper.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RedirectHomeRestController {

    @Value("${springdoc.swagger-ui.path:swagger-ui.html}")
    private String apiDoc;

    @GetMapping("/")
    public RedirectView redirectToApiDoc() {
        return new RedirectView(apiDoc);
    }
}
