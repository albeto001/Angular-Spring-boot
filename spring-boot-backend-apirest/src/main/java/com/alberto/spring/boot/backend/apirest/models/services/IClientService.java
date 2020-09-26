package com.alberto.spring.boot.backend.apirest.models.services;

import java.util.List;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {

    public List<Client> findAll();

    public Page<Client> findAll(Pageable pageable);

    public  Client findById(Long id);

    public void delete(Long id);

    public Client save(Client client);
    
}
