<template>
  <div>
    <h1>证件识别</h1>
    <input type="file" @change="handleFileChange" accept="image/*">
    <el-button @click="uploadImage" :disabled="!imageFile">上传图片</el-button>
    <el-button @click="startPolling" :disabled="!requestId">获取识别结果</el-button>
    <div v-if="result">
      <h3>识别结果：</h3>
      <p>姓名: {{ result.words_result.姓名.words }}</p>
      <p>性别: {{ result.words_result.性别.words }}</p>
      <p>民族: {{ result.words_result.民族.words }}</p>
      <p>出生日期: {{ result.words_result.出生.words }}</p>
      <p>住址: {{ result.words_result.住址.words }}</p>
      <p>公民身份号码: {{ result.words_result.公民身份号码.words }}</p>
      <p>证件状态: {{ result.image_status }}</p>
      <p>证件完整性: {{ result.card_quality.IsComplete ? '完整' : '不完整' }}</p>
      <p>遮挡检测: {{ result.card_quality.IsNoCover ? '无遮挡' : '有遮挡' }}</p>
      <p>清晰度: {{ result.card_quality.IsClear ? '清晰' : '不清晰' }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import myAxios from "../plugins/myAxios.ts";

export default {
  setup() {
    const imageFile = ref<File | null>(null);
    const requestId = ref<string | null>(null);
    const result = ref<any>(null);
    let pollInterval = null;

    const handleFileChange = (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (target.files) {
        imageFile.value = target.files[0];
      }
    };

    const uploadImage = async () => {
      if (imageFile.value) {
        const formData = new FormData();
        formData.append('file', imageFile.value);

        try {
          const response = await myAxios.post('/identity', formData);
          requestId.value = response.data; // 服务器返回的请求ID
          console.log('请求ID:', requestId.value);
          alert('图片上传成功，请求ID已接收！');
        } catch (error) {
          console.error('上传失败:', error);
          alert('上传失败，请重试！');
        }
      }
    };

    const startPolling = () => {
      if (requestId.value) {
        pollInterval = setInterval(async () => {
          try {
            const response = await myAxios.get(`/identity/${requestId.value}`);
            if (response.data && response.data.words_result_num > 0) {
              result.value = response.data;
              clearInterval(pollInterval);
            }
          } catch (error) {
            console.error('获取结果失败:', error);
            clearInterval(pollInterval);
          }
        }, 2000); // 每2秒轮询一次
      }
    };

    return { imageFile, requestId, result, handleFileChange, uploadImage, startPolling };
  },
};
</script>
