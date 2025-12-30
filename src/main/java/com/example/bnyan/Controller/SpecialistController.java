package com.example.bnyan.Controller;
import com.example.bnyan.DTO.SpecialistDTO;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.SpecialistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bnyan.Api.ApiResponse;

@RestController
@RequestMapping("/api/v1/specialist")
@RequiredArgsConstructor
public class SpecialistController {


    private final SpecialistService specialistService;

    //only admin
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(specialistService.getAll());
    }

    //everyone
    @PostMapping("/register")
    public ResponseEntity<?> registerSpecialist(@RequestBody @Valid SpecialistDTO specialistDTO) {
        specialistService.registerSpecialist( specialistDTO);
        return ResponseEntity.ok(new ApiResponse("specialist registered successfully"));
    }

    //specialist
    @PostMapping("/assign-domain/{domain_id}")
    public ResponseEntity<?> assignDomain(@AuthenticationPrincipal User user, @PathVariable Integer domain_id) {
        specialistService.assignDomain(user.getId(), domain_id);
        return ResponseEntity.ok(new ApiResponse("domain assigned successfully"));
    }

    //specialist
    @PutMapping("/accept-request/{request_id}")
    public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal User user, @PathVariable Integer request_id) {
        specialistService.acceptRequest(user.getId(), request_id);
        return ResponseEntity.ok(new ApiResponse("request accepted successfully"));
    }

    //specialist
    @PutMapping("/reject-request/{request_id}")
    public ResponseEntity<?> rejectRequest(@AuthenticationPrincipal User user, @PathVariable Integer request_id) {
        specialistService.rejectRequest(user.getId(), request_id);
        return ResponseEntity.ok(new ApiResponse("request rejected successfully"));
    }

    //admin + specialist
    @PutMapping("/update/{spec_id}")
    public ResponseEntity<?> updateSpecialist(@AuthenticationPrincipal User user,@PathVariable Integer spec_id, @RequestBody Specialist specialist) {
        specialistService.updateSpecialist(user.getId(), spec_id, specialist);
        return ResponseEntity.ok(new ApiResponse("specialist updated successfully"));
    }

    // admin
    @DeleteMapping("/delete/{specialist_id}")
    public ResponseEntity<?> deleteSpecialist(@PathVariable Integer specialist_id) {
        specialistService.deleteSpecialist(specialist_id);
        return ResponseEntity.ok(new ApiResponse("specialist deleted successfully"));
    }

    //all users
    @GetMapping("/get/arch-eng")
    public ResponseEntity<?> getARCHITECTURAL_ENGINEER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getARCHITECTURAL_ENGINEER(user.getId()));
    }

    //all users
    @GetMapping("/get/civil-eng")
    public ResponseEntity<?> getCIVIL_ENGINEER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getCIVIL_ENGINEER(user.getId()));
    }

    //all users
    @GetMapping("/get/designer")
    public ResponseEntity<?> getDESIGNER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getDESIGNER(user.getId()));
    }

    //all users
    @GetMapping("/get/elect-eng")
    public ResponseEntity<?> getELECTRICAL_ENGINEER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getELECTRICAL_ENGINEER(user.getId()));
    }

    //all users
    @GetMapping("/get/gen-cont")
    public ResponseEntity<?> getGENERAL_CONTRACTOR(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getGENERAL_CONTRACTOR(user.getId()));
    }

    //all users
    @GetMapping("/get/mech-eng")
    public ResponseEntity<?> getMECHANICAL_ENGINEER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getMECHANICAL_ENGINEER(user.getId()));
    }

    //all users
    @GetMapping("/get/project-manager")
    public ResponseEntity<?> getPROJECT_MANAGER(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getPROJECT_MANAGER(user.getId()));
    }

    //specialist
    @GetMapping("/requests")
    public ResponseEntity<?> getMyRequests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(specialistService.getMyRequests(user.getId()));
    }

}
