package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.LandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/land")
@RequiredArgsConstructor
public class LandController {

    private final LandService landService;

    /// CRUD endpoints

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(landService.getAllLands(authUser));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@AuthenticationPrincipal User authUser, @RequestBody @Valid Land land) {
        landService.add(authUser, land);
        return ResponseEntity.ok(new ApiResponse("Land added"));
    }

    @PutMapping("/update/{landId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User authUser, @PathVariable Integer landId, @RequestBody @Valid Land land) {

        landService.update(authUser, landId, land);
        return ResponseEntity.ok(new ApiResponse("Land updated"));
    }

    @DeleteMapping("/delete/{landId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User authUser, @PathVariable Integer landId) {
        landService.delete(authUser, landId);
        return ResponseEntity.ok(new ApiResponse("Land deleted"));
    }

    /// Extra endpoints

    @GetMapping("/my-lands")
    public ResponseEntity<?> getMyLands(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(landService.getMyLands(authUser));
    }

    @GetMapping("/get-by-id/{landId}")
    public ResponseEntity<Land> getById(@AuthenticationPrincipal User authUser, @PathVariable Integer landId) {
        return ResponseEntity.ok(landService.getLandById(authUser, landId));
    }
}
