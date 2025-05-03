package com.myblog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}
