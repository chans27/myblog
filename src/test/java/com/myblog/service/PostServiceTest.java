package com.myblog.service;

import com.myblog.domain.Post;
import com.myblog.repository.PostRepository;
import com.myblog.request.PostCreate;
import com.myblog.request.PostEdit;
import com.myblog.request.PostSearch;
import com.myblog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        postRepository.save(requestPost);

        // when
        PostResponse postResponse = postService.getOnePost(requestPost.getId());

        // then
        assertNotNull(postResponse);
        assertEquals(1L, postRepository.count());
        assertEquals("newTitle", postResponse.getTitle());
        assertEquals("newContent", postResponse.getContent());
    }

    @Test
    @DisplayName("글 1page 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(1, 20)
                        .mapToObj(i-> {
                            return Post.builder()
                                    .title("blog title " + i)
                                    .content("blog content " + i)
                                    .build();
                        })
                                .toList();
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("blog title 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
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

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id=" + post.getId()));
        assertEquals("newTitle", changedPost.getTitle());
        assertEquals("blog content", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("blog title")
                .content("blog content")
                .build();
        postRepository.save(post);


        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("newContent")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id=" + post.getId()));
        assertEquals("blog title", changedPost.getTitle());
        assertEquals("newContent", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("blog title")
                .content("blog content")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }
}