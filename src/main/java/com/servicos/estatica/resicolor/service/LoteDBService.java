package com.servicos.estatica.resicolor.service;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.model.Produto;

public interface LoteDBService {

	public List<Produto> findEmAndamento();
	
	public List<Produto> findRecentes();

	public Produto findById(Long id);

	public boolean isLoteExists(int lote);

	public Produto findByLote(int lote);
	
	public void cancelaProcesso(Date dt, int lote);

}
