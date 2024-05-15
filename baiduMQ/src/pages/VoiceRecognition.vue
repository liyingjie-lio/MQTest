<template>
  <div>
    <h1>语音识别</h1>
    <el-upload
        class="upload-demo"
        action="https://fake-url.com/upload"
        :on-success="handleSuccess"
        :before-upload="handleBeforeUpload"
        :http-request="customUpload">
      <el-button size="small" type="primary">点击上传音频文件</el-button>
    </el-upload>
    <el-button @click="startRecording" :disabled="isRecording">开始录音</el-button>
    <el-button @click="stopRecording" :disabled="!isRecording">停止录音</el-button>
    <audio v-if="audioUrl" :src="audioUrl" controls></audio>
    <el-divider></el-divider>
    <div>结果展示</div>
    <el-card v-if="recognitionResult" class="result-card">
      <h2>识别结果</h2>
      <p>{{ recognitionResult }}</p>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { ElMessage } from 'element-plus'
import 'element-plus/es/components/message/style/css'

export default defineComponent({
  setup() {
    const audioUrl = ref<string | null>(null)
    const isRecording = ref(false)
    const recognitionResult = ref<string | null>(null)
    const mediaRecorder = ref<MediaRecorder | null>(null)
    let chunks: Blob[] = []

    function handleBeforeUpload(file: File) {
      if (!file.type.includes('audio/')) {
        ElMessage.error('请上传音频文件！');
        return false;
      }
      return true;
    }

    function handleSuccess(response: any, file: File) {
      recognitionResult.value = response.recognitionResult || '无法识别音频'
    }

    function customUpload(options: any) {
      const { onSuccess, onError, file, onProgress } = options;

      const formData = new FormData();
      formData.append('file', file);

      fetch(options.action, {
        method: 'POST',
        body: formData,
      }).then(response => response.json())
          .then(data => {
            onSuccess(data, file);
          })
          .catch(error => {
            onError(error);
            console.error('Upload failed:', error);
          });
    }

    function startRecording() {
      navigator.mediaDevices.getUserMedia({ audio: true })
          .then(stream => {
            mediaRecorder.value = new MediaRecorder(stream);
            mediaRecorder.value.start();
            mediaRecorder.value.ondataavailable = e => {
              chunks.push(e.data);
            };
            isRecording.value = true;
            ElMessage.info('录音开始');
          })
          .catch(err => {
            ElMessage.error('录音失败: ' + err.message);
          });
    }

    function stopRecording() {
      if (mediaRecorder.value) {
        mediaRecorder.value.stop();
        mediaRecorder.value.onstop = () => {
          const blob = new Blob(chunks, { type: 'audio/wav' });
          chunks = [];
          audioUrl.value = URL.createObjectURL(blob);
          customUpload({
            action: 'https://fake-url.com/upload',
            file: blob,
            onSuccess: handleSuccess,
            onError: error => console.error('Upload error:', error),
          });
          isRecording.value = false;
          ElMessage.success('录音结束');
        };
      }
    }

    return { audioUrl, isRecording, recognitionResult, handleBeforeUpload, handleSuccess, startRecording, stopRecording, customUpload }
  }
})
</script>

<style scoped>
.result-card {
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
