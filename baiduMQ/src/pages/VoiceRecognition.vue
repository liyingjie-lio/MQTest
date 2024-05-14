<template>
  <div>
    <h1>语音识别</h1>
    <el-upload
        class="upload-demo"
        action="https://fake-url.com/upload"
        :on-success="handleSuccess"
        :before-upload="handleBeforeUpload">
      <el-button size="small" type="primary">点击上传音频文件</el-button>
    </el-upload>
    <el-button @click="startRecording">开始录音</el-button>
    <el-button @click="stopRecording" :disabled="!isRecording">停止录音</el-button>
    <audio v-if="audioUrl" :src="audioUrl" controls></audio>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

export default defineComponent({
  setup() {
    const audioUrl = ref<string | null>(null)
    const isRecording = ref(false)

    function handleBeforeUpload(file: File) {
      // 在这里可以添加文件类型检查逻辑
      return true
    }

    function handleSuccess(response: any, file: File) {
      console.log('Upload success:', response, file)
      // 这里可以处理从服务器返回的识别结果
    }

    function startRecording() {
      console.log('Start recording')
      isRecording.value = true
      // 这里添加开始录音的逻辑
    }

    function stopRecording() {
      console.log('Stop recording')
      isRecording.value = false
      // 这里添加停止录音并上传的逻辑
    }

    return { audioUrl, isRecording, handleBeforeUpload, handleSuccess, startRecording, stopRecording }
  }
})
</script>
