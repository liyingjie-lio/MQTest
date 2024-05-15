<template>
  <div>
    <h1>语音识别</h1>
    <input type="file" @change="handleFileChange" accept="audio/*">
    <el-button @click="uploadVoice" :disabled="!audioFile">上传音频</el-button>
    <el-button @click="startPolling" :disabled="!requestId">获取识别结果</el-button>
    <div v-if="result">
      <h3>识别结果：</h3>
      <p>{{ resultText }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import myAxios from "../plugins/myAxios.ts";

export default {
  setup() {
    const audioFile = ref<File | null>(null);
    const requestId = ref<string | null>(null);
    const result = ref<any>(null);
    const resultText = ref<string>('');
    let pollInterval = null;

    const handleFileChange = (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (target.files) {
        audioFile.value = target.files[0];
      }
    };

    const uploadVoice = async () => {
      if (audioFile.value) {
        const formData = new FormData();
        formData.append('file', audioFile.value);

        try {
          const response = await myAxios.post('/voice', formData);
          requestId.value = response.data; // 服务器返回的请求ID
          console.log('请求ID:', requestId.value);
          alert('音频上传成功，请求ID已接收！');
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
            const response = await myAxios.get(`/voice/${requestId.value}`);
            if (response.data && response.data.result) {
              result.value = response.data;
              resultText.value = result.value.result.join(' '); // 将结果数组合并为单个字符串
              clearInterval(pollInterval);
            }
          } catch (error) {
            console.error('获取结果失败:', error);
            clearInterval(pollInterval);
          }
        }, 2000); // 每2秒轮询一次
      }
    };

    return { audioFile, requestId, result, resultText, handleFileChange, uploadVoice, startPolling };
  },
};
</script>
