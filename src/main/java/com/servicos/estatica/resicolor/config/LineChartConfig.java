package com.servicos.estatica.resicolor.config;

import javafx.beans.property.SimpleBooleanProperty;

public class LineChartConfig {

	private SimpleBooleanProperty markProperty = new SimpleBooleanProperty();
	private SimpleBooleanProperty reatorAProperty = new SimpleBooleanProperty();
	private SimpleBooleanProperty caldeiraAProperty = new SimpleBooleanProperty();
	private SimpleBooleanProperty reatorBProperty = new SimpleBooleanProperty();
	private SimpleBooleanProperty caldeiraBProperty = new SimpleBooleanProperty();

	public SimpleBooleanProperty markProperty() {
		return markProperty;
	}

	public Boolean getMark() {
		return markProperty().get();
	}

	public void setMark(Boolean mark) {
		markProperty().set(mark);
	}

	public SimpleBooleanProperty reatorAProperty() {
		return reatorAProperty;
	}

	public Boolean getReatorA() {
		return reatorAProperty().get();
	}

	public void setReatorA(Boolean reatorA) {
		reatorAProperty().set(reatorA);
	}

	public SimpleBooleanProperty caldeiraAProperty() {
		return caldeiraAProperty;
	}

	public Boolean getCaldeiraA() {
		return caldeiraAProperty().get();
	}

	public void setCaldeiraA(Boolean caldeiraA) {
		caldeiraAProperty().set(caldeiraA);
	}

	public SimpleBooleanProperty reatorBProperty() {
		return reatorBProperty;
	}

	public Boolean getReatorB() {
		return reatorBProperty().get();
	}

	public void setReatorB(Boolean reatorB) {
		reatorBProperty().set(reatorB);
	}

	public SimpleBooleanProperty caldeiraBProperty() {
		return caldeiraBProperty;
	}

	public Boolean getCaldeiraB() {
		return caldeiraBProperty().get();
	}

	public void setCaldeiraB(Boolean caldeiraB) {
		caldeiraBProperty().set(caldeiraB);
	}

}
