<script setup lang="ts">
import {defineProps, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter()

const post = ref({
  id: 0,
  title :"",
  content: "",
})

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
})

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {router.replace({name : "home"})
  })
}

</script>

<template>

  <el-row>
    <el-col>
      <div>
        <el-input v-model="post.title" />
      </div>
    </el-col>
  </el-row>

  <div>
    <div class="mt-2">
      <el-input v-model="post.content" type="textarea" rows="15" />
    </div>

    <el-row>
      <el-col>
        <div class="d-flex justify-content-end">
          <el-button type="warning" @click="edit()">Update</el-button>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<style>

</style>