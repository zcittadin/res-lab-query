package com.servicos.estatica.resicolor.util;

import javafx.beans.property.SimpleStringProperty;

public class CurrentScreenProperty {

	private static SimpleStringProperty screenProperty = new SimpleStringProperty();

	public static SimpleStringProperty screenProperty() {
		return screenProperty;
	}

	public static String getScreen() {
		return screenProperty().get();
	}

	public static void setScreen(String screen) {
		screenProperty().set(screen);
	}
}
