<template>
    <el-header class="header">
    <!--      <RouterLink to="/">Home</RouterLink>-->
    <!--      <RouterLink to="/write">Write</RouterLink>-->
        <el-menu mode="horizontal" router>
          <el-menu-item index="/">Home</el-menu-item>
          <el-menu-item index="write">Write</el-menu-item>
        </el-menu>
    </el-header>
</template>

<script setup lang="ts"></script>

<style scoped>
  .header {
    padding: 0;
    height: 60px;
  }
</style>