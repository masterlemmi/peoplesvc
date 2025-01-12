package com.lemmi.tools.people.spring;


import com.lemmi.tools.people.relationship.RelationshipRepository;
import com.lemmi.tools.people.person.PeopleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class AppInitializer implements ApplicationRunner {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AppInitializer.class);


    @Autowired
    private PeopleRepository repo;

    @Autowired
    private RelationshipRepository relRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
