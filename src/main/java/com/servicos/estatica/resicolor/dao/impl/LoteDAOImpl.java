package com.servicos.estatica.resicolor.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.servicos.estatica.resicolor.dao.LoteDAO;
import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.util.HibernateUtil;

public class LoteDAOImpl implements LoteDAO {

	@SuppressWarnings("unchecked")
	@Override
	public boolean isLoteExists(int lote) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Lote p WHERE p.lote = " + lote;
		Query query = session.createQuery(hql);
		List<Produto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		if (list.isEmpty())
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Produto findByLote(int lote) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Produto p WHERE p.lote = " + lote;
		Query query = session.createQuery(hql);
		List<Produto> list = query.getResultList();
		if (list.isEmpty())
			return null;
		Produto p = list.get(0);
		hql = "SELECT p FROM Processo p  WHERE p.produto = " + p.getId();
		query = session.createQuery(hql);
		List<Processo> lista = query.getResultList();
		p.setProcessos(lista);
		session.close();
		return p;
	}

	@Override
	public Produto findById(Long id) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		// String consulta = "SELECT p FROM Produto p WHERE p.id = :id";
		session.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> findEmAndamento() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Produto p WHERE p.dtFinal IS NULL AND p.dtInicial IS NOT NULL";
		Query query = session.createQuery(hql);
		List<Produto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> findRecentes() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Produto p WHERE p.dtFinal IS NOT NULL ORDER BY p.id DESC";
		Query query = session.createQuery(hql);
		query.setMaxResults(6);
		List<Produto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unused")
	@Override
	public void cancelaProcesso(Date dt, int lote) {
		int rowCount = 0;
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "UPDATE Produto SET dtFinal = :dt  WHERE lote = :lote";
		Query query = session.createQuery(hql);
		query.setParameter("dt", dt);
		query.setParameter("lote", lote);
		rowCount = query.executeUpdate();
        tx.commit();
		session.close();
	}
}
