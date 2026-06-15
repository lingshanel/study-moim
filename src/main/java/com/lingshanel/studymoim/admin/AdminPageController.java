package com.lingshanel.studymoim.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/ops/study-moim-admin")
    public String adminPage() {
        return "forward:/admin.html";
    }
}
