package com.lemoncode.person;

import lombok.Data;

import java.util.List;

@Data
public class FamilyDTO {
    private List<SimplePersonDTO> parents;
    private List<SimplePersonDTO> children;
}
