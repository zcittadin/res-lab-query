package com.servicos.estatica.resicolor.dao;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.model.Produto;

public interface LoteDAO {

	public boolean isLoteExists(int lote);

	public Produto findByLote(int lote);

	public Produto findById(Long id);
	
	public List<Produto> findEmAndamento();

	public List<Produto> findRecentes();
	
	public void cancelaProcesso(Date dt, int lote);

}
