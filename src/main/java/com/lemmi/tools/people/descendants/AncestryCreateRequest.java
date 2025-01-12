package com.lemmi.tools.people.descendants;

import lombok.Data;

@Data
public class AncestryCreateRequest {
    private String label;
    private Long ancestorId;
}
