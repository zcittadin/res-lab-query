package com.servicos.estatica.resicolor.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.app.ControlledScreen;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.service.LoteDBService;
import com.servicos.estatica.resicolor.service.RegistroDBService;
import com.servicos.estatica.resicolor.service.impl.LoteDBServiceImpl;
import com.servicos.estatica.resicolor.service.impl.RegistroDBServiceImpl;
import com.servicos.estatica.resicolor.util.PeriodFormatter;
import com.servicos.estatica.resicolor.util.ProducaoUtil;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

@SuppressWarnings("rawtypes")
public class InicialController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Button btAtualizar;
	@FXML
	private Button btCancelar;
	@FXML
	private TableView tbProcessos;
	@FXML
	private TableView tbRecentes;
	@FXML
	private TableColumn colLote;
	@FXML
	private TableColumn colCodigo;
	@FXML
	private TableColumn colReator;
	@FXML
	private TableColumn colData;
	@FXML
	private TableColumn colInicio;
	@FXML
	private TableColumn colQuantidade;
	@FXML
	private TableColumn colOperadores;
	@FXML
	private TableColumn colLoteRecente;
	@FXML
	private TableColumn colCodigoRecente;
	@FXML
	private TableColumn colReatorRecente;
	@FXML
	private TableColumn colDataRecente;
	@FXML
	private TableColumn colInicioRecente;
	@FXML
	private TableColumn colFinalRecente;
	@FXML
	private TableColumn colTempoRecente;
	@FXML
	private TableColumn colQuantidadeRecente;
	@FXML
	private TableColumn colProducaoRecente;
	@FXML
	private TableColumn colOperadoresRecente;
	@FXML
	private ProgressIndicator progressProcessos;
	@FXML
	private ProgressIndicator progressRecentes;
	@FXML
	private ImageView imgEstatica;

	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; ";
	private Tooltip TOOLTIP_REFRESH = new Tooltip("Atualizar informações");
	private Tooltip TOOLTIP_CANCEL = new Tooltip("Finalizar lotes pendentes");

	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer imgResizer;

	private Produto produto;

	private Date dataFinal = null;

	private String tempo = null;
	private String quantidade = null;

	private static ObservableList<Produto> produtos = FXCollections.observableArrayList();
	private static ObservableList<Produto> produtosRecentes = FXCollections.observableArrayList();
	private static LoteDBService loteService = new LoteDBServiceImpl();
	private static RegistroDBService registroService = new RegistroDBServiceImpl();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);
		imgEstatica.setImage(new Image("/com/servicos/estatica/resicolor/style/logotipo.png"));
		imgResizer = new ImageViewResizer(imgEstatica, 138, 42);
		imgResizer.setLayoutX(150.0);
		imgResizer.setLayoutY(150.0);
		imgResizer.setLayoutX(1083);
		imgResizer.setLayoutY(607);
		mainPane.getChildren().addAll(imgResizer);
		TOOLTIP_REFRESH.setStyle(TOOLTIP_CSS);
		TOOLTIP_CANCEL.setStyle(TOOLTIP_CSS);
		Tooltip.install(btAtualizar, TOOLTIP_REFRESH);
		Tooltip.install(btCancelar, TOOLTIP_CANCEL);
		atualizar();
	}

	@FXML
	private void atualizar() {
		atualizarAndamento();
		atualizarRecentes();
	}

	private void atualizarAndamento() {
		tbProcessos.getItems().clear();
		progressProcessos.setVisible(true);
		tbProcessos.setDisable(true);
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				produtos = FXCollections.observableList((List<Produto>) loteService.findEmAndamento());
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (!produtos.isEmpty()) {
					populateTableAndamento();
				}
				progressProcessos.setVisible(false);
				tbProcessos.setDisable(false);
			}
		});
		Thread t = new Thread(searchTask);
		t.start();
	}

	private void atualizarRecentes() {
		tbRecentes.getItems().clear();
		progressRecentes.setVisible(true);
		tbRecentes.setDisable(true);
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				produtosRecentes = FXCollections.observableList((List<Produto>) loteService.findRecentes());
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (!produtosRecentes.isEmpty()) {
					populateTableRecentes();
				}
				progressRecentes.setVisible(false);
				tbRecentes.setDisable(false);
			}
		});
		Thread t = new Thread(searchTask);
		t.start();

	}

	@SuppressWarnings("unchecked")
	private void populateTableRecentes() {

		tbRecentes.setItems(produtosRecentes);
		colLoteRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Produto, Integer> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								p.getLote());
						return simpleObject;
					}
				});
		colCodigoRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Produto, Integer> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								p.getCodigo());
						return simpleObject;
					}
				});
		colReatorRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getNomeReator());
						return simpleObject;
					}
				});
		colDataRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDtInicial() == null ? "Sem registro"
										: new SimpleDateFormat("dd/MM/yyyy").format(p.getDtInicial()));
						return simpleObject;
					}
				});
		colInicioRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDtInicial() == null ? "Sem registro"
										: new SimpleDateFormat("HH:mm:ss").format(p.getDtInicial()));
						return simpleObject;
					}
				});
		colFinalRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDtFinal() == null ? "Sem registro"
										: new SimpleDateFormat("HH:mm:ss").format(p.getDtFinal()));
						return simpleObject;
					}
				});
		colTempoRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDtInicial() == null || p.getDtFinal() == null ? "Sem registro"
										: PeriodFormatter.formatPeriod(p.getDtInicial(), p.getDtFinal()));
						if (p.getDtInicial() != null && p.getDtFinal() != null)
							tempo = PeriodFormatter.formatPeriod(p.getDtInicial(), p.getDtFinal());
						return simpleObject;
					}
				});
		colQuantidadeRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Produto, Double> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								p.getQuantidade());
						quantidade = String.valueOf(p.getQuantidade());
						return simpleObject;
					}
				});
		colProducaoRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								ProducaoUtil.calculaProducao(tempo, quantidade));
						return simpleObject;
					}
				});
		colOperadoresRecente.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								(p.getOperador()) + (p.getResponsavel1() != null ? "/" + p.getResponsavel1() : ""));
						return simpleObject;
					}
				});
		colLoteRecente.setStyle("-fx-alignment: CENTER;");
		colCodigoRecente.setStyle("-fx-alignment: CENTER;");
		colReatorRecente.setStyle("-fx-alignment: CENTER;");
		colDataRecente.setStyle("-fx-alignment: CENTER;");
		colInicioRecente.setStyle("-fx-alignment: CENTER;");
		colFinalRecente.setStyle("-fx-alignment: CENTER;");
		colTempoRecente.setStyle("-fx-alignment: CENTER;");
		colQuantidadeRecente.setStyle("-fx-alignment: CENTER;");
		colProducaoRecente.setStyle("-fx-alignment: CENTER;");
		colOperadoresRecente.setStyle("-fx-alignment: CENTER;");
		tbRecentes.getColumns().setAll(colLoteRecente, colCodigoRecente, colReatorRecente, colDataRecente,
				colInicioRecente, colFinalRecente, colTempoRecente, colQuantidadeRecente, colProducaoRecente,
				colOperadoresRecente);

	}

	@SuppressWarnings("unchecked")
	private void populateTableAndamento() {
		tbProcessos.setItems(produtos);
		colLote.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Produto, Integer> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								p.getLote());
						return simpleObject;
					}
				});
		colCodigo.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Produto, Integer> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								p.getCodigo());
						return simpleObject;
					}
				});
		colReator.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getNomeReator());
						return simpleObject;
					}
				});
		colData.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								new SimpleDateFormat("dd/MM/yyyy").format(p.getDtInicial()));
						return simpleObject;
					}
				});
		colInicio.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								new SimpleDateFormat("HH:mm:ss").format(p.getDtInicial()));
						return simpleObject;
					}
				});
		colQuantidade.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Produto, Double> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								p.getQuantidade());
						return simpleObject;
					}
				});
		colOperadores.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Produto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Produto, String> cell) {
						final Produto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								(p.getOperador()) + (p.getResponsavel1() != null ? "/" + p.getResponsavel1() : ""));
						return simpleObject;
					}
				});
		colLote.setStyle("-fx-alignment: CENTER;");
		colCodigo.setStyle("-fx-alignment: CENTER;");
		colReator.setStyle("-fx-alignment: CENTER;");
		colData.setStyle("-fx-alignment: CENTER;");
		colInicio.setStyle("-fx-alignment: CENTER;");
		colQuantidade.setStyle("-fx-alignment: CENTER;");
		colOperadores.setStyle("-fx-alignment: CENTER;");
		tbProcessos.getColumns().setAll(colLote, colCodigo, colReator, colData, colInicio, colQuantidade,
				colOperadores);
	}

	@FXML
	public void onSelectLine() {
		if (produto != null) {
			Long index = produto.getId();
			produto = (Produto) tbProcessos.getSelectionModel().getSelectedItem();
			if (index == produto.getId()) {
				tbProcessos.getSelectionModel().clearSelection();
				produto = null;
				return;
			}
		} else {
			produto = (Produto) tbProcessos.getSelectionModel().getSelectedItem();
		}
	}

	@FXML
	private void cancelaProcesso() {
		if (produto != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmar cancelamento");
			alert.setHeaderText("Deseja realmente finalizar o lote selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				finaliza();
			}
		}
	}

	private void finaliza() {
		Task<Void> cancelTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				List<Date> dates = registroService.getPeriodo(produto.getLote());
				dataFinal = dates.get(1);
				return null;
			}
		};

		cancelTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Task<Void> updateTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						loteService.cancelaProcesso(dataFinal, produto.getLote());
						return null;
					}
				};

				updateTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						atualizar();
					}
				});
				Thread t = new Thread(updateTask);
				t.start();
			}
		});
		Thread t = new Thread(cancelTask);
		t.start();
	}

	@FXML
	public void hoverImgEstatica() {
		estaticaFadeTransition.setFromValue(0.2);
		estaticaFadeTransition.setToValue(1.0);
		estaticaFadeTransition.play();
	}

	@FXML
	public void unhoverImgEstatica() {
		estaticaFadeTransition.setFromValue(1.0);
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

	@FXML
	private void handleImgEstaticaAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/app/EstaticaInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o fabricante");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
		estaticaFadeTransition.setFromValue(imgEstatica.getOpacity());
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}
}
