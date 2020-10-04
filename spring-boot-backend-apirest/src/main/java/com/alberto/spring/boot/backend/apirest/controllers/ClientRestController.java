package com.alberto.spring.boot.backend.apirest.controllers;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;
import com.alberto.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index(){
        return  clientService.findAll();
    }

    @GetMapping("/clients/page/{page}")
    public Page<Client> page(@PathVariable Integer page){
        return clientService.findAll(PageRequest.of(page, 4));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<?> find(@PathVariable Long id){
        List<Client> client = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        try{
            client.add(clientService.findById(id));
        }catch (DataAccessException e){
            response.put("message", "Error Accessing data" );
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause());
        }
        if(client.get(0) == null){
            response.put("message", "The client " + id + " does not exist" );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Client>>(client, HttpStatus.OK);
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        Map<String, String> response = new HashMap<>();
        try {
            clientService.delete(id);
            response.put("message", id + " was deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e ) {
            response.put("message", "Error accessing data");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/client")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result){
        Client newClient = null;
        Map<String, String> response = new HashMap<>();
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try{
        newClient = clientService.save(client);
        } catch (DataAccessException e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(newClient, HttpStatus.OK);
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Client clientToUpdate = null;
        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();
            for(FieldError err: result.getFieldErrors()){
                errors.add(err.getField() + ": " + err.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            clientToUpdate = clientService.findById(id);
            clientToUpdate.setEmail(client.getEmail());
            clientToUpdate.setName(client.getName());
            clientToUpdate.setSurname(client.getSurname());
        } catch (DataAccessException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(clientService.save(clientToUpdate), HttpStatus.CREATED);
    }

    @PostMapping("/client/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        if (!file.isEmpty()) {
            Client client = clientService.findById(id);
            String fileName = file.getOriginalFilename();
            String fileExtension = file.getContentType();
            Path currentRelativePath = Paths.get("");
            String currentPath = ((Path) currentRelativePath).toAbsolutePath().toString();
            System.out.println(currentPath);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


}
