package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Domain;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;

    //only admin
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(domainService.getAll());
    }

    //only specialist
    @GetMapping("/get-specialist")
    public ResponseEntity<?> getMyDomain(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(domainService.getMyDomain(user.getId()));
    }

    //only specialist
    @PostMapping("/add")
    public ResponseEntity<?> addDomain(@AuthenticationPrincipal User user,@RequestBody Domain domain) {
        domainService.addDomain(user.getId(), domain);
        return ResponseEntity.ok(new ApiResponse("domain added successfully"));
    }

    //only specialist
    @PutMapping("/update/{domain_id}")
    public ResponseEntity<?> updateDomain(@AuthenticationPrincipal User user,@PathVariable Integer domain_id, @RequestBody Domain domain) {
        domainService.updateDomain(user.getId(), domain_id, domain);
        return ResponseEntity.ok(new ApiResponse("domain updated successfully"));
    }

    //only specialist
    @DeleteMapping("/delete/{domain_id}")
    public ResponseEntity<?> deleteDomain(@AuthenticationPrincipal User user, @PathVariable Integer domain_id) {
        domainService.delete(user.getId(),domain_id);
        return ResponseEntity.ok(new ApiResponse("domain deleted successfully"));
    }
}
