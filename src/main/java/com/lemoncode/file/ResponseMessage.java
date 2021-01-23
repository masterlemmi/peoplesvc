package com.lemoncode.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMessage<T> {
    private String message;
    private T data;

    public ResponseMessage(String msg) {
        this.message = msg;
    }


}