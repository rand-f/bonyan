package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
//import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.DTO.CustomerDTO;
import com.example.bnyan.DTO.MessageDTO;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Model.*;
//import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Repository.*;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Model.Customer;
//import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.OpenAI.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AiService aiService;
    private final LandRepository landRepository;
    private final BuiltRepository builtRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<Customer> get() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) throw new ApiException("No customers found");
        return customers;
    }

    public void registerCustomer(CustomerDTO customerDTO) {
        if (userRepository.getUserByUsername(customerDTO.getUsername()) != null)
            throw new ApiException("Username already exists");

        if (userRepository.getUserByEmail(customerDTO.getEmail()) != null)
            throw new ApiException("Email already exists");

        User user = new User();
        user.setUsername(customerDTO.getUsername());
        String hash = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        user.setPassword(hash);
        user.setEmail(customerDTO.getEmail());
        user.setPhoneNumber(customerDTO.getPhoneNumber());
        user.setFullName(customerDTO.getFullName());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(user);
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Integer userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);
        if (customer == null) throw new ApiException("Customer not found");
        return customer;
    }

    public QuestionDTO askAI(Integer userId, String question) {
        Customer customer = customerRepository.getCustomerByUserId(userId);
        if (customer == null) throw new ApiException("Customer not found");
        return aiService.askAI(question);
    }

    public List<?> getMyProperties(Integer userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);
        if (customer == null) throw new ApiException("Customer not found");

        List<Object> properties = new ArrayList<>();
        List<Land> lands = landRepository.getLandsByCustomerId(customer.getId());
        List<Built> builts = builtRepository.getBuiltsByUserId(customer.getUser().getId());

        if (lands.isEmpty() && builts.isEmpty()) {
            throw new ApiException("You have no registered properties");
        }

        properties.addAll(lands);
        properties.addAll(builts);

        return properties;
    }

    public List<Project> onGoingProjects(Integer userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);
        if (customer == null) throw new ApiException("Customer not found");

        List<Project> onGoing = projectRepository.findProjectByCustomerAndStatus(customer, "on going");
        if (onGoing.isEmpty()) throw new ApiException("You have no ongoing projects");
        return onGoing;
    }

    public List<Project> completedProjects(Integer userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);
        if (customer == null) throw new ApiException("Customer not found");

        List<Project> completed = projectRepository.findProjectByCustomerAndStatus(customer, "completed");
        if (completed.isEmpty()) throw new ApiException("You have no completed projects");
        return completed;
    }
}

