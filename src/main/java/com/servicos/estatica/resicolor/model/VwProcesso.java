package com.servicos.estatica.resicolor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_processo")
public class VwProcesso implements Serializable {

	private static final long serialVersionUID = -892619582801445776L;

	@Id
	private Integer id;
	@Column(name = "dt_processo")
	private Date dtProcesso;
	@Column(name = "t_reator")
	private double tempReator;
	@Column(name = "sp_reator")
	private double spReator;
	@Column(name = "lote")
	private int lote;
	@Column(name = "t_caldeira")
	private double tempCaldeira;
	@Column(name = "sp_caldeira")
	private double spCaldeira;

	public VwProcesso(Date dtProcesso, double tempReator, double spReator, int lote, double tempCaldeira,
			double spCaldeira) {
		this.dtProcesso = dtProcesso;
		this.tempReator = tempReator;
		this.spReator = spReator;
		this.lote = lote;
		this.tempCaldeira = tempCaldeira;
		this.spCaldeira = spCaldeira;
	}

	public VwProcesso() {

	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Date getDtProcesso() {
		return dtProcesso;
	}

	public void setDtProcesso(Date dtProcesso) {
		this.dtProcesso = dtProcesso;
	}

	public double getTempReator() {
		return tempReator;
	}

	public void setTempReator(double tempReator) {
		this.tempReator = tempReator;
	}

	public double getSpReator() {
		return spReator;
	}

	public void setSpReator(double spReator) {
		this.spReator = spReator;
	}

	public double getTempCaldeira() {
		return tempCaldeira;
	}

	public void setTempCaldeira(double tempCaldeira) {
		this.tempCaldeira = tempCaldeira;
	}

	public double getLote() {
		return lote;
	}

	public void setlote(int lote) {
		this.lote = lote;
	}

	public double getSpCaldeira() {
		return spCaldeira;
	}

	public void setSpCaldeira(double spCaldeira) {
		this.spCaldeira = spCaldeira;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtProcesso == null) ? 0 : dtProcesso.hashCode());
		result = prime * result + lote;
		long temp;
		temp = Double.doubleToLongBits(spCaldeira);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(spReator);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(tempCaldeira);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(tempReator);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		VwProcesso other = (VwProcesso) obj;
		if (dtProcesso == null) {
			if (other.dtProcesso != null)
				return false;
		} else if (!dtProcesso.equals(other.dtProcesso))
			return false;
		if (lote != other.lote)
			return false;
		if (Double.doubleToLongBits(spCaldeira) != Double.doubleToLongBits(other.spCaldeira))
			return false;
		if (Double.doubleToLongBits(spReator) != Double.doubleToLongBits(other.spReator))
			return false;
		if (Double.doubleToLongBits(tempCaldeira) != Double.doubleToLongBits(other.tempCaldeira))
			return false;
		if (Double.doubleToLongBits(tempReator) != Double.doubleToLongBits(other.tempReator))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VwProcesso [dtProcesso=" + dtProcesso + ", tempReator=" + tempReator + ", spReator=" + spReator
				+ ", lote=" + lote + ", tempCaldeira=" + tempCaldeira + ", spCaldeira=" + spCaldeira + "]";
	}

}
