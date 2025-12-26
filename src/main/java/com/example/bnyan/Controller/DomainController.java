package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Domain;
import com.example.bnyan.Service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(domainService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDomain(@RequestBody Domain domain) {
        domainService.addDomain(domain);
        return ResponseEntity.ok(new ApiResponse("domain added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDomain(@PathVariable Integer id, @RequestBody Domain domain) {
        domainService.updateDomain(id, domain);
        return ResponseEntity.ok(new ApiResponse("domain updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomain(@PathVariable Integer id) {
        domainService.delete(id);
        return ResponseEntity.ok(new ApiResponse("domain deleted successfully"));
    }
}
