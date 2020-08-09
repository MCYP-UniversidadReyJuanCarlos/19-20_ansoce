package es.urjc.oauth2.resource.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/mensaje")
	public String message() {
		return "Esto es un mensaje para usuarios de lectura";
	}

	@GetMapping("/administrador")
	public String messageAdmin() {
		return "Esto es un mensaje para usuarios de administración";
	}

}
