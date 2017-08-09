package com.servicos.estatica.resicolor.property;

import eu.hansolo.medusa.LcdDesign;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class StyleClockProperty {

	private static ObjectProperty<LcdDesign> lcdDesign = new SimpleObjectProperty<>();

	public static ObjectProperty<LcdDesign> lcdDesignProperty() {
		return lcdDesign;
	}

	public static LcdDesign getLcdDesign() {
		return lcdDesignProperty().get();
	}

	public final void setLcdDesign(LcdDesign lcdDesign) {
		lcdDesignProperty().set(lcdDesign);
	}

}
