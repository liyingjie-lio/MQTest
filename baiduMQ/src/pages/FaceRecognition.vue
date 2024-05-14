<template>
  <div>
    <h1>人脸识别</h1>
    <el-upload
        class="upload-demo"
        action="https://fake-url.com/face-upload"
        :on-success="handleSuccess"
        list-type="picture-card">
      <el-button size="small" type="primary">上传人脸图片</el-button>
    </el-upload>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  setup() {
    function handleSuccess(response: any) {
      console.log('Face recognition results:', response)
      // 显示人脸识别结果
    }

    return { handleSuccess }
  }
})
</script>
