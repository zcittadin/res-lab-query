package com.servicos.estatica.resicolor.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.property.StyleClockProperty;

import eu.hansolo.medusa.LcdDesign;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfigClockStyleController implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Button btOk;
	@FXML
	private RadioButton rdStandardGreen;
	@FXML
	private RadioButton rdBlue;
	@FXML
	private RadioButton rdRed;
	@FXML
	private RadioButton rdBeige;
	@FXML
	private RadioButton rdYellow;
	@FXML
	private RadioButton rdWhite;
	@FXML
	private RadioButton rdGray;
	@FXML
	private RadioButton rdBlack;
	@FXML
	private RadioButton rdGreen;
	@FXML
	private RadioButton rdGreenDarkGreen;
	@FXML
	private RadioButton rdBlue2;
	@FXML
	private RadioButton rdBlueBlack;
	@FXML
	private RadioButton rdBlueDarkBlue;
	@FXML
	private RadioButton rdBlueLightBlue;
	@FXML
	private RadioButton rdBlueGray;
	@FXML
	private RadioButton rdStandard;
	@FXML
	private RadioButton rdLightGreen;
	@FXML
	private RadioButton rdBlueBlue;
	@FXML
	private RadioButton rdRedDarkRed;
	@FXML
	private RadioButton rdDarkBlue;
	@FXML
	private RadioButton rdPurple;
	@FXML
	private RadioButton rdBlackRed;
	@FXML
	private RadioButton rdDarkGreen;
	@FXML
	private RadioButton rdAmber;
	@FXML
	private RadioButton rdLightBlue;
	@FXML
	private RadioButton rdGreenBlack;
	@FXML
	private RadioButton rdYellowBlack;
	@FXML
	private RadioButton rdBlackYellow;
	@FXML
	private RadioButton rdLightGreenBlack;
	@FXML
	private RadioButton rdDarkPurple;
	@FXML
	private RadioButton rdDarkAmber;
	@FXML
	private RadioButton rdBlueLightBlue2;
	@FXML
	private RadioButton rdGrayPurple;
	@FXML
	private RadioButton rdYoctopuce;
	@FXML
	private RadioButton rdSections;
	@FXML
	private RadioButton rdFlatCustom;
	@FXML
	private RadioButton rdOrange;

	ToggleGroup group = new ToggleGroup();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainPane.setPrefSize(680, 440);
		initGroup();
		initSelection();
	}

	@FXML
	public void setStdGreen() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.STANDARD_GREEN);
	}

	@FXML
	public void setBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE);
	}

	@FXML
	public void setRed() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.RED);
	}

	@FXML
	public void setBeige() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BEIGE);
	}

	@FXML
	public void setYellow() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.YELLOW);
	}

	@FXML
	public void setWhite() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.WHITE);
	}

	@FXML
	public void setGray() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.GRAY);
	}

	@FXML
	public void setBlack() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLACK);
	}

	@FXML
	public void setGreen() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.GREEN);
	}

	@FXML
	public void setGreenDarkGreen() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.GREEN_DARKGREEN);
	}

	@FXML
	public void setBlue2() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE2);
	}

	@FXML
	public void setBlueBlack() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_BLACK);
	}

	@FXML
	public void setBlueDarkBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_DARKBLUE);
	}

	@FXML
	public void setBlueLightBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_LIGHTBLUE);
	}

	@FXML
	public void setBlueGray() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_GRAY);
	}

	@FXML
	public void setStandard() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.STANDARD);
	}

	@FXML
	public void setLightGreen() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.LIGHTGREEN);
	}

	@FXML
	public void setBlueBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_BLUE);
	}

	@FXML
	public void setRedDarkRed() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.RED_DARKRED);
	}

	@FXML
	public void setDarkBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.DARKBLUE);
	}

	@FXML
	public void setPurple() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.PURPLE);
	}

	@FXML
	public void setBlackRed() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLACK_RED);
	}

	@FXML
	public void setDarkGreen() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.DARKGREEN);
	}

	@FXML
	public void setAmber() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.AMBER);
	}

	@FXML
	public void setLightBlue() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.LIGHTBLUE);
	}

	@FXML
	public void setGreenBlack() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.GREEN_BLACK);
	}

	@FXML
	public void setYellowBlack() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.YELLOW_BLACK);
	}

	@FXML
	public void setBlackYellow() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLACK_YELLOW);
	}

	@FXML
	public void setLightGreenBlack() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.LIGHTGREEN_BLACK);
	}

	@FXML
	public void setDarkPurple() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.DARKPURPLE);
	}

	@FXML
	public void setDarkAmber() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.DARKAMBER);
	}

	@FXML
	public void setBlueLightBlue2() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.BLUE_LIGHTBLUE2);
	}

	@FXML
	public void setGrayPurple() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.GRAY_PURPLE);
	}

	@FXML
	public void setYoctopuce() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.YOCTOPUCE);
	}

	@FXML
	public void setSections() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.SECTIONS);
	}

	@FXML
	public void setFlatCustom() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.FLAT_CUSTOM);
	}

	@FXML
	public void setOrange() {
		StyleClockProperty.lcdDesignProperty().set(LcdDesign.ORANGE);
	}

	@FXML
	public void exit() {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}

	private void initSelection() {

		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.AMBER) {
			rdAmber.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BEIGE) {
			rdBeige.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLACK) {
			rdBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLACK_RED) {
			rdBlackRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLACK_YELLOW) {
			rdBlackYellow.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE) {
			rdBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE2) {
			rdBlue2.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_BLACK) {
			rdBlueBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_BLUE) {
			rdBlueBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_DARKBLUE) {
			rdBlueDarkBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_GRAY) {
			rdBlueGray.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_LIGHTBLUE) {
			rdBlueLightBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.BLUE_LIGHTBLUE2) {
			rdBlueLightBlue2.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.DARKAMBER) {
			rdDarkAmber.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.DARKBLUE) {
			rdDarkBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.DARKGREEN) {
			rdDarkGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.DARKPURPLE) {
			rdDarkPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.FLAT_CUSTOM) {
			rdFlatCustom.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.GRAY) {
			rdGray.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.GRAY_PURPLE) {
			rdGrayPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.GREEN) {
			rdGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.GREEN_BLACK) {
			rdGreenBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.GREEN_DARKGREEN) {
			rdGreenDarkGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.LIGHTBLUE) {
			rdLightBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.LIGHTGREEN) {
			rdLightGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.LIGHTGREEN_BLACK) {
			rdLightGreenBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.ORANGE) {
			rdOrange.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.PURPLE) {
			rdPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.RED) {
			rdRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.RED_DARKRED) {
			rdRedDarkRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.SECTIONS) {
			rdSections.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.STANDARD) {
			rdStandard.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.STANDARD_GREEN) {
			rdStandardGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.WHITE) {
			rdWhite.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.YELLOW) {
			rdYellow.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.YELLOW_BLACK) {
			rdYellowBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockProperty.lcdDesignProperty().get() == LcdDesign.YOCTOPUCE) {
			rdYoctopuce.setSelected(Boolean.TRUE);
		}
	}

	private void initGroup() {
		rdStandardGreen.setToggleGroup(group);
		rdBlue.setToggleGroup(group);
		rdRed.setToggleGroup(group);
		rdAmber.setToggleGroup(group);
		rdBeige.setToggleGroup(group);
		rdBlack.setToggleGroup(group);
		rdBlackRed.setToggleGroup(group);
		rdBlackYellow.setToggleGroup(group);
		rdBlue2.setToggleGroup(group);
		rdBlueBlack.setToggleGroup(group);
		rdBlueBlue.setToggleGroup(group);
		rdBlueDarkBlue.setToggleGroup(group);
		rdBlueGray.setToggleGroup(group);
		rdBlueLightBlue.setToggleGroup(group);
		rdBlueLightBlue2.setToggleGroup(group);
		rdDarkAmber.setToggleGroup(group);
		rdDarkBlue.setToggleGroup(group);
		rdDarkGreen.setToggleGroup(group);
		rdDarkPurple.setToggleGroup(group);
		rdFlatCustom.setToggleGroup(group);
		rdGray.setToggleGroup(group);
		rdGrayPurple.setToggleGroup(group);
		rdGreen.setToggleGroup(group);
		rdGreenBlack.setToggleGroup(group);
		rdGreenDarkGreen.setToggleGroup(group);
		rdLightBlue.setToggleGroup(group);
		rdLightGreen.setToggleGroup(group);
		rdLightGreenBlack.setToggleGroup(group);
		rdOrange.setToggleGroup(group);
		rdPurple.setToggleGroup(group);
		rdRedDarkRed.setToggleGroup(group);
		rdSections.setToggleGroup(group);
		rdStandard.setToggleGroup(group);
		rdWhite.setToggleGroup(group);
		rdYellow.setToggleGroup(group);
		rdYellowBlack.setToggleGroup(group);
		rdYoctopuce.setToggleGroup(group);
	}

}
