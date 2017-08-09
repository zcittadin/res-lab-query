package com.servicos.estatica.resicolor.util;

import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.model.Produto;

public class ProducaoUtil {

	public static String calculaProducao(String tempoProcesso, String quantidade) {
		String[] fields = tempoProcesso.split(":");
		Integer hours = Integer.parseInt(fields[0]);
		Integer minutes = Integer.parseInt(fields[1]);
		Integer passedMinutes = 0;
		Double producao = new Double(0);
		if (hours > 0) {
			passedMinutes = hours * 60;
			passedMinutes = passedMinutes + minutes;
			producao = (Double.parseDouble(quantidade.replace(",", ".")) / passedMinutes) * 60;
			String str = String.format("%1.2f", producao);
			producao = Double.valueOf(str.replace(",", "."));
			return producao.toString().replace(".", ",");
		} else {
			return "000,00";
		}
	}
	
	public static double getTempMin(Produto produto) {
		double min = 1000;
		for (Processo processo : produto.getProcessos()) {
			if (processo.getTempReator() < min) {
				min = processo.getTempReator();
			}
		}
		return min;
	}

	public static double getTempMax(Produto produto) {
		double max = 0;
		for (Processo processo : produto.getProcessos()) {
			if (processo.getTempReator() >= max) {
				max = processo.getTempReator();
			}
		}
		return max;
	}
}
