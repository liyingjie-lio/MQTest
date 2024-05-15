<template>
  <div>
    <h1>图片识别</h1>
    <input type="file" @change="handleFileChange" accept="image/*">
    <el-button @click="uploadImage" :disabled="!imageFile">上传图片</el-button>
    <el-button @click="startPolling" :disabled="!requestId">获取识别结果</el-button>
    <div v-if="result">
      <h3>识别结果：</h3>
      <p>识别到 {{ result.result_num }} 项:</p>
      <ul>
        <li v-for="(item, index) in result.result" :key="index">
          <strong>{{ item.keyword }}</strong> ({{ item.root }}) - 置信度: {{ item.score.toFixed(3) }}
        </li>
      </ul>
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
          const response = await myAxios.post('/upload', formData);
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
            const response = await myAxios.get(`/upload/${requestId.value}`);
            if (response.data && response.data.result && response.data.result.length > 0) {
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
