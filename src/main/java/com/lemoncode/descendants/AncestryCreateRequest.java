package com.lemoncode.descendants;

import lombok.Data;

@Data
public class AncestryCreateRequest {
    private String label;
    private Long ancestorId;
}
