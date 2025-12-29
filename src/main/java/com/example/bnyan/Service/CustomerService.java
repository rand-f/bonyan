package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
//import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.DTO.MessageDTO;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Model.Built;
import com.example.bnyan.Model.Customer;
//import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Model.Project;
import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Repository.BuiltRepository;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.LandRepository;
import com.example.bnyan.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void sendMessage(Integer customer_id,
                            MessageDTO message){

        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer == null) throw new ApiException("Customer not found");

        if(message.getFullName()==null){
            message.setFullName(customer.getUser().getFullName());
        }
        if(message.getEmail()==null){
            message.setEmail(customer.getUser().getEmail());
        }

        // message should be sent as email
    }

    public List<?> getMyProperties(Integer customer_id){
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer == null) throw new ApiException("Customer not found");

        ArrayList properties = new ArrayList<>();

        List<Land>lands=landRepository.getLandsByCustomerId(customer_id);
        List<Built>builts=builtRepository.getBuiltsByUserId(customer_id);

        if(lands.isEmpty()&&builts.isEmpty()){
            throw new ApiException("you have no registered properties");
        }

        for (Land land:lands){
            properties.add(land);
        }
        for (Built built:builts){
            properties.add(built);
        }

        return properties;
    }

    public List<Project>onGoingProjects(Integer customer_id){
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer == null) throw new ApiException("Customer not found");

        List<Project>onGoing= projectRepository.findProjectByCustomerAndStatus(customer, "on going");
        if(onGoing.isEmpty()){
            throw new ApiException("you have no on going projects");
        }
        return  onGoing;
    }

    public List<Project> completedProjects(Integer customer_id){
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer == null) throw new ApiException("Customer not found");

        List<Project> completed = projectRepository.findProjectByCustomerAndStatus(customer, "completed");
        if(completed.isEmpty()){
            throw new ApiException("you have no completed projects");
        }
        return completed;
    }


}