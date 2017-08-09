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

public class ConfigChartConsultaController implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Button btOk;
	@FXML
	private CheckBox chkMarcadores;
	@FXML
	private CheckBox chkReator;
	@FXML
	private CheckBox chkCaldeira;

	private LineChartConfigContext context;

	public void setContext(LineChartConfigContext context) {
		this.context = context;
		if (context.getCurrentConfig() == null) {
			chkMarcadores.setSelected(false);
			chkReator.setSelected(false);
			chkCaldeira.setSelected(false);
			return;
		}
		
		if (context.getCurrentConfig().getMark()) {
			chkMarcadores.setSelected(true);
		} else {
			chkMarcadores.setSelected(false);
		}
		
		if (context.getCurrentConfig().getReatorA()) {
			chkReator.setSelected(true);
		} else {
			chkReator.setSelected(false);
		}
		
		if (context.getCurrentConfig().getCaldeiraA()) {
			chkCaldeira.setSelected(true);
		} else {
			chkCaldeira.setSelected(false);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void toggleMark() {
		context.getCurrentConfig().setMark(chkMarcadores.isSelected());
	}

	@FXML
	public void toggleReator() {
		context.getCurrentConfig().setReatorA(chkReator.isSelected());
	}

	@FXML
	public void toggleCaldeira() {
		context.getCurrentConfig().setCaldeiraA(chkCaldeira.isSelected());
	}

	@FXML
	public void exit() {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}

}