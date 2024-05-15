<!--<script setup lang="ts">-->
<!--import BasicLayout from "./layouts/BasicLayout.vue";-->
<!--</script>-->

<!--<template>-->
<!--    <BasicLayout />-->
<!--</template>-->

<!--<style>-->

<!--</style>-->

<template>
  <el-container>
    <el-aside width="200px">
      <el-menu>
        <el-menu-item index="1" @click="routeTo('/')">首页</el-menu-item>
        <el-menu-item index="2" @click="routeTo('/voice-recognition')">语音识别</el-menu-item>
        <el-menu-item index="3" @click="routeTo('/image-recognition')">图片识别</el-menu-item>
        <el-menu-item index="4" @click="routeTo('/face-recognition')">人脸识别</el-menu-item>
        <el-menu-item index="5" @click="routeTo('/document-verification')">证件验证</el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  setup() {
    const router = useRouter()
    function routeTo(path: string) {
      router.push(path)
    }
    return { routeTo }
  }
})
</script>