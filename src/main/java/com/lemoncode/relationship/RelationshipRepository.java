package com.lemoncode.relationship;


import com.lemoncode.person.Person;
import com.lemoncode.person.Person_;
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

    public List<Relationship> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Relationship> q = cb.createQuery(Relationship.class);
        // set the root class
        Root<Relationship> root = q.from(Relationship.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Relationship_.hasUpdate)), cb.asc(root.get(Relationship_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }


    @Transactional
    public Relationship save(Relationship rel) {
        if (rel.getId() == null) {
            this.entityManager.persist(rel);
            return rel;
        } else {
            return this.entityManager.merge(rel);
        }
    }

    @Transactional
    public int deleteById(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Relationship> delete = cb.createCriteriaDelete(Relationship.class);
        // set the root class
        Root<Relationship> root = delete.from(Relationship.class);
        // set where clause
        delete.where(cb.equal(root.get(Relationship_.id), id));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }


    public List<Relationship> findByPersonId(int id) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Relationship> query = cb.createQuery(Relationship.class);
            // set the root class
            Root<Relationship> supplierRoot = query.from(Relationship.class);

            Join<Relationship, Person> person1 = supplierRoot.join(Relationship_.person1);
            Join<Relationship, Person> person2 = supplierRoot.join(Relationship_.person2);
            query
                    .select(supplierRoot)
                    .where(
                            cb.or(
                                    cb.equal(person1.get(Person_.id), id),
                                    cb.equal(person2.get(Person_.id), id)
                            )
                    );

            //perform query
            return this.entityManager.createQuery(query).getResultList();

        } catch (Exception e) {
            return null;
        }
    }
}