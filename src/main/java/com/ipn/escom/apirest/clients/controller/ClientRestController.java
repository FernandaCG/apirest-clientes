package com.ipn.escom.apirest.clients.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.escom.apirest.models.clients.entity.Client;
import com.ipn.escom.apirest.models.clients.services.IClientService;

/**Podria limitar los verbos que pueden ser utilizados*/
@CrossOrigin(origins = ("http://localhost:4200"))
@RestController
@RequestMapping("/api")
public class ClientRestController {
	public static final String MERROR = "error";
	public static final String MMESSAGE = "mensaje";

	@Autowired
	private IClientService clientService;
	
	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();
	}
	
	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Client client=null;
		Map<String,Object> response = new HashMap<>();
		try {
			client = clientService.findById(id);
		} catch(DataAccessException e) {
			response.put(MMESSAGE, "Failed to perform the query in the database");
			response.put(MERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(client == null) {
			response.put(MMESSAGE, "The client's id ".concat(id.toString().concat("  doesn't exist in the database")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(client, HttpStatus.OK);
	}
	
	@PostMapping("/clients")
	public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
		//cliente.setCreateAt(new Date()); para asignar fecha
		Client newClient= null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			/**List<String> errors = new ArrayList<>();
			for( FieldError err: result.getFieldErrors()) {
				errors.add("El campo "+err.getField()+" "+err.getDefaultMessage());
			}*/
			List<String> errors= result.getFieldErrors().stream().map(err -> "The field "+err.getField()+" "+err.getDefaultMessage()).collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newClient = clientService.save(client);
		} catch(DataAccessException e) {
			response.put(MMESSAGE, "Failed to insert into the database");
			response.put(MERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, "The client has been created successfully!");
		response.put("cliente", newClient);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
		Client currentClient = clientService.findById(id);
		Client updatedClient = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors= result.getFieldErrors().stream().map(err -> "The field "+err.getField()+" "+err.getDefaultMessage()).collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if(currentClient == null) {
			response.put(MMESSAGE, "Failed to edit.\nThe client's id".concat(id.toString().concat(" doesn't exist in the database")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		try {
		
		currentClient.setLastName(client.getLastName());
		currentClient.setName(client.getName());
		currentClient.setEmail(client.getEmail());
		updatedClient = clientService.save(currentClient);
		
		} catch(DataAccessException e) {
			response.put(MMESSAGE, "Error updating the client in the database");
			response.put(MERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, "The client has been updated successfully!");
		response.put("cliente", updatedClient);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clients/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		try {
			clientService.delete(id);
		} catch(DataAccessException e) {
			response.put(MMESSAGE, "Error deleting the client from the database");
			response.put(MERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, "The client has been removed successfully!");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
