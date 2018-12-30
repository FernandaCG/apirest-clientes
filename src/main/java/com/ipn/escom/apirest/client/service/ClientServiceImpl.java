package com.ipn.escom.apirest.client.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipn.escom.apirest.client.entity.Client;
import com.ipn.escom.apirest.client.repository.IClientRepository;

@Service
public class ClientServiceImpl implements IClientService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IClientRepository clientRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) clientRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return clientRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Client save(Client client) {
		return clientRepository.save(client);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clientRepository.deleteById(id);
	}

}
