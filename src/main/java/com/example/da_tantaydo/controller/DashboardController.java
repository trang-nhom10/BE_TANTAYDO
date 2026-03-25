package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor

public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DASHBOARD')")
    public ResponseEntity<?> getOverview() {
        try {
            return ResponseEntity.ok(dashboardService.getOverview());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @GetMapping("/age-distribution")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DASHBOARD')")
    public ResponseEntity<?> getAgeDistribution() {
        try {
            return ResponseEntity.ok(dashboardService.getAgeDistribution());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @GetMapping("/status-rate")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DASHBOARD')")
    public ResponseEntity<?> getStatusRate() {
        try {
            return ResponseEntity.ok(dashboardService.getStatusRate());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @GetMapping("/monthly-stats")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DASHBOARD')")
    public ResponseEntity<?> getMonthlyStats() {
        try {
            return ResponseEntity.ok(dashboardService.getMonthlyStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }
}
