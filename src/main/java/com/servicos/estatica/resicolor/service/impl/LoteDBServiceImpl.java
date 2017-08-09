package com.servicos.estatica.resicolor.service.impl;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.dao.LoteDAO;
import com.servicos.estatica.resicolor.dao.impl.LoteDAOImpl;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.service.LoteDBService;

public class LoteDBServiceImpl implements LoteDBService {

	private LoteDAO dao = new LoteDAOImpl();

	@Override
	public boolean isLoteExists(int lote) {
		return dao.isLoteExists(lote);
	}

	@Override
	public Produto findByLote(int lote) {
		return dao.findByLote(lote);
	}

	@Override
	public Produto findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<Produto> findEmAndamento() {
		return dao.findEmAndamento();
	}

	@Override
	public List<Produto> findRecentes() {
		return dao.findRecentes();
	}

	@Override
	public void cancelaProcesso(Date dt, int lote) {
		dao.cancelaProcesso(dt, lote);
	}

}
