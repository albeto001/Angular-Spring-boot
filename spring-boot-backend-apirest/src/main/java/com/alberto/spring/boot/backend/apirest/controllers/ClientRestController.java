package com.alberto.spring.boot.backend.apirest.controllers;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;
import com.alberto.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index(){
        List<Client> clients = clientService.findAll();
        return clients;
    }

    @GetMapping("/client/{id}")
    public List<Client> find(@PathVariable Long id){
        List<Client> client = new ArrayList<Client>();
        client.add(clientService.findById(id));
        return client;
    }

    @DeleteMapping("/client/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id){
        clientService.delete(id);
    }

    @PostMapping("/client")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client add(@RequestBody Client client){
        return clientService.save(client);
    }

    @PutMapping("/client/{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client update(@RequestBody Client client, @PathVariable Long id){
        Client clientToUpdate = clientService.findById(id);

        clientToUpdate.setEmail(client.getEmail());
        clientToUpdate.setName(client.getName());
        clientToUpdate.setSurname(client.getSurname());

        return clientService.save(clientToUpdate);
    }


}
