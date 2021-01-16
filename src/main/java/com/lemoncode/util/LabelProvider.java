package com.lemoncode.util;

import com.lemoncode.person.GenderEnum;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

 class LabelProvider {

    private static LabelProvider INSTANCE = null;
    private final Map<String, String> labels = new HashMap<>();

    private LabelProvider() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream in = classLoader.getResourceAsStream("labels.csv");
            Reader reader = new InputStreamReader(in);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord record : records) {
                String label = record.get(0);
                String maleAdjacentLabel = record.get(1);
                String femAdjacentLabel = record.get(2);

                labels.put(label + GenderEnum.MALE, maleAdjacentLabel);
                labels.put(label + GenderEnum.FEMALE, femAdjacentLabel);
            }

        } catch (Exception e) {
            System.out.println("Error loading label resources");
        }
    }

     synchronized static LabelProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LabelProvider();
        }
        return INSTANCE;
    }


     String getOpposite(String label, GenderEnum gender) {
        return labels.get(label + gender);
    }


}