package com.alberto.spring.boot.backend.apirest.controllers;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;
import com.alberto.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
            Client clientToDelete = clientService.findById(id);
            if (this.deleteImageClient(clientToDelete)) {
               response.put("deletions", "There was deleted a previous image: " + clientToDelete.getImage());
            }
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
            if(client == null) {
                response.put("message", "The file could not be uploaded for the provided client");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (this.deleteImageClient(client)) {
                response.put("deletion", "There was deleted a previous image: " + client.getImage() );
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "_");
            String fileExtension = file.getContentType();
            Path uploadsPath = Paths.get("uploads").resolve(fileName).toAbsolutePath();
            try {
                Files.copy(file.getInputStream(), uploadsPath);
            } catch (IOException e) {
                response.put("message", "Error uploading the file " + fileName);
                response.put("error", e.getMessage() + ": " + e.getCause().getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            client.setImage(fileName);
            clientService.save(client);
            response.put("client", client);
        } else {
            response.put("error", "the file input is empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/client/uploads/{image}")
    public ResponseEntity<?> getImage( @PathVariable String image) {
        Map<String, Object> response = new HashMap<>();
        Path filePath = Paths.get("uploads").resolve(image).toAbsolutePath();
        Resource resource = null;
        try{
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        if (!resource.exists() || !resource.isReadable()) {
            response.put("error", "The required file is not available");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image + "\"");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private boolean deleteImageClient(Client client) {
        String image = client.getImage();
        if (image != null && image.length() > 0 ) {
            Path currentImagePath = Paths.get("uploads").resolve(image).toAbsolutePath();
            File currentFile = currentImagePath.toFile();
            if (currentFile.exists() && currentFile.canRead()) {
                return currentFile.delete();
            }
        }
        return false;
    }



}
