package com.lemoncode.descendants;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AncestryRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Ancestry> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Ancestry> q = cb.createQuery(Ancestry.class);
        // set the root class
        Root<Ancestry> root = q.from(Ancestry.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Ancestry_.hasUpdate)), cb.asc(root.get(Ancestry_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }

    @Transactional
    public Ancestry save(Ancestry Ancestry) {
        if (Ancestry.getId() == null) {
            this.entityManager.persist(Ancestry);
            return Ancestry;
        } else {
            return this.entityManager.merge(Ancestry);
        }
    }


    @Transactional
    public int deleteById(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Ancestry> delete = cb.createCriteriaDelete(Ancestry.class);
        // set the root class
        Root<Ancestry> root = delete.from(Ancestry.class);
        // set where clause
        delete.where(cb.equal(root.get(Ancestry_.id), id));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }

    public Ancestry findById(Long origin) {
        try {
            this.entityManager.clear();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Ancestry> query = cb.createQuery(Ancestry.class);
            // set the root class

            EntityGraph<Ancestry> entityGraph = entityManager.createEntityGraph(Ancestry.class);
            entityGraph.addAttributeNodes("descendants");

            Root<Ancestry> root = query.from(Ancestry.class);

            query.where(cb.equal(root.get(Ancestry_.id), origin));
            //perform query
            return this.entityManager.createQuery(query)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Ancestry findByLabel(String ancestryName) {
        try {
            this.entityManager.clear();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Ancestry> query = cb.createQuery(Ancestry.class);
            // set the root class

            EntityGraph<Ancestry> entityGraph = entityManager.createEntityGraph(Ancestry.class);
            entityGraph.addAttributeNodes("descendants");

            Root<Ancestry> root = query.from(Ancestry.class);

            query.where(cb.equal(root.get(Ancestry_.label), ancestryName));
            //perform query
            return this.entityManager.createQuery(query)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int deleteAll() {
        return this.entityManager.createQuery("DELETE FROM ANCESTRY").executeUpdate();
    }
}
