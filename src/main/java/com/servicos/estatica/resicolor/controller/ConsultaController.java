package com.servicos.estatica.resicolor.controller;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import com.servicos.estatica.resicolor.report.builder.ParcialReportCreator;
import com.servicos.estatica.resicolor.report.builder.ProcessoReportCreator;
import com.servicos.estatica.resicolor.service.LoteDBService;
import com.servicos.estatica.resicolor.service.RegistroDBService;
import com.servicos.estatica.resicolor.service.impl.LoteDBServiceImpl;
import com.servicos.estatica.resicolor.service.impl.RegistroDBServiceImpl;
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

public class ConsultaController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TextField txtLote;
	@FXML
	private Label lblCodigo;
	@FXML
	private Label lblLote;
	@FXML
	private Label lblQuantidade;
	@FXML
	private Label lblProducao;
	@FXML
	private Label lblReator;
	@FXML
	private Label lblTempMin;
	@FXML
	private Label lblTempMax;
	@FXML
	private Label lblSetPoint;
	@FXML
	private Label lblInicio;
	@FXML
	private Label lblEncerramento;
	@FXML
	private Label lblTempoProcesso;
	@FXML
	private Label lblOperador;
	@FXML
	private LineChart<String, Number> chartConsulta;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
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
	private Button btConsultar;
	@FXML
	private Button btReport;
	@FXML
	private Button btXls;
	@FXML
	private Button btClear;
	@FXML
	private Button btConfigChart;
	@FXML
	private CheckBox checkMail;
	@FXML
	private ImageView imgEstatica;

	private static XYChart.Series<String, Number> tempSeries;
	private static XYChart.Series<String, Number> caldeiraSeries;
	private static DateTimeFormatter horasFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();
	private static SimpleDateFormat horasSdf = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer imgResizer;
	final List<Node> reatorMarks = new ArrayList<>();
	final List<Node> caldeiraMarks = new ArrayList<>();
	private Boolean marksVisible = false;

	private static String TOOLTIP_CSS = "-fx-font-size: 8pt; -fx-font-weight: bold; -fx-font-style: normal; ";
	private Tooltip TOOLTIP_REPORT = new Tooltip("Emitir relatório em PDF");
	private Tooltip TOOLTIP_XLS = new Tooltip("Emitir planilha XLS (Excel)");
	private Tooltip TOOLTIP_CONSULTAR = new Tooltip("Realizar consulta");
	private Tooltip TOOLTIP_CLEAR = new Tooltip("Limpar consulta");
	private Tooltip TOOLTIP_CONFIG = new Tooltip("Opções do gráfico de linhas");

	private static Produto produto;
	private static LoteDBService loteService = new LoteDBServiceImpl();
	private static RegistroDBService registroService = new RegistroDBServiceImpl();

	private LineChartConfigContext lineChartConfigContext = new LineChartConfigContext();

	String periodo = "";

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		configLineChart();

		lineChartConfigContext.getCurrentConfig().setMark(false);
		lineChartConfigContext.getCurrentConfig().setReatorA(true);
		lineChartConfigContext.getCurrentConfig().setCaldeiraA(true);
		lineChartConfigContext.getCurrentConfig().setReatorB(false);
		lineChartConfigContext.getCurrentConfig().setCaldeiraB(false);

		lineChartConfigContext.getCurrentConfig().markProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!reatorMarks.isEmpty()) {
					Node n1 = chartConsulta.lookup(".default-color0.chart-series-line");
					if (n1.isVisible()) {
						for (Node mark : reatorMarks) {
							mark.setVisible(newValue);
						}
					}
				}
				if (!caldeiraMarks.isEmpty()) {
					Node n1 = chartConsulta.lookup(".default-color1.chart-series-line");
					if (n1.isVisible()) {
						for (Node mark : caldeiraMarks) {
							mark.setVisible(newValue);
						}
					}
				}
				marksVisible = newValue;
			}
		});

		lineChartConfigContext.getCurrentConfig().reatorAProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleReatorVisibilty(newValue);
			}
		});

		lineChartConfigContext.getCurrentConfig().caldeiraAProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleCaldeiraVisibilty(newValue);
			}
		});

		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);
		imgEstatica.setImage(new Image("/com/servicos/estatica/resicolor/style/logotipo.png"));
		imgResizer = new ImageViewResizer(imgEstatica, 138, 42);
		imgResizer.setLayoutX(150.0);
		imgResizer.setLayoutY(150.0);
		imgResizer.setLayoutX(1083);
		imgResizer.setLayoutY(607);
		mainPane.getChildren().addAll(imgResizer);

		TOOLTIP_CLEAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_CONFIG.setStyle(TOOLTIP_CSS);
		TOOLTIP_CONSULTAR.setStyle(TOOLTIP_CSS);
		TOOLTIP_REPORT.setStyle(TOOLTIP_CSS);
		TOOLTIP_XLS.setStyle(TOOLTIP_CSS);
		Tooltip.install(btClear, TOOLTIP_CLEAR);
		Tooltip.install(btConsultar, TOOLTIP_CONSULTAR);
		Tooltip.install(btReport, TOOLTIP_REPORT);
		Tooltip.install(btXls, TOOLTIP_XLS);
		Tooltip.install(btConfigChart, TOOLTIP_CONFIG);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtLote.requestFocus();
			}
		});
	}

	private void toggleReatorVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color0.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: red;");
		if (!caldeiraMarks.isEmpty()) {
			for (Node mark : reatorMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	private void toggleCaldeiraVisibilty(Boolean b) {
		Node n1 = chartConsulta.lookup(".default-color1.chart-series-line");
		n1.setVisible(b);
		n1.setStyle("-fx-stroke: orange;");
		if (!caldeiraMarks.isEmpty()) {
			for (Node mark : caldeiraMarks) {
				if (b == false) {
					mark.setVisible(false);
				} else {
					mark.setVisible(marksVisible);
				}
			}
		}
	}

	private void styleSeries() {
		Node n1 = chartConsulta.lookup(".default-color0.chart-series-line");
		Node n2 = chartConsulta.lookup(".default-color1.chart-series-line");
		n1.setStyle("-fx-stroke: red; -fx-background-color: red, white;");
		n2.setStyle("-fx-stroke: orange; -fx-background-color: red, white;");
	}

	@FXML
	private void findByLote() {
		if ("".equals(txtLote.getText().trim())) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe o lote antes da consulta.");
			alert.showAndWait();
			txtLote.requestFocus();
			return;
		}
		intFetch(progA, progB, progC, progD, progReport, txtLote, btConsultar, btReport, btClear, btXls, lblCodigo,
				lblEncerramento, lblInicio, lblLote, lblOperador, lblProducao, lblReator, lblQuantidade, lblSetPoint,
				lblTempMax, lblTempMin, lblTempoProcesso, chartConsulta, xAxis, yAxis);

		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				produto = loteService.findByLote(Integer.parseInt(txtLote.getText()));
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				if (produto == null) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							fetched(progA, progB, progC, progD, progReport, txtLote, btConsultar, btReport, btClear,
									btXls, lblCodigo, lblEncerramento, lblInicio, lblLote, lblOperador, lblProducao,
									lblReator, lblQuantidade, lblSetPoint, lblTempMax, lblTempMin, lblTempoProcesso,
									chartConsulta, xAxis, yAxis);
							Toolkit.getDefaultToolkit().beep();
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Atenção");
							alert.setHeaderText("Lote não encontrado.");
							alert.showAndWait();
							txtLote.requestFocus();
							btReport.setDisable(Boolean.TRUE);
							btXls.setDisable(Boolean.TRUE);
							txtLote.requestFocus();
						}
					});
					return;
				}
				if (produto.getProcessos() == null) {
					System.out.println("Lista nula");
					fetched(progA, progB, progC, progD, progReport, txtLote, btConsultar, btReport, btClear, btXls,
							lblCodigo, lblEncerramento, lblInicio, lblLote, lblOperador, lblProducao, lblReator,
							lblQuantidade, lblSetPoint, lblTempMax, lblTempMin, lblTempoProcesso, chartConsulta, xAxis,
							yAxis);
					return;
				}
				if (produto.getProcessos().isEmpty()) {
					System.out.println("Lista vazia");
					fetched(progA, progB, progC, progD, progReport, txtLote, btConsultar, btReport, btClear, btXls,
							lblCodigo, lblEncerramento, lblInicio, lblLote, lblOperador, lblProducao, lblReator,
							lblQuantidade, lblSetPoint, lblTempMax, lblTempMin, lblTempoProcesso, chartConsulta, xAxis,
							yAxis);
					return;
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						fetched(progA, progB, progC, progD, progReport, txtLote, btConsultar, btReport, btClear, btXls,
								lblCodigo, lblEncerramento, lblInicio, lblLote, lblOperador, lblProducao, lblReator,
								lblQuantidade, lblSetPoint, lblTempMax, lblTempMin, lblTempoProcesso, chartConsulta,
								xAxis, yAxis);
						populateLineChart();
						populateFields();
						txtLote.requestFocus();
					}
				});
			}
		});

		progReport.progressProperty().bind(searchTask.progressProperty());

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
		fileChooser.setInitialFileName("lote_" + produto.getLote() + ".xls");
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
		txtLote.setDisable(Boolean.TRUE);
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
					HSSFRow titleRow = firstSheet.createRow(line);
					line++;
					titleRow.createCell(0).setCellValue("Horário");
					titleRow.createCell(1).setCellValue("Temperatura no reator");
					titleRow.createCell(2).setCellValue("Set-point do reator");
					titleRow.createCell(3).setCellValue("Temperatura na caldeira");
					titleRow.createCell(4).setCellValue("Set-point da caldeira");
					for (Processo processo : produto.getProcessos()) {
						HSSFRow row = firstSheet.createRow(line);
						row.createCell(0).setCellValue(dataHoraSdf.format(processo.getDtProcesso()));
						row.createCell(1).setCellValue(processo.getTempReator());
						row.createCell(2).setCellValue(processo.getSpReator());
						row.createCell(3).setCellValue(processo.getTempCaldeira());
						row.createCell(4).setCellValue(processo.getSpCaldeira());
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
				txtLote.setDisable(Boolean.FALSE);
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
		fileChooser.setInitialFileName("lote_" + produto.getLote() + ".pdf");
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
		txtLote.setDisable(Boolean.TRUE);
		btConsultar.setDisable(Boolean.TRUE);

		Task<Void> periodoTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (lblEncerramento.getText() == "" || lblEncerramento.getText() == null) {
					List<Date> dt = registroService.getPeriodo(Integer.parseInt(lblLote.getText()));
					Date dtIni = dt.get(0);
					Date dtFim = dt.get(1);
					periodo = PeriodFormatter.formatPeriod(dtIni, dtFim);
				} else {
					periodo = lblTempoProcesso.getText();
				}
				return null;
			}
		};

		periodoTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Task<Integer> reportTask = new Task<Integer>() {
					@Override
					protected Integer call() throws Exception {
						int result = 0;
						if (lblEncerramento.getText() == "" || lblEncerramento.getText() == null) {
							result = ParcialReportCreator.build(produto, file.getAbsolutePath(), periodo,
									lineChartConfigContext.getCurrentConfig());
						} else {
							result = ProcessoReportCreator.build(produto, file.getAbsolutePath(), periodo,
									lblProducao.getText(), lineChartConfigContext.getCurrentConfig());
						}
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
						txtLote.setDisable(Boolean.FALSE);
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
						txtLote.setDisable(Boolean.FALSE);
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
		});
		Thread t = new Thread(periodoTask);
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

	@FXML
	private void openConfigLineChart() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		URL url = getClass().getResource("/com/servicos/estatica/resicolor/app/ConfigChartConsulta.fxml");
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(url);
		fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());
		root = (Parent) fxmlloader.load(url.openStream());
		stage.setScene(new Scene(root));
		stage.setTitle("Opções do gráfico de linhas");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		((ConfigChartConsultaController) fxmlloader.getController()).setContext(lineChartConfigContext);
		stage.showAndWait();
	}

	@SuppressWarnings("unchecked")
	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(350);
		yAxis.setTickUnit(25);
		tempSeries = new XYChart.Series<String, Number>();
		caldeiraSeries = new XYChart.Series<String, Number>();
		plotValuesList.addAll(tempSeries, caldeiraSeries);
		chartConsulta.setData(plotValuesList);
	}

	@SuppressWarnings("unchecked")
	private void clearLineChart() {
		tempSeries.getData().clear();
		caldeiraSeries.getData().clear();
		chartConsulta.getData().clear();
		tempSeries = new XYChart.Series<String, Number>();
		caldeiraSeries = new XYChart.Series<String, Number>();
		chartConsulta.getData().addAll(tempSeries, caldeiraSeries);
	}

	private void populateLineChart() {
		clearLineChart();
		chartConsulta.setLegendVisible(true);
		for (Processo registro : produto.getProcessos()) {
			LocalDateTime horario = registro.getDtProcesso().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			XYChart.Data<String, Number> dataReator = new XYChart.Data<>(horasFormatter.format(horario),
					registro.getTempReator());
			createLineMark(reatorMarks, dataReator, registro.getTempReator(), "red");
			tempSeries.setName(txtLote.getText() + ": Reator");
			styleSeries();
			tempSeries.getData().add(dataReator);
		}
		for (Processo registro : produto.getProcessos()) {
			LocalDateTime horario = registro.getDtProcesso().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			XYChart.Data<String, Number> dataCaldeira = new XYChart.Data<>(horasFormatter.format(horario),
					registro.getTempCaldeira());
			createLineMark(caldeiraMarks, dataCaldeira, registro.getTempCaldeira(), "orange");
			caldeiraSeries.setName(txtLote.getText() + ": Caldeira");
			styleSeries();
			caldeiraSeries.getData().add(dataCaldeira);
		}
		toggleReatorVisibilty(lineChartConfigContext.getCurrentConfig().getReatorA());
		toggleCaldeiraVisibilty(lineChartConfigContext.getCurrentConfig().getCaldeiraA());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[0])
						.setStyle("-fx-background-color: red");
				((Node) chartConsulta.lookupAll(".chart-legend-item-symbol").toArray()[1])
						.setStyle("-fx-background-color: orange");
			}
		});
	}

	private void populateFields() {
		int size = produto.getProcessos().size();
		lblCodigo.setText(String.valueOf(produto.getCodigo()));
		lblLote.setText(String.valueOf(produto.getLote()));
		lblQuantidade.setText(String.valueOf(produto.getQuantidade()));
		lblReator.setText(produto.getNomeReator());
		lblTempMin.setText(String.valueOf(ProducaoUtil.getTempMin(produto)));
		lblTempMax.setText(String.valueOf(ProducaoUtil.getTempMax(produto)));
		lblSetPoint.setText(String.valueOf(produto.getProcessos().get(size - 1).getSpCaldeira()));
		lblInicio.setText(horasSdf.format(produto.getDtInicial()));
		lblEncerramento.setText(produto.getDtFinal() != null ? horasSdf.format(produto.getDtFinal()) : "");
		if (produto.getDtFinal() != null && produto.getDtInicial() != null) {
			lblTempoProcesso.setText(PeriodFormatter.formatPeriod(produto.getDtInicial(), produto.getDtFinal()));
		}
		lblOperador.setText(
				(produto.getOperador()) + (produto.getResponsavel1() != null ? "/" + produto.getResponsavel1() : ""));
		lblProducao.setText(ProducaoUtil.calculaProducao(lblTempoProcesso.getText(), lblQuantidade.getText()));
	}

	@FXML
	private void clearConsulta() {
		lblCodigo.setText("0");
		lblLote.setText("0");
		lblQuantidade.setText("000,00");
		lblProducao.setText("000,00");
		lblReator.setText("");
		lblTempMin.setText("00.0");
		lblTempMax.setText("00.0");
		lblSetPoint.setText("00.0");
		lblInicio.setText("00:00:00");
		lblEncerramento.setText("00:00:00");
		lblTempoProcesso.setText("00:00:00");
		lblOperador.setText("");
		clearLineChart();
		produto = null;
		btReport.setDisable(Boolean.TRUE);
		btXls.setDisable(Boolean.TRUE);
		txtLote.setText("");
		txtLote.requestFocus();
		chartConsulta.setLegendVisible(false);
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

	private void intFetch(Node... nodes) {
		for (Node node : nodes) {
			if (node instanceof ProgressIndicator) {
				node.setVisible(true);
			} else {
				node.setDisable(true);
			}
		}
	}

	private void fetched(Node... nodes) {
		for (Node node : nodes) {
			if (node instanceof ProgressIndicator) {
				node.setVisible(false);
			} else {
				node.setDisable(false);
			}
		}
	}

}
