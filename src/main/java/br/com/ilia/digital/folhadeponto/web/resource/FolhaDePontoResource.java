package br.com.ilia.digital.folhadeponto.web.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.service.FolhaDePontoService;
import br.com.ilia.digital.folhadeponto.service.exception.FolhaDePontoException;

@RestController
@RequestMapping("/v1")
public class FolhaDePontoResource {

	@Autowired
	private FolhaDePontoService folhaDePontoService;
	
	@RequestMapping(value="/batidas", method=RequestMethod.POST)
	private ResponseEntity<Registro> registrarBatida(@RequestBody Momento momento) throws FolhaDePontoException {
		
		return ResponseEntity.status(HttpStatus.CREATED).location(getLocation(momento.getId())).body(folhaDePontoService.registraBatida(momento));
	}
	
	@RequestMapping(value="/registros", method=RequestMethod.GET)
	private ResponseEntity<List<Registro>> registros() {
		
		return ResponseEntity.status(HttpStatus.OK).location(getLocation("1")).body(folhaDePontoService.getRegistros());
	}
	
	@RequestMapping(value="/momentos", method=RequestMethod.GET)
	private ResponseEntity<List<Momento>> getMomentos() {
		
		return ResponseEntity.status(HttpStatus.OK).location(getLocation("1")).body(folhaDePontoService.getMomentos());
	}
	
	/*
	 * Retorna a URI location
	 */
	private URI getLocation(Object obj) {
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(obj)
				.toUri();
	}
}
