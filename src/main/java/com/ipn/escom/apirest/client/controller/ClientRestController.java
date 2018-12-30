package com.ipn.escom.apirest.client.controller;

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

import com.ipn.escom.apirest.client.entity.Client;
import com.ipn.escom.apirest.client.service.IClientService;

/** I could limit the verbs that can be used */
@CrossOrigin(origins = ("http://localhost:4200"))
@RestController
@RequestMapping("/api/v1")
public class ClientRestController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ERROR = "error";
	public static final String MMESSAGE = "mensaje";
	public static final String ENTITY = "client";
	public static final String QUERY_ERROR = "Failed to perform the query in the database";
	public static final String SUCCESSFUL_QUERY = "The query has been executed successful";
	
	Map<String, Object> response = new HashMap<>();

	@Autowired
	private IClientService clientService;

	@GetMapping("/client")
	public ResponseEntity<?> index() {
		response = new HashMap<>();
		List<Client> clients = null;
		try {
			clients = clientService.findAll();
		} catch (DataAccessException e) {
			response.put(MMESSAGE, QUERY_ERROR);
			response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (clients.isEmpty()) {
			response.put(MMESSAGE, "There aren't registered clients");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	@GetMapping("/client/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		response = new HashMap<>();
		Client client = null;
		try {
			client = clientService.findById(id);
		} catch (DataAccessException e) {
			response.put(MMESSAGE, QUERY_ERROR);
			response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (client == null) {
			response.put(MMESSAGE, "The client's id ".concat(id.toString().concat("  doesn't exist in the database")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	@PostMapping("/client")
	public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
		response = new HashMap<>();
		Client clientExample = null;
		if (result.hasErrors()) {
			/**
			 * List<String> errors = new ArrayList<>(); for( FieldError err:
			 * result.getFieldErrors()) { errors.add("The field "+err.getField()+"
			 * "+err.getDefaultMessage()); }
			 */
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "The field " + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			clientExample = clientService.save(client);
		} catch (DataAccessException e) {
			response.put(MMESSAGE, QUERY_ERROR);
			response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, SUCCESSFUL_QUERY);
		response.put(ENTITY, clientExample);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/client/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
		response = new HashMap<>();
		Client currentClient = clientService.findById(id);
		Client updatedClient = null;
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "The field " + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentClient == null) {
			response.put(MMESSAGE,
					"Failed to edit.\nThe client's id".concat(id.toString().concat(" doesn't exist in the database")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		try {

			currentClient.setLastName(client.getLastName());
			currentClient.setName(client.getName());
			currentClient.setEmail(client.getEmail());
			updatedClient = clientService.save(currentClient);

		} catch (DataAccessException e) {
			response.put(MMESSAGE, QUERY_ERROR);
			response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, SUCCESSFUL_QUERY);
		response.put(ENTITY, updatedClient);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/client/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		response = new HashMap<>();
		try {
			clientService.delete(id);
		} catch (DataAccessException e) {
			response.put(MMESSAGE, QUERY_ERROR);
			response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(MMESSAGE, SUCCESSFUL_QUERY);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
