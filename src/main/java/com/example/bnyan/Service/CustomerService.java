package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AiService aiService;

    public List<Customer> get() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) throw new ApiException("No customers found");
        return customers;
    }

    public Customer getCustomerById(Integer id) {
        Customer customer = customerRepository.getCustomerById(id);
        if (customer == null) throw new ApiException("Customer not found");
        return customer;
    }

    public QuestionDTO askAI(//Integer customer_id,
                             String question){
        //Customer customer = customerRepository.getCustomerById(customer_id);
        //if (customer == null) throw new ApiException("Customer not found");
        return aiService.askAI(question);
    }
}