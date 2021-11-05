package com.lemoncode.relations;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConnectionsSimpleDTO {

    private Long id;
    private String name;
    private boolean adopted; //TODO
    private List<ConnectionsSimpleDTO> parents = new ArrayList<>();
    private List<ConnectionsSimpleDTO> children;
}
