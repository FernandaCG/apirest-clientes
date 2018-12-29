package com.ipn.escom.apirest.models.clients.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ipn.escom.apirest.models.clients.entity.Client;

@Repository
public interface IClientRepository extends CrudRepository<Client, Long>{

}
