<template>
  <div>
    <h1>图片识别</h1>
    <el-upload
        class="upload-demo"
        action="https://fake-url.com/image-upload"
        :on-success="handleSuccess"
        list-type="picture">
      <el-button size="small" type="primary">上传图片</el-button>
    </el-upload>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  setup() {
    function handleSuccess(response: any) {
      console.log('Image recognition results:', response)
      // 显示识别结果
    }

    return { handleSuccess }
  }
})
</script>
