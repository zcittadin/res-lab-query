package com.servicos.estatica.resicolor.model;

import java.util.Date;

public class ComparativoReportModel {

	private Date dtProcesso;
	private double tempReatorA;
	private double tempReatorB;
	private double tempCaldeiraA;
	private double tempCaldeiraB;

	public ComparativoReportModel(Date dtProcesso, double tempReatorA, double tempReatorB, double tempCaldeiraA,
			double tempCaldeiraB) {
		this.dtProcesso = dtProcesso;
		this.tempReatorA = tempReatorA;
		this.tempReatorB = tempReatorB;
		this.tempCaldeiraA = tempCaldeiraA;
		this.tempCaldeiraB = tempCaldeiraB;
	}

	public Date getDtProcesso() {
		return dtProcesso;
	}

	public void setDtProcesso(Date dtProcesso) {
		this.dtProcesso = dtProcesso;
	}

	public double getTempReatorA() {
		return tempReatorA;
	}

	public void setTempReatorA(double tempReatorA) {
		this.tempReatorA = tempReatorA;
	}

	public double getTempReatorB() {
		return tempReatorB;
	}

	public void setTempReatorB(double tempReatorB) {
		this.tempReatorB = tempReatorB;
	}

	public double getTempCaldeiraA() {
		return tempCaldeiraA;
	}

	public void setTempCaldeiraA(double tempCaldeiraA) {
		this.tempCaldeiraA = tempCaldeiraA;
	}

	public double getTempCaldeiraB() {
		return tempCaldeiraB;
	}

	public void setTempCaldeiraB(double tempCaldeiraB) {
		this.tempCaldeiraB = tempCaldeiraB;
	}

}