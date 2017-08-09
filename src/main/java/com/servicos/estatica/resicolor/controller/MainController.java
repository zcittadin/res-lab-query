package com.servicos.estatica.resicolor.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.property.StyleClockProperty;
import com.servicos.estatica.resicolor.util.CurrentScreenProperty;

import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.LcdDesign;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class MainController implements Initializable {

	public static String screenConsultaID = "consulta";
	public static String screenConsultaFile = "/com/servicos/estatica/resicolor/app/Consulta.fxml";
	public static String screenComparaID = "compara";
	public static String screenComparaFile = "/com/servicos/estatica/resicolor/app/Comparacoes.fxml";
	public static String screenInicioID = "inicio";
	public static String screenInicioFile = "/com/servicos/estatica/resicolor/app/PaginaInicial.fxml";

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Pane centralPane;
	@FXML
	private ImageView imgCliente;
	@FXML
	private ImageView imgExit;
	@FXML
	private Button btStyleClock;
	@FXML
	private Clock clock;

	private static ImageViewResizer imgClienteResizer;
	private static ImageViewResizer imgExitResizer;
	private static Timeline tmlBtClockGrow = new Timeline();
	private static Timeline tmlBtClockShrink = new Timeline();

	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		StyleClockProperty.lcdDesignProperty().addListener(new ChangeListener<LcdDesign>() {
			@Override
			public void changed(ObservableValue<? extends LcdDesign> observable, LcdDesign oldValue,
					LcdDesign newValue) {
				clock.setLcdDesign(newValue);
			}
		});

		imgCliente.setImage(new Image("/com/servicos/estatica/resicolor/style/resicolor.png"));
		imgClienteResizer = new ImageViewResizer(imgCliente, 126, 70);
		imgClienteResizer.setLayoutX(16);
		imgClienteResizer.setLayoutY(6);
		imgExitResizer = new ImageViewResizer(imgExit, 70, 71);
		imgExitResizer.setLayoutX(50);
		imgExitResizer.setLayoutY(633);
		mainPane.getChildren().addAll(imgClienteResizer, imgExitResizer);

		tmlBtClockGrow.getKeyFrames()
				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), -105)));
		tmlBtClockShrink.getKeyFrames()
				.addAll(new KeyFrame(Duration.seconds(0.3), new KeyValue(btStyleClock.translateXProperty(), 0)));

		mainContainer.loadScreen(screenInicioID, screenInicioFile);
		mainContainer.loadScreen(screenConsultaID, screenConsultaFile);
		mainContainer.loadScreen(screenComparaID, screenComparaFile);
		CurrentScreenProperty.setScreen(screenInicioID);

		mainContainer.setScreen(screenInicioID);
		centralPane.getChildren().addAll(mainContainer);
	}

	@FXML
	private void openInicial() {
		mainContainer.setScreen(screenInicioID);
	}

	@FXML
	private void openConsultas() {
		mainContainer.setScreen(screenConsultaID);
	}

	@FXML
	private void openCompara() {
		mainContainer.setScreen(screenComparaID);
	}

	@FXML
	private void handleImgClienteAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/app/ClienteInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o cliente");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgCliente.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	public void openStyleOptions() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/app/ConfigClockStyle.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Estilo do relógio");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgExit.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	private void hoverBtClock() {
		tmlBtClockGrow.play();
	}

	@FXML
	private void unhoverBtClock() {
		tmlBtClockShrink.play();
	}

	@FXML
	private void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente sair do sistema?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

}
