package com.example.bnyan.Controller;
import com.example.bnyan.DTO.SpecialistDTO;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Service.SpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bnyan.Api.ApiResponse;

@RestController
@RequestMapping("/api/v1/specialist")
@RequiredArgsConstructor
public class SpecialistController {


    private final SpecialistService specialistService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(specialistService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerSpecialist(@RequestBody SpecialistDTO specialistDTO) {
        specialistService.registerSpecialist(specialistDTO);
        return ResponseEntity.ok(new ApiResponse("specialist registered successfully"));
    }

    @PostMapping("/assign-domain/{specialist_id}/{domain_id}")
    public ResponseEntity<?> assignDomain(@PathVariable Integer specialist_id, @PathVariable Integer domain_id) {
        specialistService.assignDomain(specialist_id, domain_id);
        return ResponseEntity.ok(new ApiResponse("domain assigned successfully"));
    }

    @PutMapping("/accept-request/{spec_id}/{request_id}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer spec_id, @PathVariable Integer request_id) {
        specialistService.acceptRequest(spec_id, request_id);
        return ResponseEntity.ok(new ApiResponse("request accepted successfully"));
    }

    @PutMapping("/reject-request/{spec_id}/{request_id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer spec_id, @PathVariable Integer request_id) {
        specialistService.rejectRequest(spec_id, request_id);
        return ResponseEntity.ok(new ApiResponse("request rejected successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSpecialist(@PathVariable Integer id, @RequestBody Specialist specialist) {
        specialistService.updateSpecialist(id, specialist);
        return ResponseEntity.ok(new ApiResponse("specialist updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSpecialist(@PathVariable Integer id) {
        specialistService.deleteSpecialist(id);
        return ResponseEntity.ok(new ApiResponse("specialist deleted successfully"));
    }

    @GetMapping("/get/arch-eng")
    public ResponseEntity<?> getARCHITECTURAL_ENGINEER() {
        return ResponseEntity.ok(specialistService.getARCHITECTURAL_ENGINEER());
    }
    @GetMapping("/get/civil-eng")
    public ResponseEntity<?> getCIVIL_ENGINEER() {
        return ResponseEntity.ok(specialistService.getCIVIL_ENGINEER());
    }
    @GetMapping("/get/designer")
    public ResponseEntity<?> getDESIGNER() {
        return ResponseEntity.ok(specialistService.getDESIGNER());
    }
    @GetMapping("/get/elect-eng")
    public ResponseEntity<?> getELECTRICAL_ENGINEER() {
        return ResponseEntity.ok(specialistService.getELECTRICAL_ENGINEER());
    }
    @GetMapping("/get/gen-cont")
    public ResponseEntity<?> getGENERAL_CONTRACTOR() {
        return ResponseEntity.ok(specialistService.getGENERAL_CONTRACTOR());
    }
    @GetMapping("/get/mech-eng")
    public ResponseEntity<?> getMECHANICAL_ENGINEER() {
        return ResponseEntity.ok(specialistService.getMECHANICAL_ENGINEER());
    }
    @GetMapping("/get/project-manager")
    public ResponseEntity<?> getPROJECT_MANAGER() {
        return ResponseEntity.ok(specialistService.getPROJECT_MANAGER());
    }

    @GetMapping("/requests/{spec_id}")
    public ResponseEntity<?> getMyRequests(@PathVariable Integer spec_id) {
        return ResponseEntity.ok(specialistService.getMyRequests(spec_id));
    }

}
