package com.lemoncode.util;

import com.lemoncode.person.PeopleRepository;
import com.lemoncode.relationship.RelationshipRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class InitLoader {

    private final PeopleRepository repository;
    private final RelationshipRepository relrepo;

    public InitLoader(PeopleRepository repository, RelationshipRepository relRepo) {
        this.repository = repository;
        this.relrepo = relRepo;
    }

    public void loadInitialPeople() {
        System.out.println("Loading Initial Person List");


        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream in = classLoader.getResourceAsStream("data.csv");
            Reader reader = new InputStreamReader(in);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMockdata() {

        System.out.println("Loading Mock Data");
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream in = classLoader.getResourceAsStream("mockdata.csv");
            Reader reader = new InputStreamReader(in);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            int count = 0;
            for (CSVRecord record : records) {

                if (count++ == 20) break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
