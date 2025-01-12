package com.lemmi.tools.people.dfs;

import com.lemmi.tools.people.descendants.DescendantDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {
    private DescendantDTO dto;
    private int index;
}