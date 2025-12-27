package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.SpecialistDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.DomainRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import com.example.bnyan.Repository.SpecialistRequestRepository;
import com.example.bnyan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        user.setPassword(specialistDTO.getPassword());
        user.setEmail(specialistDTO.getEmail());
        user.setPhoneNumber(specialistDTO.getPhoneNumber());
        user.setRole("SPECIALIST");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Specialist specialist = new Specialist();
        specialist.setSpeciality(specialistDTO.getSpeciality());
        specialist.setUser(user);
        specialistRepository.save(specialist);
    }

    public void assignDomain(Integer specialist_id, Integer domain_id) {
        Specialist specialist = specialistRepository.findSpecialistById(specialist_id);
        Domain domain = domainRepository.findDomainById(domain_id);

        if (specialist == null || domain == null) {
            throw new ApiException("can not assign this domain to this specialist");
        }

        domain.getSpecialists().add(specialist);
        specialist.getDomains().add(domain);
        specialistRepository.save(specialist);
        domainRepository.save(domain);
    }

    public void acceptRequest(Integer spec_id, Integer request_id) {
        Specialist specialist = specialistRepository.findSpecialistById(spec_id);
        SpecialistRequest specialistRequest = specialistRequestRepository.findSpecialistRequestById(request_id);

        if (specialist == null || specialistRequest == null) {
            throw new ApiException("can not accept this request");
        }

        if (!specialist.getId().equals(specialistRequest.getSpecialist().getId())) {
            throw new ApiException("unauthorized to accept this request");
        }

        specialistRequestService.acceptRequest(request_id);
    }

    public void rejectRequest(Integer spec_id, Integer request_id) {
        Specialist specialist = specialistRepository.findSpecialistById(spec_id);
        SpecialistRequest specialistRequest = specialistRequestRepository.findSpecialistRequestById(request_id);

        if (specialist == null || specialistRequest == null) {
            throw new ApiException("can not reject this request");
        }

        if (!specialist.getId().equals(specialistRequest.getSpecialist().getId())) {
            throw new ApiException("unauthorized to reject this request");
        }

        specialistRequestService.rejectRequest(request_id);
    }

    public void updateSpecialist(Integer id, Specialist specialist) {
        Specialist old = specialistRepository.findSpecialistById(id);
        if (old == null) {
            throw new ApiException("specialist not found");
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
}