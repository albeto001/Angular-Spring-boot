package com.alberto.spring.boot.backend.apirest.models.dao;

import com.alberto.spring.boot.backend.apirest.models.entity.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientDao extends JpaRepository<Client, Long> {}
