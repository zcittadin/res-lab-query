package com.servicos.estatica.resicolor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = -5530691997574499312L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_produto")
	private Long id;
	@Column(name = "codigo")
	private int codigo;
	@Column(name = "lote")
	private int lote;
	@Column(name = "nome_reator")
	private String nomeReator;
	@Column(name = "responsavel")
	private String operador;
	@Column(name = "receita")
	private int receita;
	@Column(name = "responsavel_1")
	private String responsavel1;
	@Column(name = "colaborador_2")
	private String responsavel2;
	@Column(name = "colaborador_3")
	private String responsavel3;
	@Column(name = "dt_inicial")
	private Date dtInicial;
	@Column(name = "dt_final")
	private Date dtFinal;
	@Column(name = "finalizado")
	private String finalizado;
	@Column(name = "quantidade")
	private double quantidade;
	@OneToMany(mappedBy = "produto", targetEntity = Processo.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Processo> processos;

	public Produto(Long id, int codigo, int lote, String nomeReator, String operador, int receita, String responsavel1,
			String responsavel2, String responsavel3, Date dtInicial, Date dtFinal, String finalizado,
			double quantidade, List<Processo> processos) {
		this.id = id;
		this.codigo = codigo;
		this.lote = lote;
		this.nomeReator = nomeReator;
		this.operador = operador;
		this.receita = receita;
		this.responsavel1 = responsavel1;
		this.responsavel2 = responsavel2;
		this.responsavel3 = responsavel3;
		this.dtInicial = dtInicial;
		this.dtFinal = dtFinal;
		this.finalizado = finalizado;
		this.quantidade = quantidade;
		this.processos = processos;
	}

	public Produto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getLote() {
		return lote;
	}

	public void setLote(int lote) {
		this.lote = lote;
	}

	public String getNomeReator() {
		return nomeReator;
	}

	public void setNomeReator(String nomeReator) {
		this.nomeReator = nomeReator;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public int getReceita() {
		return receita;
	}

	public void setReceita(int receita) {
		this.receita = receita;
	}

	public String getResponsavel1() {
		return responsavel1;
	}

	public void setResponsavel1(String responsavel1) {
		this.responsavel1 = responsavel1;
	}

	public String getResponsavel2() {
		return responsavel2;
	}

	public void setResponsavel2(String responsavel2) {
		this.responsavel2 = responsavel2;
	}

	public String getResponsavel3() {
		return responsavel3;
	}

	public void setResponsavel3(String responsavel3) {
		this.responsavel3 = responsavel3;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public String getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(String finalizado) {
		this.finalizado = finalizado;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", codigo=" + codigo + "]";
	}

}
