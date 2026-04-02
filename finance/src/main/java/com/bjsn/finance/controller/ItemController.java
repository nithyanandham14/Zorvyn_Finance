package com.bjsn.finance.controller;

import com.bjsn.finance.Module.Item;
import com.bjsn.finance.Service.Itemservice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/bjsn")
public class ItemController {
    @Autowired
    Itemservice service;

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/item/{jwlno}")
    public ResponseEntity<Item> getitem(@PathVariable int jwlno)
    {
     Item item = service.getitemById(jwlno);
     if(item != null)
     {
       return new ResponseEntity<>(service.getitemById(jwlno), HttpStatus.OK);
     }else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @PostMapping("/item")
    public ResponseEntity<?>additem(@RequestBody Item item)
    {
        try{
            Item item1 = service.additem(item);
            return new ResponseEntity<>(item1,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @DeleteMapping("/item/{jwlno}")
    public ResponseEntity<String>DeleteItem(@PathVariable int jwlno)
    {
        Item item = service.getitemById(jwlno);
        if(item !=null)
        {

            service.deleteitem(jwlno);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Failed to Delete",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @PutMapping("/item/{jwlno}")
    public ResponseEntity<String> UpdateItem(@PathVariable int jwlno ,@RequestBody Item item)
    {
        Item item1 = null;
        try {
            item1 = service.updateItem(jwlno, item);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
        }

        if (item1 != null) {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/item/{jwlno}/interest/details")
    public ResponseEntity<Item> getFullInterestDetails(
            @PathVariable int jwlno,
            @RequestParam double monthlyRate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate overrideDate
    ) {
        LocalDate today = (overrideDate != null) ? overrideDate : LocalDate.now();

        Item result = service.getItemWithInterestDetails(jwlno, monthlyRate, today);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 with no body
        }

        return ResponseEntity.ok(result);  // 200 OK with result body
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @PutMapping("/item/{jwlno}/release")
    public ResponseEntity<String> markItemAsReleased(@PathVariable int jwlno, @RequestBody Item item) {
        Item existingItem = null;
        try {
            existingItem = service.getitemById(jwlno);
            if (existingItem == null) {
                return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
            }

            // Set status to false (Released)
            existingItem.setStatus(false);

            // Save the updated item
            Item updatedItem = service.updateItem(jwlno, existingItem);

            if (updatedItem != null) {
                return new ResponseEntity<>("Item marked as Released", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to release item", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error while updating item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/items/report/pdf")
    public ResponseEntity<byte[]> generateMonthlyReportPdf(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam double monthlyRate,
            @RequestParam(required = false) String filterStatus
    ) {
        try {
            byte[] pdfBytes = service.generateMonthlyReportPdf(month, year, monthlyRate, filterStatus);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("Report_" + month + "_" + year + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @PutMapping("/item/{jwlno}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable int jwlno,
            @RequestParam boolean status) {
        String response = service.updateItemStatus(jwlno, status);
        if (response.equals("Item not found")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/csrf")
    public CsrfToken gettoken(HttpServletRequest request)
    {
        return(CsrfToken) request.getAttribute("_csrf");
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/filter")
    public List<Item> filterItems(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        return service.filter(type, category);
    }

}
