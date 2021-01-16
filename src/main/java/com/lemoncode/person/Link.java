package com.lemoncode.person;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String name;
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String url;
    @ManyToOne
    private Person person;

}
