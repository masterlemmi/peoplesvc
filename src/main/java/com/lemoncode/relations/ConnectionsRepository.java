package com.lemoncode.relations;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConnectionsRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Connections> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Connections> q = cb.createQuery(Connections.class);
        // set the root class
        Root<Connections> root = q.from(Connections.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Connections_.hasUpdate)), cb.asc(root.get(Connections_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }

    @Transactional
    public Connections save(Connections Connections) {
        if (Connections.getId() == null) {
            this.entityManager.persist(Connections);
            return Connections;
        } else {
            return this.entityManager.merge(Connections);
        }
    }


    @Transactional
    public int deleteById(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Connections> delete = cb.createCriteriaDelete(Connections.class);
        // set the root class
        Root<Connections> root = delete.from(Connections.class);
        // set where clause
        delete.where(cb.equal(root.get(Connections_.id), id));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }

    public Connections find(Long source, Long targe){
        try {
            this.entityManager.clear();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Connections> query = cb.createQuery(Connections.class);
            // set the root class


            Root<Connections> root = query.from(Connections.class);

            query.where(cb.equal(root.get(Connections_.source), source))
                    .where(cb.equal(root.get(Connections_.target), targe));
            //perform query
            return this.entityManager.createQuery(query)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Connections findById(long id) {
        try {
            this.entityManager.clear();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Connections> query = cb.createQuery(Connections.class);
            // set the root class


            Root<Connections> root = query.from(Connections.class);

            query.where(cb.equal(root.get(Connections_.id), id));
            //perform query
            return this.entityManager.createQuery(query)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int deleteAll() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Connections> delete = cb.createCriteriaDelete(Connections.class);
        // set the root class
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }
}