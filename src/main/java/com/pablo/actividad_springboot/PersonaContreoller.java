package com.pablo.actividad_springboot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonaContreoller {
	//Capturar datos
	@GetMapping("/abrir")
	public String abrirP(Model model){
		model.addAttribute("estudiante", new Persona());
		return "formulario";
	}

	//Enviar datos
	@PostMapping("/registrar")
	public String registrar(Model model, Persona persona){
		model.addAttribute("rec_nombre", persona.getNombre());
		model.addAttribute("rec_apellido", persona.getApellido());
		model.addAttribute("rec_correo", persona.getCorreo());
		model.addAttribute("rec_edad", persona.getEdad());
		model.addAttribute("rec_estudios", persona.getEdad());
		if(persona.isEstudioso()){
			persona.setEstudia(((byte)1));
			model.addAttribute("rec_estudios", (byte)1);
		}else{
			persona.setEstudia(((byte)0));
			model.addAttribute("rec_estudios", (byte)0);
		}

		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		Transaction tx = null;
		try {
			tx = conexion.beginTransaction();
			Query query = conexion.createQuery("from Persona ");
			List<Persona> listado = query.getResultList();
			ArrayList<String> nombres = new ArrayList<>();
			ArrayList<String> apellidos = new ArrayList<>();
			for (Persona per:listado) {
				nombres.add(per.getNombre());
				apellidos.add(per.getApellido());
			}
			if(nombres.contains(persona.getNombre()) && apellidos.contains(persona.getApellido())){
				return "error";
			}else{
				Persona persona1 = new Persona(persona.getNombre(), persona.getApellido(), persona.getCorreo(),persona.getEdad(), persona.getEstudia());
				conexion.save(persona1);
				tx.commit();
				System.out.println("Se han insertado correctamente todos los departamentos");
			}

		} catch (Exception e) {
			tx.rollback();
			return "error";
		} finally {
			conexion.close();
		}

		return "ok";
	}
	//Enviar datos
	@GetMapping("/altas")
	public String mostrarPersonas(Model model){
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();

		try {
			Query query = conexion.createQuery("from Persona ");
			List<Persona> listado = query.getResultList();
			model.addAttribute("lista", listado);
		} catch (Exception e) {
			System.out.println("Algo no ha ido como debería");
		} finally {
			conexion.close();
		}
		return "persona_alta";
	}


}
