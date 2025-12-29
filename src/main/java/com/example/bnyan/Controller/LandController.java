package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Service.LandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/land")
@RequiredArgsConstructor
public class LandController {

    private final LandService landService;

    /// CRUD endpoints

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200)
                .body(landService.getAllLands());
    }

    @PostMapping("/add/{customerId}")
    public ResponseEntity<?> add(@PathVariable Integer customerId, @RequestBody @Valid Land land) {

        landService.add(customerId, land);
        return ResponseEntity.status(200).body(new ApiResponse("Land added"));
    }

    @PutMapping("/update/{landId}/{customerId}")
    public ResponseEntity<?> update(@PathVariable Integer landId, @PathVariable Integer customerId, @RequestBody @Valid Land land) {

        landService.update(landId, customerId, land);
        return ResponseEntity.status(200).body(new ApiResponse("Land updated"));
    }


    @DeleteMapping("/delete/{landId}/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Integer landId, @PathVariable Integer customerId) {

        landService.delete(landId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Land deleted"));
    }

    /// Extra endpoints

    @GetMapping("/get-by-customer-id/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(landService.getLandsByCustomer(customerId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(landService.getLandById(id));
    }
}
