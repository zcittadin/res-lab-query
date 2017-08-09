package com.servicos.estatica.resicolor.controller;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.servicos.estatica.resicolor.app.ControlledScreen;
import com.servicos.estatica.resicolor.config.LineChartConfigContext;
import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.report.builder.ComparativoReportCreator;
import com.servicos.estatica.resicolor.service.LoteDBService;
import com.servicos.estatica.resicolor.service.impl.LoteDBServiceImpl;
import com.servicos.estatica.resicolor.util.HoverDataChart;
import com.servicos.estatica.resicolor.util.PeriodFormatter;
import com.servicos.estatica.resicolor.util.ProducaoUtil;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class ComparacoesController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TextField txtLoteA;
	@FXML
	private TextField txtLoteB;
	@FXML
	private Label lblCodigoA;
	@FXML
	private Label lblLoteA;
	@FXML
	private Label lblQuantidadeA;
	@FXML
	private Label lblProducaoA;
	@FXML
	private Label lblReatorA;
	@FXML
	private Label lblTempMinA;
	@FXML
	private Label lblTempMaxA;
	@FXML
	private Label lblSetPointA;
	@FXML
	private Label lblInicioA;
	@FXML
	private Label lblEncerramentoA;
	@FXML
	private Label lblTempoProcessoA;
	@FXML
	private Label lblOperadorA;
	@FXML
	private Label lblCodigoB;
	@FXML
	private Label lblLoteB;
	@FXML
	private Label lblQuantidadeB;
	@FXML
	private Label lblProducaoB;
	@FXML
	private Label lblReatorB;
	@FXML
	private Label lblTempMinB;
	@FXML
	private Label lblTempMaxB;
	@FXML
	private Label lblSetPointB;
	@FXML
	private Label lblInicioB;
	@FXML
	private Label lblEncerramentoB;
	@FXML
	private Label lblTempoProcessoB;
	@FXML
	private Label lblOperadorB;
	@FXML
	private LineChart<String, Number> chartConsulta;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private ImageView imgEstatica;
	@FXML
	private ProgressIndicator progA;
	@FXML
	private ProgressIndicator progB;
	@FXML
	private ProgressIndicator progC;
	@FXML
	private ProgressIndicator progD;
	@FXML
	private ProgressIndicator progReport;
	@FXML
	private Button btReport;
	@FXML
	private Button btXls;
	@FXML
	private Button btConsultar;
	@FXML
	private Button btClear;
	@FXML
	private Button btConfigLineChart;
	@FXML
	private CheckBox chkMarcadores;

	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer estaticaResizer;
	private static Series<String, Number> tempReatorSeriesA;
	private static Series<String, Number> tempCaldeiraSeriesA;
	private static Series<String, Number> tempReatorSeriesB;
	private static Series<String, Number> tempCaldeiraSeriesB;
	private static SimpleDateFormat horasSdf = new SimpleDateFormat("HH:mm:ss");
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; ";
	private Tooltip TOOLTIP_REPORT = new Tooltip("Emitir relatório em PDF");
	private Tooltip TOOLTIP_XLS = new Tooltip("Emitir planilha XLS (Excel)");
	private Tooltip TOOLTIP_CONSULTAR = new Tooltip("Realizar consulta");
	private Tooltip TOOLTIP_CLEAR = new Tooltip("Limpar consulta");
	private Tooltip TOOLTIP_CONFIG = new Tooltip("Opções do gráfico de linhas");

	private static Produto produtoA;
	private static Produto produtoB;
	private static LoteDBService loteService = new LoteDBServiceImpl();

	final List<Node> reatorAMarks = new ArrayList<>();
	final List<Node> reatorBMarks = new ArrayList<>();
	final List<Node> caldeiraAMarks = new ArrayList<>();
	final List<Node> caldeiraBMarks = new ArrayList<>();

	private static SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");

	private Boolean marksVisible = false;

	private LineChartConfigContext lineChartConfigContext = new LineChartConfigContext();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		configLineChart();

		lineChartConfigContext.getCurrentConfig().setReatorA(true);
		lineChartConfigContext.getCurrentConfig().setCaldeiraA(true);
		lineChartConfigContext.getCurrentConfig().setReatorB(true);
		lineChartConfigContext.getCurrentConfig().setCaldeiraB(true);

		imgEstatica.setImage(new Image("/com/servicos/estatica/resicolor/style/logotipo.png"));
		estaticaResizer = new ImageViewResizer(imgEstatica, 138, 42);
		estaticaResizer.setLayoutX(150.0);
		estaticaResizer.setLayoutY(150.0);
		estaticaResizer.setLayoutX(1083);
		estaticaResizer.setLayoutY(607);
		mainPane.getChildren().addAll(estaticaResizer);

		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);

		lineChartConfigContext.getCurrentConfig().markProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				Node n1 = chartConsulta.lookup(".default-color0.chart-series-line");
				if (n1.isVisible()) {
					for (Node mark : reatorAMarks) {
						mark.setVisible(newValue);
					}
				}
				Node n2 = chartConsulta.lookup(".default-color1.chart-series-line");
				if (n2.isVisible()) {
					for (Node mark : reatorBMarks) {
						mark.setVisible(newValue);
					}
				}
				Node n3 = chartConsulta.lookup(".default-color2.chart-series-line");
				if (n3.isVisible()) {
					for (Node mark : caldeiraAMarks) {
						mark.setVisible(newValue);
					}
				}
				Node n4 = chartConsulta.lookup(".default-color3.chart-series-line");
				if (n4.isVisible()) {
					for (Node mark : caldeiraBMarks) {
						mark.setVisible(newValue);
					}
				}
				marksVisible = newValue;
			}
		});

		lineChartConfigContext.getCurrentConfig().reatorAProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleReatorAVisibilty(newValue);
			}
		});

		lineChartConfigContext.getCurrentConfig().caldeiraAProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleCaldeiraAVisibilty(newValue);
			}
		});

		lineChartConfigContext.getCurrentConfig().reatorBProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleReatorBVisibilty(newValue);
			}
		});

		lineChartConfigContext.getCurrentConfig().caldeiraBProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleCaldeiraBVisibilty(newValue);
			}
		});

		TOOLTIP_CLEAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_CONFIG.setStyle(TOOLTIP_CSS);
		TOOLTIP_CONSULTAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_REPORT.setStyle(TOOLTIP_CSS);
		TOOLTIP_XLS.setStyle(TOOLTIP_CSS);
		Tooltip.install(btClear, TOOLTIP_CLEAR);
		Tooltip.install(btConsultar, TOOLTIP_CONSULTAR);
		Tooltip.install(btReport, TOOLTIP_REPORT);
		Tooltip.install(btXls, TOOLTIP_XLS);
		Tooltip.install(btConfigLineChart, TOOLTIP_CONFIG);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtLoteA.requestFocus();
			}
		});

	}

	private void toggleReatorAVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color0.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: purple;");
		if (!reatorAMarks.isEmpty()) {
			for (Node mark : reatorAMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	private void toggleCaldeiraAVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color2.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: olive;");
		if (!caldeiraAMarks.isEmpty()) {
			for (Node mark : caldeiraAMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	private void toggleReatorBVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color1.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: red;");
		if (!reatorBMarks.isEmpty()) {
			for (Node mark : reatorBMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	private void toggleCaldeiraBVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color3.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: blue;");
		if (!caldeiraBMarks.isEmpty()) {
			for (Node mark : caldeiraBMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	@FXML
	private void buscar() {
		findComparative();
	}

	@FXML
	private void clearConsulta() {
		lblCodigoA.setText("0");
		lblLoteA.setText("0");
		lblQuantidadeA.setText("000,00");
		lblProducaoA.setText("000,00");
		lblReatorA.setText("");
		lblTempMinA.setText("00.0");
		lblTempMaxA.setText("00.0");
		lblSetPointA.setText("00.0");
		lblInicioA.setText("00:00:00");
		lblEncerramentoA.setText("00:00:00");
		lblTempoProcessoA.setText("00:00:00");
		lblOperadorA.setText("");
		produtoA = null;

		lblCodigoB.setText("0");
		lblLoteB.setText("0");
		lblQuantidadeB.setText("000,00");
		lblProducaoB.setText("000,00");
		lblReatorB.setText("");
		lblTempMinB.setText("00.0");
		lblTempMaxB.setText("00.0");
		lblSetPointB.setText("00.0");
		lblInicioB.setText("00:00:00");
		lblEncerramentoB.setText("00:00:00");
		lblTempoProcessoB.setText("00:00:00");
		lblOperadorB.setText("");
		produtoB = null;
		clearLineChart();

		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		txtLoteA.setText("");
		txtLoteB.setText("");
		txtLoteA.requestFocus();
		chartConsulta.setLegendVisible(false);
	}

	private void findComparative() {

		if ("".equals(txtLoteA.getText().trim()) || "".equals(txtLoteB.getText().trim())) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe os dois lotes a serem comparados.");
			alert.showAndWait();
			txtLoteA.requestFocus();
			return;
		}

		initFetch();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				produtoA = loteService.findByLote(Integer.parseInt(txtLoteA.getText()));
				produtoB = loteService.findByLote(Integer.parseInt(txtLoteB.getText()));
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {

				if (produtoA == null) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endFetch();
							Toolkit.getDefaultToolkit().beep();
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Atenção");
							alert.setHeaderText("O lote " + txtLoteA.getText() + " não foi encontrado.");
							alert.showAndWait();
							btReport.setDisable(Boolean.TRUE);
							btXls.setDisable(Boolean.TRUE);
							txtLoteA.requestFocus();
						}
					});
					return;
				}
				if (produtoB == null) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endFetch();
							Toolkit.getDefaultToolkit().beep();
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Atenção");
							alert.setHeaderText("O lote " + txtLoteB.getText() + " não foi encontrado.");
							alert.showAndWait();
							btReport.setDisable(Boolean.TRUE);
							btXls.setDisable(Boolean.TRUE);
							txtLoteB.requestFocus();
						}
					});
					return;
				}
				if (produtoA.getProcessos() == null) {
					System.out.println("Lista nula");
					endFetch();
					return;
				}
				if (produtoA.getProcessos().isEmpty()) {
					System.out.println("Lista vazia");
					endFetch();
					return;
				}

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						endFetch();
						populateComparative();
						populateFields();
						txtLoteA.requestFocus();
					}
				});
			}
		});
		// progReport.progressProperty().bind(searchTask.progressProperty());
		Thread t = new Thread(searchTask);
		t.start();
	}

	@FXML
	public void saveXls() {
		Stage stage = new Stage();
		stage.initOwner(btReport.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XLS Files", "*.xls"));
		fileChooser.setTitle("Salvar planilha de processo");
		fileChooser.setInitialFileName("lote_" + produtoA.getLote() + "_" + produtoB.getLote() + ".xls");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generateXlsReport(savedFile);
		}
	}

	@SuppressWarnings("resource")
	private void generateXlsReport(File file) {
		progReport.setVisible(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		txtLoteA.setDisable(Boolean.TRUE);
		txtLoteB.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		Task<Void> xlsTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				int maximum = 20;
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet firstSheet = workbook.createSheet("Aba1");
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					int line = 0;
					HSSFRow headerRowA = firstSheet.createRow(line);
					headerRowA.createCell(0).setCellValue("Lote " + produtoA.getLote());
					line++;
					HSSFRow titleRowA = firstSheet.createRow(line);
					line++;
					titleRowA.createCell(0).setCellValue("Horário");
					titleRowA.createCell(1).setCellValue("Temperatura no reator");
					titleRowA.createCell(2).setCellValue("Set-point do reator");
					titleRowA.createCell(3).setCellValue("Temperatura na caldeira");
					titleRowA.createCell(4).setCellValue("Set-point da caldeira");
					for (Processo processo : produtoA.getProcessos()) {
						HSSFRow rowA = firstSheet.createRow(line);
						rowA.createCell(0).setCellValue(dataHoraSdf.format(processo.getDtProcesso()));
						rowA.createCell(1).setCellValue(processo.getTempReator());
						rowA.createCell(2).setCellValue(processo.getSpReator());
						rowA.createCell(3).setCellValue(processo.getTempCaldeira());
						rowA.createCell(4).setCellValue(processo.getSpCaldeira());
						line++;
					}
					HSSFRow headerRowB = firstSheet.createRow(line);
					headerRowB.createCell(0).setCellValue("Lote " + produtoB.getLote());
					line++;
					HSSFRow titleRowB = firstSheet.createRow(line);
					line++;
					titleRowB.createCell(0).setCellValue("Horário");
					titleRowB.createCell(1).setCellValue("Temperatura no reator");
					titleRowB.createCell(2).setCellValue("Set-point do reator");
					titleRowB.createCell(3).setCellValue("Temperatura na caldeira");
					titleRowB.createCell(4).setCellValue("Set-point da caldeira");
					for (Processo processo : produtoB.getProcessos()) {
						HSSFRow rowB = firstSheet.createRow(line);
						rowB.createCell(0).setCellValue(dataHoraSdf.format(processo.getDtProcesso()));
						rowB.createCell(1).setCellValue(processo.getTempReator());
						rowB.createCell(2).setCellValue(processo.getSpReator());
						rowB.createCell(3).setCellValue(processo.getTempCaldeira());
						rowB.createCell(4).setCellValue(processo.getSpCaldeira());
						line++;
					}
					workbook.write(fos);
					for (int i = 0; i < maximum; i++) {
						updateProgress(i, maximum);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao exportar arquivo");
				} finally {
					try {
						fos.flush();
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};

		xlsTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progReport.setVisible(Boolean.FALSE);
				btReport.setDisable(Boolean.FALSE);
				btXls.setDisable(Boolean.FALSE);
				btClear.setDisable(Boolean.FALSE);
				txtLoteA.setDisable(Boolean.FALSE);
				txtLoteB.setDisable(Boolean.FALSE);
				btConsultar.setDisable(Boolean.FALSE);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Concluído");
				alert.setHeaderText("Planilha de dados emitida com sucesso. Deseja visualizar?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		progReport.progressProperty().bind(xlsTask.progressProperty());
		Thread t = new Thread(xlsTask);
		t.start();
	}

	@FXML
	public void saveReport() {
		Stage stage = new Stage();
		stage.initOwner(btReport.getScene().getWindow());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
		fileChooser.setTitle("Salvar relatório de processo");
		fileChooser.setInitialFileName("lote_" + produtoA.getLote() + "_" + produtoB.getLote() + ".pdf");
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile != null) {
			generatePdfReport(savedFile);
		}
	}

	private void generatePdfReport(File file) {
		progReport.setVisible(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		txtLoteA.setDisable(Boolean.TRUE);
		txtLoteB.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		Task<Integer> reportTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				int result = ComparativoReportCreator.build(produtoA, produtoB, file.getAbsolutePath(),
						lblTempoProcessoA.getText(), lblTempoProcessoB.getText(), lblProducaoA.getText(),
						lblProducaoB.getText(), lineChartConfigContext.getCurrentConfig());
				int maximum = 20;
				for (int i = 0; i < maximum; i++) {
					updateProgress(i, maximum);
				}
				return new Integer(result);
			}
		};

		reportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progReport.setVisible(Boolean.FALSE);
				btReport.setDisable(Boolean.FALSE);
				btXls.setDisable(Boolean.FALSE);
				btClear.setDisable(Boolean.FALSE);
				txtLoteA.setDisable(Boolean.FALSE);
				txtLoteB.setDisable(Boolean.FALSE);
				btConsultar.setDisable(Boolean.FALSE);
				int r = reportTask.getValue();
				if (r != 1) {
					Toolkit.getDefaultToolkit().beep();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erro");
					alert.setHeaderText("Houve uma falha na emissão do relatório.");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Concluído");
				alert.setHeaderText("Relatório emitido com sucesso. Deseja visualizar?");
				// if (checkMail.isSelected())
				// sendMailReport(file);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		reportTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				progReport.setVisible(Boolean.FALSE);
				btReport.setDisable(Boolean.FALSE);
				btXls.setDisable(Boolean.FALSE);
				btClear.setDisable(Boolean.FALSE);
				txtLoteA.setDisable(Boolean.FALSE);
				txtLoteB.setDisable(Boolean.FALSE);
				btConsultar.setDisable(Boolean.FALSE);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText("Houve uma falha na emissão do relatório.");
				alert.showAndWait();
			}
		});

		progReport.progressProperty().bind(reportTask.progressProperty());

		Thread t = new Thread(reportTask);
		t.start();
	}

	@FXML
	private void openConfigLineChart() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		URL url = getClass().getResource("/com/servicos/estatica/resicolor/app/ConfigChartComparativo.fxml");
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(url);
		fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());
		root = (Parent) fxmlloader.load(url.openStream());
		stage.setScene(new Scene(root));
		stage.setTitle("Opções do gráfico de linhas");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		((ConfigChartComparativoController) fxmlloader.getController()).setContext(lineChartConfigContext);
		stage.showAndWait();
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

	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(350);
		yAxis.setTickUnit(25);
		tempReatorSeriesA = new XYChart.Series<String, Number>();
		tempReatorSeriesB = new XYChart.Series<String, Number>();
		tempCaldeiraSeriesA = new XYChart.Series<String, Number>();
		tempCaldeiraSeriesB = new XYChart.Series<String, Number>();
		plotValuesList.add(tempReatorSeriesA);
		plotValuesList.add(tempReatorSeriesB);
		plotValuesList.add(tempCaldeiraSeriesA);
		plotValuesList.add(tempCaldeiraSeriesB);
		chartConsulta.setData(plotValuesList);
	}

	private void clearLineChart() {
		tempReatorSeriesA.getData().clear();
		tempReatorSeriesB.getData().clear();
		tempCaldeiraSeriesA.getData().clear();
		tempCaldeiraSeriesB.getData().clear();
		tempReatorSeriesA = new XYChart.Series<String, Number>();
		tempReatorSeriesB = new XYChart.Series<String, Number>();
		tempCaldeiraSeriesA = new XYChart.Series<String, Number>();
		tempCaldeiraSeriesB = new XYChart.Series<String, Number>();
		chartConsulta.getData().add(tempReatorSeriesA);
		chartConsulta.getData().add(tempReatorSeriesB);
		chartConsulta.getData().add(tempCaldeiraSeriesA);
		chartConsulta.getData().add(tempCaldeiraSeriesB);
		Node reatorALine = chartConsulta.lookup(".default-color0.chart-series-line");
		Node caldeiraALine = chartConsulta.lookup(".default-color2.chart-series-line");
		Node reatorBLine = chartConsulta.lookup(".default-color1.chart-series-line");
		Node caldeiraBLine = chartConsulta.lookup(".default-color3.chart-series-line");
		reatorALine.setStyle("-fx-stroke: purple;");
		caldeiraALine.setStyle("-fx-stroke: olive;");
		reatorBLine.setStyle("-fx-stroke: red;");
		caldeiraBLine.setStyle("-fx-stroke: blue;");
	}

	private void populateComparative() {
		chartConsulta.getData().clear();
		clearLineChart();
		if (produtoA.getProcessos().size() >= produtoB.getProcessos().size()) {
			LocalDateTime inicio = produtoA.getProcessos().get(0).getDtProcesso().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDateTime();
			int i = 0;
			for (Processo registro : produtoA.getProcessos()) {
				LocalDateTime horario = registro.getDtProcesso().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDateTime();
				long millis = inicio.until(horario, ChronoUnit.MILLIS);
				LocalDateTime intervalo = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
				XYChart.Data<String, Number> dataReatorA = new XYChart.Data<>(horasFormatter.format(intervalo),
						registro.getTempReator());
				XYChart.Data<String, Number> dataCaldeiraA = new XYChart.Data<>(horasFormatter.format(intervalo),
						registro.getTempCaldeira());

				createLineMark(reatorAMarks, dataReatorA, registro.getTempReator(), "purple");
				createLineMark(caldeiraAMarks, dataCaldeiraA, registro.getTempCaldeira(), "olive");

				tempReatorSeriesA.setName(txtLoteA.getText() + ": Reator");
				tempCaldeiraSeriesA.setName(txtLoteA.getText() + ": Caldeira");
				tempReatorSeriesA.getData().add(dataReatorA);
				tempCaldeiraSeriesA.getData().add(dataCaldeiraA);

				if (produtoB.getProcessos().size() > i) {
					XYChart.Data<String, Number> dataReatorB = new XYChart.Data<>(horasFormatter.format(intervalo),
							produtoB.getProcessos().get(i).getTempReator());
					XYChart.Data<String, Number> dataCaldeiraB = new XYChart.Data<>(horasFormatter.format(intervalo),
							produtoB.getProcessos().get(i).getTempCaldeira());

					createLineMark(reatorBMarks, dataReatorB, produtoB.getProcessos().get(i).getTempReator(), "red");
					createLineMark(caldeiraBMarks, dataCaldeiraB, produtoB.getProcessos().get(i).getTempCaldeira(),
							"blue");

					tempReatorSeriesB.setName(txtLoteB.getText() + ": Reator");
					tempCaldeiraSeriesB.setName(txtLoteB.getText() + ": Caldeira");
					tempReatorSeriesB.getData().add(dataReatorB);
					tempCaldeiraSeriesB.getData().add(dataCaldeiraB);
				}
				i++;
			}

		} else {
			LocalDateTime inicio = produtoB.getProcessos().get(0).getDtProcesso().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDateTime();
			int i = 0;
			for (Processo registro : produtoB.getProcessos()) {
				LocalDateTime horario = registro.getDtProcesso().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDateTime();
				long millis = inicio.until(horario, ChronoUnit.MILLIS);
				LocalDateTime intervalo = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
				XYChart.Data<String, Number> dataReatorA = new XYChart.Data<>(horasFormatter.format(intervalo),
						registro.getTempReator());
				XYChart.Data<String, Number> dataCaldeiraA = new XYChart.Data<>(horasFormatter.format(intervalo),
						registro.getTempCaldeira());

				createLineMark(reatorBMarks, dataReatorA, registro.getTempReator(), "red");
				createLineMark(caldeiraBMarks, dataCaldeiraA, registro.getTempCaldeira(), "blue");

				tempReatorSeriesB.setName(txtLoteB.getText() + ": Reator");
				tempCaldeiraSeriesB.setName(txtLoteB.getText() + ": Caldeira");
				tempReatorSeriesB.getData().add(dataReatorA);
				tempCaldeiraSeriesB.getData().add(dataCaldeiraA);

				if (produtoA.getProcessos().size() > i) {
					XYChart.Data<String, Number> dataReatorB = new XYChart.Data<>(horasFormatter.format(intervalo),
							produtoA.getProcessos().get(i).getTempReator());
					XYChart.Data<String, Number> dataCaldeiraB = new XYChart.Data<>(horasFormatter.format(intervalo),
							produtoA.getProcessos().get(i).getTempCaldeira());

					createLineMark(reatorAMarks, dataReatorB, produtoA.getProcessos().get(i).getTempReator(),
							"purple;");
					createLineMark(caldeiraAMarks, dataCaldeiraB, produtoA.getProcessos().get(i).getTempCaldeira(),
							"olive;");

					tempReatorSeriesA.setName(txtLoteA.getText() + ": Reator");
					tempCaldeiraSeriesA.setName(txtLoteA.getText() + ": Caldeira");
					tempReatorSeriesA.getData().add(dataReatorB);
					tempCaldeiraSeriesA.getData().add(dataCaldeiraB);
				}
				i++;
			}
		}
		toggleReatorAVisibilty(lineChartConfigContext.getCurrentConfig().getReatorA());
		toggleCaldeiraAVisibilty(lineChartConfigContext.getCurrentConfig().getCaldeiraA());
		toggleReatorBVisibilty(lineChartConfigContext.getCurrentConfig().getReatorB());
		toggleCaldeiraBVisibilty(lineChartConfigContext.getCurrentConfig().getCaldeiraB());
		chartConsulta.setLegendVisible(true);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[0])
						.setStyle("-fx-background-color: purple");
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[1])
						.setStyle("-fx-background-color: red");
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[2])
						.setStyle("-fx-background-color: olive");
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[3])
						.setStyle("-fx-background-color: blue");
			}
		});
	}

	private void populateFields() {
		lblCodigoA.setText(String.valueOf(produtoA.getCodigo()));
		lblLoteA.setText(String.valueOf(produtoA.getLote()));
		lblQuantidadeA.setText(String.valueOf(produtoA.getQuantidade()));
		lblReatorA.setText(produtoA.getNomeReator());
		lblTempMinA.setText(String.valueOf(ProducaoUtil.getTempMin(produtoA)));
		lblTempMaxA.setText(String.valueOf(ProducaoUtil.getTempMax(produtoA)));
		lblSetPointA.setText(String.valueOf(produtoA.getProcessos().get(0).getSpReator()));
		lblInicioA.setText(horasSdf.format(produtoA.getDtInicial()));
		lblEncerramentoA.setText(produtoA.getDtFinal() != null ? horasSdf.format(produtoA.getDtFinal()) : "");
		if (produtoA.getDtFinal() != null && produtoA.getDtInicial() != null) {
			lblTempoProcessoA.setText(PeriodFormatter.formatPeriod(produtoA.getDtInicial(), produtoA.getDtFinal()));
		}
		lblOperadorA.setText((produtoA.getOperador())
				+ (produtoA.getResponsavel1() != null ? "/" + produtoA.getResponsavel1() : ""));
		lblProducaoA.setText(ProducaoUtil.calculaProducao(lblTempoProcessoA.getText(), lblQuantidadeA.getText()));

		lblCodigoB.setText(String.valueOf(produtoB.getCodigo()));
		lblLoteB.setText(String.valueOf(produtoB.getLote()));
		lblQuantidadeB.setText(String.valueOf(produtoB.getQuantidade()));
		lblReatorB.setText(produtoB.getNomeReator());
		lblTempMinB.setText(String.valueOf(ProducaoUtil.getTempMin(produtoB)));
		lblTempMaxB.setText(String.valueOf(ProducaoUtil.getTempMax(produtoB)));
		lblSetPointB.setText(String.valueOf(produtoB.getProcessos().get(0).getSpReator()));
		lblInicioB.setText(horasSdf.format(produtoB.getDtInicial()));
		lblEncerramentoB.setText(produtoB.getDtFinal() != null ? horasSdf.format(produtoB.getDtFinal()) : "");
		if (produtoB.getDtFinal() != null && produtoB.getDtInicial() != null) {
			lblTempoProcessoB.setText(PeriodFormatter.formatPeriod(produtoB.getDtInicial(), produtoB.getDtFinal()));
		}
		lblOperadorB.setText((produtoB.getOperador())
				+ (produtoB.getResponsavel1() != null ? "/" + produtoB.getResponsavel1() : ""));
		lblProducaoB.setText(ProducaoUtil.calculaProducao(lblTempoProcessoB.getText(), lblQuantidadeB.getText()));
	}

	private void initFetch() {
		progA.setVisible(Boolean.TRUE);
		progB.setVisible(Boolean.TRUE);
		progC.setVisible(Boolean.TRUE);
		progD.setVisible(Boolean.TRUE);
		progReport.setVisible(Boolean.TRUE);
		txtLoteA.setDisable(Boolean.TRUE);
		txtLoteB.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);
		btReport.setDisable(Boolean.TRUE);
		btClear.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		lblCodigoA.setDisable(Boolean.TRUE);
		lblEncerramentoA.setDisable(Boolean.TRUE);
		lblInicioA.setDisable(Boolean.TRUE);
		lblLoteA.setDisable(Boolean.TRUE);
		lblOperadorA.setDisable(Boolean.TRUE);
		lblProducaoA.setDisable(Boolean.TRUE);
		lblReatorA.setDisable(Boolean.TRUE);
		lblQuantidadeA.setDisable(Boolean.TRUE);
		lblSetPointA.setDisable(Boolean.TRUE);
		lblTempMaxA.setDisable(Boolean.TRUE);
		lblTempMinA.setDisable(Boolean.TRUE);
		lblTempoProcessoA.setDisable(Boolean.TRUE);
		lblCodigoB.setDisable(Boolean.TRUE);
		lblEncerramentoB.setDisable(Boolean.TRUE);
		lblInicioB.setDisable(Boolean.TRUE);
		lblLoteB.setDisable(Boolean.TRUE);
		lblOperadorB.setDisable(Boolean.TRUE);
		lblProducaoB.setDisable(Boolean.TRUE);
		lblReatorB.setDisable(Boolean.TRUE);
		lblQuantidadeB.setDisable(Boolean.TRUE);
		lblSetPointB.setDisable(Boolean.TRUE);
		lblTempMaxB.setDisable(Boolean.TRUE);
		lblTempMinB.setDisable(Boolean.TRUE);
		lblTempoProcessoB.setDisable(Boolean.TRUE);
		chartConsulta.setDisable(Boolean.TRUE);
		xAxis.setDisable(Boolean.TRUE);
		yAxis.setDisable(Boolean.TRUE);

	}

	private void endFetch() {
		progA.setVisible(Boolean.FALSE);
		progB.setVisible(Boolean.FALSE);
		progC.setVisible(Boolean.FALSE);
		progD.setVisible(Boolean.FALSE);
		progReport.setVisible(Boolean.FALSE);
		txtLoteA.setDisable(Boolean.FALSE);
		txtLoteB.setDisable(Boolean.FALSE);
		btConsultar.setDisable(Boolean.FALSE);
		btReport.setDisable(Boolean.FALSE);
		btClear.setDisable(Boolean.FALSE);
		btXls.setDisable(Boolean.FALSE);
		lblCodigoA.setDisable(Boolean.FALSE);
		lblEncerramentoA.setDisable(Boolean.FALSE);
		lblInicioA.setDisable(Boolean.FALSE);
		lblLoteA.setDisable(Boolean.FALSE);
		lblOperadorA.setDisable(Boolean.FALSE);
		lblProducaoA.setDisable(Boolean.FALSE);
		lblReatorA.setDisable(Boolean.FALSE);
		lblQuantidadeA.setDisable(Boolean.FALSE);
		lblSetPointA.setDisable(Boolean.FALSE);
		lblTempMaxA.setDisable(Boolean.FALSE);
		lblTempMinA.setDisable(Boolean.FALSE);
		lblTempoProcessoA.setDisable(Boolean.FALSE);
		lblCodigoB.setDisable(Boolean.FALSE);
		lblEncerramentoB.setDisable(Boolean.FALSE);
		lblInicioB.setDisable(Boolean.FALSE);
		lblLoteB.setDisable(Boolean.FALSE);
		lblOperadorB.setDisable(Boolean.FALSE);
		lblProducaoB.setDisable(Boolean.FALSE);
		lblReatorB.setDisable(Boolean.FALSE);
		lblQuantidadeB.setDisable(Boolean.FALSE);
		lblSetPointB.setDisable(Boolean.FALSE);
		lblTempMaxB.setDisable(Boolean.FALSE);
		lblTempMinB.setDisable(Boolean.FALSE);
		lblTempoProcessoB.setDisable(Boolean.FALSE);
		chartConsulta.setDisable(Boolean.FALSE);
		xAxis.setDisable(Boolean.FALSE);
		yAxis.setDisable(Boolean.FALSE);
	}

	private void createLineMark(List<Node> nodes, Data<String, Number> data, Double value, String color) {
		Node mark = new HoverDataChart(1, value);
		mark.setStyle("-fx-background-color: " + color);
		nodes.add(mark);
		data.setNode(mark);
		if (lineChartConfigContext.getCurrentConfig().getMark()) {
			mark.setVisible(Boolean.TRUE);
		} else {
			mark.setVisible(Boolean.FALSE);
		}
	}

}
