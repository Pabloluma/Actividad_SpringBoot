package com.pablo.actividad_springboot.Modelo;

import lombok.Data;

import java.util.Objects;

//Modelo
@Data
public class Persona {
	private int id;
	private String nombre;
	private String apellido;
	private String correo;
	private Integer edad;
	private Byte estudia;
	private boolean estudioso;

	public Persona() {
	}
	public Persona(String nombre, String apellido, String correo, Integer edad, Byte estudia) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.edad = edad;
		this.estudia = estudia;
	}

	public Persona(int id, String nombre, String apellido, String correo, Integer edad, Byte estudia) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.edad = edad;
		this.estudia = estudia;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Byte getEstudia() {
		return estudia;
	}

	public void setEstudia(Byte estudia) {
		this.estudia = estudia;
	}
	public boolean isEstudioso() {
		return estudia != null && estudia == 1;
	}

	public void setEstudioso(boolean estudioso) {
		this.estudioso = estudioso;
		this.estudia = (byte) (estudioso ? 1 : 0);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Persona persona = (Persona) o;
		return id == persona.id && Objects.equals(nombre, persona.nombre) && Objects.equals(apellido, persona.apellido) && Objects.equals(correo, persona.correo) && Objects.equals(edad, persona.edad) && Objects.equals(estudia, persona.estudia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, apellido, correo, edad, estudia);
	}
}
