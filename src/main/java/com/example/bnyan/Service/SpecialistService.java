package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.SpecialistDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final DomainRepository domainRepository;
    private final UserRepository userRepository;
    private final SpecialistRequestRepository specialistRequestRepository;
    private final SpecialistRequestService specialistRequestService;
    private final ProjectManagerRepository projectManagerRepository;

    public List<Specialist> getAll() {
        return specialistRepository.findAll();
    }

    public void registerSpecialist(SpecialistDTO specialistDTO) {
        if (userRepository.getUserByUsername(specialistDTO.getUsername()) != null)
            throw new ApiException("Username already exists");

        if (userRepository.getUserByEmail(specialistDTO.getEmail()) != null)
            throw new ApiException("Email already exists");

        User user = new User();
        user.setUsername(specialistDTO.getUsername());
        String hash = new BCryptPasswordEncoder().encode(specialistDTO.getPassword());
        user.setPassword(hash);
        user.setEmail(specialistDTO.getEmail());
        user.setPhoneNumber(specialistDTO.getPhoneNumber());
        user.setFullName(specialistDTO.getFullName());
        user.setRole("SPECIALIST");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Specialist specialist = new Specialist();
        specialist.setSpeciality(specialistDTO.getSpeciality());
        specialist.setUser(user);
        if(specialistDTO.getSpeciality().equals("PROJECT_MANAGER")){
            ProjectManager manager = new ProjectManager();
            manager.setUser(user);
            manager.setSpeciality(specialist.getSpeciality());
            projectManagerRepository.save(manager);
        }
        specialistRepository.save(specialist);
    }

    public void assignDomain(Integer user_id, Integer domain_id) {

        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){throw new ApiException("only specialist can make this process");}

        Domain domain = domainRepository.findDomainById(domain_id);

        if (domain == null) {
            throw new ApiException("domain not found");
        }

        domain.getSpecialists().add(specialist);
        specialist.getDomains().add(domain);
        specialistRepository.save(specialist);
        domainRepository.save(domain);
    }

    public void acceptRequest(Integer user_id, Integer request_id) {
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        Specialist specialist = specialistRepository.findSpecialistById(user_id);

        SpecialistRequest specialistRequest = specialistRequestRepository.findSpecialistRequestById(request_id);

        if (specialist == null || specialistRequest == null) {
            throw new ApiException("can not accept this request");
        }

        if (!specialist.getId().equals(specialistRequest.getSpecialist().getId())) {
            throw new ApiException("unauthorized to accept this request");
        }

        specialistRequestService.acceptRequest(user_id, request_id);
    }

    public void rejectRequest(Integer user_id, Integer request_id) {

        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        SpecialistRequest specialistRequest = specialistRequestRepository.findSpecialistRequestById(request_id);

        if (specialist == null || specialistRequest == null) {
            throw new ApiException("can not reject this request");
        }

        if (!specialist.getId().equals(specialistRequest.getSpecialist().getId())) {
            throw new ApiException("unauthorized to reject this request");
        }

        specialistRequestService.rejectRequest(user_id,request_id);
    }

    public void updateSpecialist(Integer user_id,Integer spec_id, Specialist specialist) {
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        Specialist old = specialistRepository.findSpecialistById(spec_id);
        if (old == null) {
            throw new ApiException("specialist not found");
        }

        if(!user.getRole().equals("ADMIN") && old.getId()!=user.getId()){
            throw new ApiException("unauthorized");
        }

        old.setSpeciality(specialist.getSpeciality());

        specialistRepository.save(old);
    }

    public void deleteSpecialist(Integer id) {
        Specialist specialist = specialistRepository.findSpecialistById(id);
        if (specialist == null) {
            throw new ApiException("specialist not found");
        }
        specialistRepository.delete(specialist);
    }

    public List<Specialist>getARCHITECTURAL_ENGINEER(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("ARCHITECTURAL_ENGINEER");
    }

    public List<Specialist>getMECHANICAL_ENGINEER(Integer user_id){
        User user = userRepository.getUserById(user_id);

        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("MECHANICAL_ENGINEER");
    }

    public List<Specialist>getELECTRICAL_ENGINEER(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("ELECTRICAL_ENGINEER");
    }

    public List<Specialist>getCIVIL_ENGINEER(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("CIVIL_ENGINEER");
    }

    public List<Specialist>getPROJECT_MANAGER(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("PROJECT_MANAGER");
    }

    public List<Specialist>getDESIGNER(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("DESIGNER");
    }

    public List<Specialist>getGENERAL_CONTRACTOR(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user  not fond");
        }

        return specialistRepository.findSpecialistBySpeciality("CONTRACTOR");
    }

    public List<SpecialistRequest>getMyRequests(Integer user_id){
        User user = userRepository.getUserById(user_id);
        if (user==null){
            throw new ApiException("user not fond");
        }

        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){
            throw new ApiException("only specialist ca do this process");
        }

        List<SpecialistRequest> myRequests= specialistRequestRepository.findSpecialistRequestBySpecialist(specialist);
        if(myRequests.isEmpty()){
            throw new ApiException("you have no requests");
        }
        
        return myRequests;
    }
}