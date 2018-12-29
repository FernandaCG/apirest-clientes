package com.ipn.escom.apirest.models.clients.services;

import java.util.List;

import com.ipn.escom.apirest.models.clients.entity.Client;

public interface IClientService {
	
	public List<Client> findAll();
	
	public Client findById(Long Id);
	
	public Client save(Client client);
	
	public void delete(Long id);
}
