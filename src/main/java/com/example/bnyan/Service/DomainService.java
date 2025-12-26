package com.example.bnyan.Service;
import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Domain;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Repository.DomainRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;

    public List<Domain> getAll() {
        return domainRepository.findAll();
    }

//    public Domain getByUserId(Integer id) {
//        return domainRepository.findByUser;
//    }

    public void addDomain(Domain domain) {
         domainRepository.save(domain);
    }


    public void updateDomain(Integer id, Domain domain) {
        Domain old = domainRepository.findDomainById(id);
        if(old==null){
            throw new ApiException("domain not found");
        }
        old.setDomain(domain.getDomain());
        domainRepository.save(old);
    }

    public void delete(Integer id) {
        Domain domain= domainRepository.findDomainById(id);
        if(domain==null){
            throw new ApiException("domain not found");
        }
        domainRepository.delete(domain);
    }
}
