package com.lemoncode.descendants;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DescendandListResponse {
    private List<DescendantDTO> descendants;
    private boolean success = true;
    private String message;

    public static DescendandListResponse ok(List<DescendantDTO> ancestry) {
        DescendandListResponse response = new DescendandListResponse();
        response.setDescendants(ancestry);
        return  response;
    }

    public static DescendandListResponse bad(String message) {
        DescendandListResponse response = new DescendandListResponse();
        response.setDescendants(new ArrayList<>());
        response.setSuccess(false);
        response.setMessage(message);
        return  response;
    }
}
