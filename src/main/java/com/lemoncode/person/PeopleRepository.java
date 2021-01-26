package com.lemoncode.person;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class PeopleRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Person> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Person> q = cb.createQuery(Person.class);
        // set the root class
        Root<Person> root = q.from(Person.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Person_.hasUpdate)), cb.asc(root.get(Person_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }

    public List<Person> findSome() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Person> q = cb.createQuery(Person.class);
        // set the root class
        Root<Person> root = q.from(Person.class);
        //perform query
//        q.orderBy(cb.desc(root.get(Person_.hasUpdate)), cb.asc(root.get(Person_.title)));

        return this.entityManager.createQuery(q).setFirstResult(0).setMaxResults(15)
                .getResultList();
    }

    public List<Person> findSiblings(int childId) {
        try {
            this.entityManager.clear();
            String sqlScript = "select distinct(c.child_id)  from children as c join children as c1 on (c1.parent_id  = c.parent_id and c1.child_id = " + childId + ")";
            Query q = entityManager.createNativeQuery(sqlScript);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Person> findChildren(Set<Person> parents) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            // set the root class
            Root<Person> parent = query.from(Person.class);
            SetJoin<Person, Person> children = parent.joinSet(Person_.CHILDREN);

            CriteriaBuilder.In<Long> inClause = cb.in(parent.get(Person_.id));
            for (Person p : parents) {
                inClause.value(p.getId());
            }


            query
                    .select(children)
                    .distinct(true)
                    .where(inClause);

            //perform query
            return this.entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public List<Person> findParents(Long childId) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            // set the root class
            Root<Person> parent = query.from(Person.class);
            SetJoin<Person, Person> children = parent.joinSet(Person_.CHILDREN);
            query
                    .select(parent)
                    .where(
                            cb.equal(children.get(Person_.id), childId)
                    );

            //perform query
            return this.entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }


    @Transactional
    public Person save(Person Person) {
        if (Person.getId() == null) {
            this.entityManager.persist(Person);
            return Person;
        } else {
            return this.entityManager.merge(Person);
        }
    }

    @Transactional
    public List<Person> save(List<Person> people) {

        int i = 0;
        for (Person p : people) {
            if (++i % 20 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            if (p.getId() == null) {
                this.entityManager.persist(p);
            } else {
                this.entityManager.merge(p);
            }
        }

        return people;
    }

    @Transactional
    public int deleteById(long id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Person> delete = cb.createCriteriaDelete(Person.class);
        // set the root class
        Root<Person> root = delete.from(Person.class);
        // set where clause
        delete.where(cb.equal(root.get(Person_.id), id));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }


    public Person findById(long id) {
        try {
            this.entityManager.clear();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            // set the root class

            EntityGraph<Person> entityGraph = entityManager.createEntityGraph(Person.class);
            entityGraph.addAttributeNodes("children");
            entityGraph.addAttributeNodes("relationships");


            Root<Person> root = query.from(Person.class);

            query.where(cb.equal(root.get(Person_.id), id));
            //perform query
            return this.entityManager.createQuery(query)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Person> search(String q) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            // set the root class
            Root<Person> root = query.from(Person.class);

            query.where(
                    cb.or(
                            cb.like(cb.upper(root.get(Person_.firstName)), "%" + q.toUpperCase() + "%"),
                            cb.like(cb.upper(root.get(Person_.lastName)), "%" + q.toUpperCase() + "%"),
                            cb.like(cb.upper(root.get(Person_.nickname)), "%" + q.toUpperCase() + "%")
                    ));

            //perform query
            return this.entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}