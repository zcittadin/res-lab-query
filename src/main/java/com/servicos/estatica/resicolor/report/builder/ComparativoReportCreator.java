package com.servicos.estatica.resicolor.report.builder;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.config.LineChartConfig;
import com.servicos.estatica.resicolor.model.ComparativoReportModel;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.report.template.ComparativoReportTemplate;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.exception.DRException;

public class ComparativoReportCreator {

	public static int build(Produto produtoA, Produto produtoB, String path, String periodoA, String periodoB,
			String producaoA, String producaoB, LineChartConfig lineChartConfig) {
		List<ComparativoReportModel> models = new ArrayList<>();
		int sizeA = produtoA.getProcessos().size();
		int sizeB = produtoB.getProcessos().size();
		int i = 0;
		if (sizeA >= sizeB) {
			Date inicio = produtoA.getProcessos().get(0).getDtProcesso();
			for (i = 0; i < sizeA; i++) {
				Date horario = produtoA.getProcessos().get(i).getDtProcesso();
				long millis = horario.getTime() - inicio.getTime();
				LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
				Date intervalo = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
				models.add(new ComparativoReportModel(intervalo, produtoA.getProcessos().get(i).getTempReator(),
						i < sizeB ? produtoB.getProcessos().get(i).getTempReator() : 0,
						produtoA.getProcessos().get(i).getTempCaldeira(),
						i < sizeB ? produtoB.getProcessos().get(i).getTempCaldeira() : 0));
			}
		} else {
			Date inicio = produtoB.getProcessos().get(0).getDtProcesso();
			for (i = 0; i < sizeB; i++) {
				Date horario = produtoB.getProcessos().get(i).getDtProcesso();
				long millis = horario.getTime() - inicio.getTime();
				LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
				Date intervalo = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
				models.add(new ComparativoReportModel(intervalo,
						i < sizeA ? produtoA.getProcessos().get(i).getTempReator() : 0,
						produtoB.getProcessos().get(i).getTempReator(),
						i < sizeA ? produtoA.getProcessos().get(i).getTempCaldeira() : 0,
						produtoB.getProcessos().get(i).getTempCaldeira()));
			}
		}

		try {
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
			report().setTemplate(ComparativoReportTemplate.reportTemplate)
					.title(ComparativoReportTemplate.createHeaderComponent(produtoA, produtoB),
							ComparativoReportTemplate.createSeparatorComponent(),
							ComparativoReportTemplate.createDadosComponent(produtoA, periodoA, producaoA),
							ComparativoReportTemplate.createSeparatorComponent(),
							ComparativoReportTemplate.createDadosComponent(produtoB, periodoB, producaoB),
							ComparativoReportTemplate.createSeparatorComponent(),
							ComparativoReportTemplate.createChartComponent(models, lineChartConfig, produtoA.getLote(),
									produtoB.getLote()),
							ComparativoReportTemplate.createSeparatorComponent())
					.setDataSource(models)
					// .columns(dtProcessoColumn, tempReatorColumn,
					// setPointReatorColumn, tempCaldeiraColumn,
					// setPointCaldeiraColumn)
					.summary(ComparativoReportTemplate.createEmissaoComponent())
					.pageFooter(ComparativoReportTemplate.footerComponent).toPdf(pdfExporter);
			return 1;
		} catch (DRException e) {
			e.printStackTrace();
		}
		return 0;
	}

}