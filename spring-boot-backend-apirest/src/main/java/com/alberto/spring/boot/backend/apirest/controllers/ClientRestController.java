package com.alberto.spring.boot.backend.apirest.controllers;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;
import com.alberto.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> find(@PathVariable Long id){
        List<Client> client = new ArrayList<Client>();
        Map<String, String> response = new HashMap<>();
        try{
            client.add(clientService.findById(id));
        }catch (DataAccessException e){
            response.put("message", "Error Accessing data" );
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(client.get(0) == null){
            response.put("message", "The client " + id + " does not exist" );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Client>>(client, HttpStatus.OK);
    }

    @DeleteMapping("/client/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id){
        clientService.delete(id);
    }

    @PostMapping("/client")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Client client){
        Client newClient = null;
        Map<String, String> response = new HashMap<>();
        try{
        newClient = clientService.save(client);
        } catch (DataAccessException e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(newClient, HttpStatus.OK);
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
