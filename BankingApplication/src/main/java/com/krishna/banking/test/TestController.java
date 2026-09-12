package com.krishna.banking.test;

import com.krishna.banking.user.security.CurrentUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final CurrentUser currentUser;

    public TestController(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @GetMapping
    public String jwtTester() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());

        System.out.println("User ID from JWT:" + currentUser.getId());
        return "Auth success";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerOnly() {
        return "Customer endpoint";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Admin endpoint";
    }
}
