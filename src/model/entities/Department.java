package model.entities;

import java.io.Serializable;

public class Department implements Serializable{
	
	//SERIALIZABLE -> Objetos tranformados em sequência de bytes
	private static final long serialVersionUID = 1L;
	
	//ATTRIBUTES
	private Integer id;
	private String name;
	
	//CONSTRUCTOR
	public Department() {
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	//GETTERS & SETTERS
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//HASH CODE AND EQUALS -> Comparação dos objetos por conteúdo
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//TO STRING -> Facilidade de imprimir
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
}
