package com.servicos.estatica.resicolor.app;

import com.servicos.estatica.resicolor.util.HibernateUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Resicolor - Sistema de consultas de processo");
		stage.setMaximized(false);
		stage.setResizable(false);
		stage.getIcons().add(new Image("/com/servicos/estatica/resicolor/style/paintcan.png"));
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		HibernateUtil.closeSessionFactory();
	}

	public static void main(String[] args) {
		launch();
	}

}
