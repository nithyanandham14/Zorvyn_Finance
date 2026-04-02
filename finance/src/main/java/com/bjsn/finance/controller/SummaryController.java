package com.bjsn.finance.controller;

import com.bjsn.finance.Module.Item;
import com.bjsn.finance.Service.Itemservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nithy
 **/
@RestController
@RequestMapping("/summary")
public class SummaryController {

        @Autowired
        private Itemservice itemService;

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/total-loan")
        public double totalLoan() {
            return itemService.getTotalLoan();
        }
//
//        @GetMapping("/total-interest")
//        public double totalInterest() {
//            return itemService.getTotalInterest();
//        }
//
//        @GetMapping("/net")
//        public double netBalance() {
//            return itemService.getNetBalance();
//        }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public List<Item> filter(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        return itemService.filter(type, category);
    }
    }
