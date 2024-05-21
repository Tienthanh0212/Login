package com.example.login.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private int code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    public BaseResponse(int status, String msg) {
        super();
        this.code = status;
        this.msg = msg;
    }

}