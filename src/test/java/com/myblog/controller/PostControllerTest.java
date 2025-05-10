package com.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.domain.Post;
import com.myblog.repository.PostRepository;
import com.myblog.request.PostCreate;
import com.myblog.request.PostEdit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE post AUTO_INCREMENT = 1").executeUpdate();
    }

    @Test
    @DisplayName("/posts リクエストに成功")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("タイトルです。")
                .content("内容です。")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);// Object -> Json;

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts リクエストする時にタイトルは必須")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("内容です。")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);// Object -> Json;

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400")) // json data validation
                .andExpect(jsonPath("$.message").value("不正なリクエストです。"))
                .andExpect(jsonPath("$.validation.title").value("タイトルを入力してください。"))
                .andDo(print());
    }

    @Test
    @DisplayName("/postsリクエストするとDBに保存される")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("タイトルです。")
                .content("内容です。")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);// Object -> Json;

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("タイトルです。", post.getTitle());
        assertEquals("内容です。", post.getContent());
    }

    @Test
    @DisplayName("投稿を1件取得する")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("newContent")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("newContent"))

                .andDo(print());
    }

    @Test
    @DisplayName("投稿を複数件取得する")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i-> {
                    return Post.builder()
                            .title("blog title " + i)
                            .content("blog content " + i)
                            .build();
                })
                .toList();
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title", is("blog title 19")))
                .andExpect(jsonPath("$[0].content", is("blog content 19")))
                .andDo(print());
    }

    @Test
    @DisplayName("ページを0でリクエストすると、最初のページが取得される。")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i-> {
                    return Post.builder()
                            .title("blog title " + i)
                            .content("blog content " + i)
                            .build();
                })
                .toList();
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title", is("blog title 19")))
                .andExpect(jsonPath("$[0].content", is("blog content 19")))
                .andDo(print());
    }

    @Test
    @DisplayName("投稿のタイトルを編集")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("blog title")
                .content("blog content")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("newTitle")
                .content("blog content")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("投稿を削除")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("blog title")
                .content("blog content")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("存在しない投稿の取得")
    void test9() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("存在しない投稿の編集")
    void test10() throws Exception {
        PostEdit postEdit = PostEdit.builder()
                .title("newTitle")
                .content("blog content")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("タイトルに’NG’を含めることはできない")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("NG")
                .content("内容です。")
                .build();

        String json = objectMapper.writeValueAsString(request);// Object -> Json;

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}