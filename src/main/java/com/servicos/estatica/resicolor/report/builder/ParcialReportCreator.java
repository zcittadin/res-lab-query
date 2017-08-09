package com.servicos.estatica.resicolor.report.builder;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.Date;

import com.servicos.estatica.resicolor.config.LineChartConfig;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.report.template.ParcialReportTemplate;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;

public class ParcialReportCreator {

	public static int build(Produto produto, String path, String periodo, LineChartConfig lineChartConfig) {

		TextColumnBuilder<Date> dtProcessoColumn = col.column("Horário", "dtProcesso", type.timeHourToSecondType());
		TextColumnBuilder<Double> tempReatorColumn = col.column("T. reator (ºC)", "tempReator", type.doubleType());
		TextColumnBuilder<Double> setPointReatorColumn = col.column("Set-point reator (ºC)", "spReator",
				type.doubleType());
		TextColumnBuilder<Double> tempCaldeiraColumn = col.column("T. caldeira (ºC)", "tempCaldeira",
				type.doubleType());
		TextColumnBuilder<Double> setPointCaldeiraColumn = col.column("Set-point caldeira (ºC)", "spCaldeira",
				type.doubleType());

		try {
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
			report().setTemplate(ParcialReportTemplate.reportTemplate)
					.title(ParcialReportTemplate.createHeaderComponent(produto),
							ParcialReportTemplate.createSeparatorComponent(),
							ParcialReportTemplate.createDadosComponent(produto, periodo),
							ParcialReportTemplate.createSeparatorComponent(),
							ParcialReportTemplate.createChartComponent(produto, lineChartConfig),
							ParcialReportTemplate.createSeparatorComponent())
					.setDataSource(produto.getProcessos())
					.columns(dtProcessoColumn, tempReatorColumn, setPointReatorColumn, tempCaldeiraColumn,
							setPointCaldeiraColumn)
					.summary(ParcialReportTemplate.createEmissaoComponent())
					.pageFooter(ParcialReportTemplate.footerComponent).toPdf(pdfExporter);
			return 1;
		} catch (DRException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
