package com.qualmercado.importer.external.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Registros implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CODIGO")
	private String codigo;
	
	@XStreamAlias("BARRAS")
	private String barras;
	
	@XStreamAlias("NOME")
	private String nome;
	
	@XStreamAlias("DESCRICAO")
	private String descricao;
	
	@XStreamAlias("PRECO_CONSUMIDOR")
	private String preco_consumidor;
	
	@XStreamAlias("UND_V")
	private String unidade;

	public Registros(String codigo, String barras, String nome, String descricao, String preco_consumidor) {
		super();
		this.codigo = codigo;
		this.barras = barras;
		this.nome = nome;
		this.descricao = descricao;
		this.preco_consumidor = preco_consumidor;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getBarras() {
		return barras;
	}

	public void setBarras(String barras) {
		this.barras = barras;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPreco_consumidor() {
		return preco_consumidor;
	}

	public void setPreco_consumidor(String preco_consumidor) {
		this.preco_consumidor = preco_consumidor;
	}

	public String getUnidade() {
		return unidade;
	}
	
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	
}
