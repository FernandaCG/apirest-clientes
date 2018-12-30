package com.ipn.escom.apirest.client.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ipn.escom.apirest.client.entity.Client;

@Repository
public interface IClientRepository extends CrudRepository<Client, Long>{

}
