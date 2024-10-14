package com.pablo.actividad_springboot.Controlador;

import com.pablo.actividad_springboot.Modelo.Persona;
import com.pablo.actividad_springboot.SessionFactoryUtil;
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
public class PersonaController {
	//Capturar datos
	@GetMapping("/abrir")
	public String abrirP(Model model){
		model.addAttribute("estudiante", new Persona());
		return "formulario";
	}

	//
	@PostMapping("/registrar")
	public String registrar(Model model, Persona persona){
//		Mostramos los datos una vez guardados en la base de datos
		model.addAttribute("rec_nombre", persona.getNombre());
		model.addAttribute("rec_apellido", persona.getApellido());
		model.addAttribute("rec_correo", persona.getCorreo());
		model.addAttribute("rec_edad", persona.getEdad());
		if(persona.isEstudioso()){
			persona.setEstudia(((byte)1));
			model.addAttribute("rec_estudios", (byte)1);
		}else{
			persona.setEstudia(((byte)0));
			model.addAttribute("rec_estudios", (byte)0);
		}
//		Guardamos en la base de datos
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		Transaction tx = null;
		try {
//			Abrimos la conexion
			tx = conexion.beginTransaction();
//			Recuperamos los datos de todas las personas
			Query query = conexion.createQuery("from Persona ");
			List<Persona> listado = query.getResultList();
			ArrayList<String> nombres = new ArrayList<>();
			ArrayList<String> apellidos = new ArrayList<>();
			for (Persona per:listado) {
				nombres.add(per.getNombre());
				apellidos.add(per.getApellido());
			}
//			Filtramos para que no haya nombres y apellidos repetidos
			if(nombres.contains(persona.getNombre()) && apellidos.contains(persona.getApellido())){
				return "error_reg";
			}else{
//				Creamos un objeto y lo guardamos en la base de datos
				Persona persona1 = new Persona(persona.getNombre(), persona.getApellido(), persona.getCorreo(),persona.getEdad(), persona.getEstudia());
				conexion.save(persona1);
				tx.commit();
				System.out.println("Se han insertado correctamente todos los departamentos");
			}
		} catch (Exception e) {
			tx.rollback();
			return "error_reg";
		} finally {
			conexion.close();
		}
		return "guarda_Ok";
	}

	//Obtenemos los datos de todos los registros de la tabla para mostrarlos
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

//	Obtenemos los datos de la BBDD para mostrarlos en el combobox
	@GetMapping("/borrar")
	public String borrar(Model model){
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		ArrayList<String> combo = new ArrayList<>();

		try {
			Query query = conexion.createQuery("from Persona ");
			List<Persona> listaCombo = query.getResultList();
			for (Persona i:listaCombo ) {
				combo.add(i.getId() + " " + i.getNombre());
			}
//			Pasamos la lista de lo que va a aparecer en el combobox
			model.addAttribute("listaCombo", listaCombo);
		} catch (Exception e) {
			System.out.println("Algo no ha ido como debería");
		} finally {
			conexion.close();
		}
		return "borrar";
	}

//	Hacemos efectivo el borrado
	@PostMapping("/eliminado")
	public String eliminarPersonas(@RequestParam("idPersona") Long idPersona, Model model) {
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		Transaction tx = null;

		try {
//			Obtenemos el objeto Persona
			tx = conexion.beginTransaction();
			Persona persona = conexion.get(Persona.class, idPersona);
			if (persona != null) {
//				Obtenemos el nombre y el apellido para mostrarlo en la pantalla de verificacion una vez eliminado
				String nombrePersona = persona.getNombre();
				String apellidoPersona = persona.getApellido();
				conexion.remove(persona);
				tx.commit();
				model.addAttribute("nombre_persona", nombrePersona);
				model.addAttribute("apellido_persona", apellidoPersona);
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				return "elim_error";
			}
			e.printStackTrace();
			return "elim_error";
		} finally {
			conexion.close();
		}
		return "elim_Ok";
	}



//	Obtenemos los datos de la persona a actualizar
	@PostMapping("/editar")
	public String editarPersona(@RequestParam("idPersona") Long idPersona, Model model) {
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		Persona persona = null;

		try {
//			Obtenemos los datos de la persona con el id
			persona = conexion.get(Persona.class, idPersona);  // Cargar los datos de la persona
			if (persona != null) {
				model.addAttribute("persona", persona);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conexion.close();
		}

		return "formUpdate";
	}

	@PostMapping("/actualizar")
	public String actualizarPersona(@ModelAttribute("persona") Persona persona, Model model) {
		SessionFactory sesionActual = SessionFactoryUtil.getSessionFactory();
		Session conexion = sesionActual.openSession();
		Transaction tx = null;

//		Obtenmos los datos a actualizar
		try {
			tx = conexion.beginTransaction();
			Persona personaExistente = conexion.get(Persona.class, persona.getId());

			if (personaExistente != null) {
				personaExistente.setNombre(persona.getNombre());
				personaExistente.setApellido(persona.getApellido());
				personaExistente.setCorreo(persona.getCorreo());
				personaExistente.setEdad(persona.getEdad());
				personaExistente.setEstudioso(persona.isEstudioso());
//				Actualizamos con los nuevos datos
				conexion.update(personaExistente);
				tx.commit();

//
				model.addAttribute("rec_nombre", persona.getNombre());
				model.addAttribute("rec_apellido", persona.getApellido());
				model.addAttribute("rec_correo", persona.getCorreo());
				model.addAttribute("rec_edad", persona.getEdad());
				model.addAttribute("rec_estudios", personaExistente.isEstudioso() ? "Sí" : "No");
			}

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				return "error_update";
			}
			e.printStackTrace();
		} finally {
			conexion.close();
		}

		return "guarda_Ok";
	}

}
