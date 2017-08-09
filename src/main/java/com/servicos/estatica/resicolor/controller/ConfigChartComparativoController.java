package com.servicos.estatica.resicolor.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.config.LineChartConfigContext;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfigChartComparativoController implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Button btOk;
	@FXML
	private CheckBox chkMarcadores;
	@FXML
	private CheckBox chkReatorA;
	@FXML
	private CheckBox chkCaldeiraA;
	@FXML
	private CheckBox chkReatorB;
	@FXML
	private CheckBox chkCaldeiraB;

	private LineChartConfigContext lineChartConfigContext;

	public void setContext(LineChartConfigContext lineChartConfigContext) {
		this.lineChartConfigContext = lineChartConfigContext;
		if (lineChartConfigContext.getCurrentConfig() == null) {
			chkMarcadores.setSelected(false);
			chkReatorA.setSelected(false);
			chkCaldeiraA.setSelected(false);
			return;
		}

		if (lineChartConfigContext.getCurrentConfig().getMark()) {
			chkMarcadores.setSelected(true);
		} else {
			chkMarcadores.setSelected(false);
		}

		if (lineChartConfigContext.getCurrentConfig().getReatorA()) {
			chkReatorA.setSelected(true);
		} else {
			chkReatorA.setSelected(false);
		}

		if (lineChartConfigContext.getCurrentConfig().getCaldeiraA()) {
			chkCaldeiraA.setSelected(true);
		} else {
			chkCaldeiraA.setSelected(false);
		}

		if (lineChartConfigContext.getCurrentConfig().getReatorB()) {
			chkReatorB.setSelected(true);
		} else {
			chkReatorB.setSelected(false);
		}

		if (lineChartConfigContext.getCurrentConfig().getCaldeiraB()) {
			chkCaldeiraB.setSelected(true);
		} else {
			chkCaldeiraB.setSelected(false);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void toggleMark() {
		lineChartConfigContext.getCurrentConfig().setMark(chkMarcadores.isSelected());
	}

	@FXML
	public void toggleReatorA() {
		lineChartConfigContext.getCurrentConfig().setReatorA(chkReatorA.isSelected());
	}

	@FXML
	public void toggleCaldeiraA() {
		lineChartConfigContext.getCurrentConfig().setCaldeiraA(chkCaldeiraA.isSelected());
	}

	@FXML
	public void toggleReatorB() {
		lineChartConfigContext.getCurrentConfig().setReatorB(chkReatorB.isSelected());
	}

	@FXML
	public void toggleCaldeiraB() {
		lineChartConfigContext.getCurrentConfig().setCaldeiraB(chkCaldeiraB.isSelected());
	}

	@FXML
	public void exit() {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}

}
