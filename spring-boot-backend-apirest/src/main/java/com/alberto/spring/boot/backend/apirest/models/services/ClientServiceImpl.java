package com.alberto.spring.boot.backend.apirest.models.services;

import java.util.List;

import com.alberto.spring.boot.backend.apirest.models.dao.IClientDao;
import com.alberto.spring.boot.backend.apirest.models.entity.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        List<Client> clients = (List<Client>) clientDao.findAll();
        return clients;
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return (Client) clientDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientDao.deleteById(id);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        return clientDao.save(client);
    }

}
