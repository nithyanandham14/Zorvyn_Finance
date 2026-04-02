package com.bjsn.finance.controller;

import com.bjsn.finance.Service.MetalRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class MetalRateController {

    @Autowired
    private MetalRateService metalRateService;
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/metal-rates")
    public ResponseEntity<Map<String, Double>> getRates() {
        return ResponseEntity.ok(metalRateService.fetchRates());
    }
}