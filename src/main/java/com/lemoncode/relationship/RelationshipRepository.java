package com.lemoncode.relationship;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RelationshipRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Relations> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Relations> q = cb.createQuery(Relations.class);
        // set the root class
        Root<Relations> root = q.from(Relations.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Relationship_.hasUpdate)), cb.asc(root.get(Relationship_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }


    @Transactional
    public Relations save(Relations rel) {
        if (rel.getId() == null) {
            this.entityManager.persist(rel);
            return rel;
        } else {
            return this.entityManager.merge(rel);
        }
    }


}