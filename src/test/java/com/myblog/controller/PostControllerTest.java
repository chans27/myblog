package com.myblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest // MockMvc Bean 주입
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 Hello World 출력")
    void test() throws Exception {
        // 글 제목, 글 내용

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "글 제목")
                        .param("content", "글 내용")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수")
    void test2() throws Exception {
        // 글 제목, 글 내용

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

}