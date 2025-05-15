<script setup lang="ts">
  import axios from "axios";
  import {ref} from "vue";
  import {useRouter} from "vue-router";

  interface Post {
    id: number;
    title: string;
    content: string;
  }

  const router = useRouter();
  const posts = ref<Post[]>([])

    axios.get("api/posts?page=1&size=5", ).then(response => {
      response.data.forEach((r: Post) => {
        posts.value.push(r);
      })
    })
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: {postId: post.id}}">{{post.title}}</router-link>
      </div>

      <div class="content">
        {{ post.content}}
      </div>

      <div class="sub d-flex">
        <div class="category">Develop</div>
        <div class="regDate">2024-01-01</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.15rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.8rem;
      margin-top: 8px;
      color: #7e7e7e;
    }

    li:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 10px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}
</style>