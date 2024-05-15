<template>
  <div>
    <h1>证件验证</h1>
    <el-upload
        class="upload-demo"
        :http-request="handleUpload"
        list-type="picture">
      <el-button size="small" type="primary">上传证件图片</el-button>
    </el-upload>
    <div v-if="ocrResult">
      <h2>OCR 结果</h2>
      <p><strong>姓名:</strong> {{ ocrResult['姓名'].words }}</p>
      <p><strong>民族:</strong> {{ ocrResult['民族'].words }}</p>
      <p><strong>住址:</strong> {{ ocrResult['住址'].words }}</p>
      <p><strong>公民身份号码:</strong> {{ ocrResult['公民身份号码'].words }}</p>
      <p><strong>出生:</strong> {{ ocrResult['出生'].words }}</p>
      <p><strong>性别:</strong> {{ ocrResult['性别'].words }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import axios from 'axios';

export default defineComponent({
  setup() {
    const ocrResult = ref<any>(null);

    function handleUpload(options: any) {
      const { file, onSuccess, onError } = options;

      const formData = new FormData();
      formData.append('file', file);

      axios.post('/your-backend-endpoint', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
          .then(response => {
            console.log('Upload Response:', response.data);
            ocrResult.value = response.data.words_result; // 假设后端返回这个格式
            onSuccess(response.data);
          })
          .catch(error => {
            console.error('Error during upload request:', error);
            onError(error);
          });
    }

    return { handleUpload, ocrResult };
  }
});
</script>

