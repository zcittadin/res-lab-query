package com.servicos.estatica.resicolor.dao;

import java.util.Date;
import java.util.List;

import com.servicos.estatica.resicolor.model.Processo;

public interface RegistroDAO {

	public void saveProcesso(Processo registro);

	public List<Processo> findByLote(int lote);

	public List<Date> getPeriodo(int lote);
}
