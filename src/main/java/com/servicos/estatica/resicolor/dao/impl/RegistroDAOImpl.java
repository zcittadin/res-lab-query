package com.servicos.estatica.resicolor.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.dao.RegistroDAO;
import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.util.HibernateUtil;

public class RegistroDAOImpl implements RegistroDAO {

	@Override
	public void saveProcesso(Processo registro) {
	}

	@Override
	public List<Processo> findByLote(int lote) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Date> getPeriodo(int lote) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT proc.dtProcesso FROM VwProcesso proc WHERE proc.lote = " + lote;
		Query query = session.createQuery(hql);
		List<Date> list = query.getResultList();
		if (list.isEmpty()) {
			return null;
		}
		int listLenght = list.size();
		List<Date> dt = new ArrayList<>();
		dt.add(list.get(0));
		dt.add(list.get(listLenght - 1));
		return dt;
	}

}
