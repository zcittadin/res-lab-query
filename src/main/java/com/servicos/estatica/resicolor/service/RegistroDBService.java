package com.servicos.estatica.resicolor.service;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.model.Processo;

public interface RegistroDBService {

	public void saveProcesso(Processo registro);

	public List<Processo> findByLote(int lote);
	
	public List<Date> getPeriodo(int lote);
}
