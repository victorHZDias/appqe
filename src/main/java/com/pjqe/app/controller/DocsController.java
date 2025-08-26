package com.pjqe.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/api-docs")
    public String apiDocsPage() {
        // Retorna o nome do arquivo HTML (sem a extensão .html)
        // que está na pasta 'src/main/resources/templates'
        return "api";
    }
}
