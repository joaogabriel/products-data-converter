package com.qualmercado.importer.external.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Exportacao")
public class Exportacao {

	@XStreamImplicit(itemFieldName = "Registros")
	private List<Registros> Registros;
	
	public Exportacao() {
		super();
	}
	
	public Exportacao(List<Registros> registros) {
		super();
		this.Registros = registros;
	}

	public List<Registros> getRegistros() {
		return Registros;
	}
	
	public void setRegistros(List<Registros> registros) {
		this.Registros = registros;
	}
	
}
