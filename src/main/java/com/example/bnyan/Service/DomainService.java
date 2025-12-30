package com.example.bnyan.Service;
import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Domain;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.User;
import com.example.bnyan.Repository.DomainRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import com.example.bnyan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;
    private final UserRepository userRepository;
    private final SpecialistRepository specialistRepository;

    public List<Domain> getAll() {
        return domainRepository.findAll();
    }

    public Domain getMyDomain(Integer user_id) {
        User user = userRepository.getUserById(user_id);
        if(user==null){throw new ApiException("User not found");}
        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){throw new ApiException("only specialist can make this process");}

        Set<Specialist> specialists = new HashSet<>();
        specialists.add(specialist);

        return domainRepository.findDomainBySpecialists(specialists);
    }

    public void addDomain(Integer user_id,Domain domain) {
        User user = userRepository.getUserById(user_id);
        if(user==null){throw new ApiException("User not found");}

        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){throw new ApiException("only specialist can make this process");}

        domainRepository.save(domain);
    }

    public void updateDomain(Integer user_id, Integer id, Domain domain) {
        User user = userRepository.getUserById(user_id);
        if(user==null){throw new ApiException("User not found");}
        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){throw new ApiException("only specialist can make this process");}

        Domain old = domainRepository.findDomainById(id);
        if(old==null){
            throw new ApiException("domain not found");
        }
        boolean hasSpecialist= old.getSpecialists().contains(specialist);
        if(!hasSpecialist){
            throw new ApiException("unauthorized access");
        }

        old.setDomain(domain.getDomain());
        domainRepository.save(old);
    }

    public void delete(Integer user_id, Integer id) {
        User user = userRepository.getUserById(user_id);
        if(user==null){throw new ApiException("User not found");}
        Specialist specialist = specialistRepository.findSpecialistById(user_id);
        if(specialist==null){throw new ApiException("only specialist can make this process");}

        Domain domain= domainRepository.findDomainById(id);
        if(domain==null){
            throw new ApiException("domain not found");
        }

        boolean hasSpecialist= domain.getSpecialists().contains(specialist);

        if(!hasSpecialist){
            throw new ApiException("unauthorized access");
        }

        domainRepository.delete(domain);
    }
}
