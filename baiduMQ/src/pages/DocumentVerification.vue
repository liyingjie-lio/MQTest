<template>
  <div>
    <h1>证件验证</h1>
    <el-upload
        class="upload-demo"
        action="https://fake-url.com/document-upload"
        :on-success="handleSuccess"
        list-type="picture">
      <el-button size="small" type="primary">上传证件图片</el-button>
    </el-upload>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  setup() {
    function handleSuccess(response: any) {
      console.log('Document verification results:', response)
      // 显示证件验证结果
    }

    return { handleSuccess }
  }
})
</script>
