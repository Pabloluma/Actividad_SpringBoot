package com.pablo.actividad_springboot.Controlador;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Controlador {
	@GetMapping("/")
	public String saludoPers() {
		return "index";
	}
}
