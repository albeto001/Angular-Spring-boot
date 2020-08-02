package com.alberto.spring.boot.backend.apirest.models.services;

import java.util.List;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;

public interface IClientService {

    public List<Client> findAll();

    public  Client findById(Long id);

    public void delete(Long id);

    public Client save(Client client);
    
}
