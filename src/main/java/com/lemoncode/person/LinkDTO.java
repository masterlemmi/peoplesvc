package com.lemoncode.person;

import lombok.*;
import org.apache.commons.text.WordUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String url;

    public void setName(String name) {
        if (name != null) {
            this.name = WordUtils.capitalizeFully((name.toLowerCase()));
        }
    }
}
