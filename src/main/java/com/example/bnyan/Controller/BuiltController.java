package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Built;
import com.example.bnyan.Service.BuiltService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/built")
@RequiredArgsConstructor
public class BuiltController {

    private final BuiltService builtService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(builtService.get());
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> add(@PathVariable Integer userId, @RequestBody @Valid Built built) {
        builtService.add(userId, built);
        return ResponseEntity.status(200).body(new ApiResponse("Built added"));
    }

    @PutMapping("/update/{builtId}/{userId}")
    public ResponseEntity<?> update(@PathVariable Integer builtId, @PathVariable Integer userId, @RequestBody @Valid Built built) {
        builtService.update(builtId, userId, built);
        return ResponseEntity.status(200).body(new ApiResponse("Built updated"));
    }

    @DeleteMapping("/delete/{builtId}/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer builtId, @PathVariable Integer userId) {
        builtService.delete(builtId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Built deleted"));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getBuiltById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(builtService.getBuiltById(id));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getBuiltsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(builtService.getBuiltsByStatus(status));
    }

    @GetMapping("/get-by-price-less-than-or-equal/{price}")
    public ResponseEntity<?> getBuiltsByPriceLessThanOrEqual(@PathVariable Double price) {
        return ResponseEntity.status(200).body(builtService.getBuiltsByPriceLessThanOrEqual(price));
    }

    @GetMapping("/get-by-location/{location}")
    public ResponseEntity<?> getBuiltsByLocation(@PathVariable String location) {
        return ResponseEntity.status(200).body(builtService.getBuiltsByLocation(location));
    }

    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<?> getBuiltsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(builtService.getBuiltsByUserId(userId));
    }
}