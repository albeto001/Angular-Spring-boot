package com.alberto.spring.boot.backend.apirest.models.dao;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;

import org.springframework.data.repository.CrudRepository;

public interface IClientDao extends CrudRepository<Client, Long>{}
