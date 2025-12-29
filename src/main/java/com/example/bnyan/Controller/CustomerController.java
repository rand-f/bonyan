package com.example.bnyan.Controller;

//import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(customerService.get());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(customerService.getCustomerById(id));
    }

    @PostMapping("/ask-ai")
    public ResponseEntity<?> askAI(@RequestBody QuestionDTO questionDTO){
        return ResponseEntity.ok(customerService.askAI(questionDTO.getQuestion()));
    }

    @GetMapping("/get-properties/{id}")
    public ResponseEntity<?> getCustomerProperties(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(customerService.getMyProperties(id));
    }

    @GetMapping("/on-going-projects/{id}")
    public ResponseEntity<?> getCustomerOnGoingProjects(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(customerService.onGoingProjects(id));
    }

    @GetMapping("/completed-projects/{id}")
    public ResponseEntity<?> getCustomerCompletedProjects(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(customerService.completedProjects(id));
    }
}