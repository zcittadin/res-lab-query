package com.servicos.estatica.resicolor.service.impl;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.dao.RegistroDAO;
import com.servicos.estatica.resicolor.dao.impl.RegistroDAOImpl;
import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.service.RegistroDBService;

public class RegistroDBServiceImpl implements RegistroDBService {

	private RegistroDAO dao = new RegistroDAOImpl();

	@Override
	public void saveProcesso(Processo registro) {
		dao.saveProcesso(registro);

	}

	@Override
	public List<Processo> findByLote(int lote) {
		return null;
	}

	public List<Date> getPeriodo(int lote) {
		return dao.getPeriodo(lote);
	}

}
