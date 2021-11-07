package com.lemoncode.dfs;

import com.lemoncode.descendants.DescendantDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {
    private DescendantDTO dto;
    private int index;
}