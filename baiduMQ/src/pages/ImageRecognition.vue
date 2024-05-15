<template>
  <div>
    <h1>图片识别</h1>
    <input
        type="file"
        id="id"
        name="image"
        class="getImgUrl_file"
        @change="shangc($event)"
        accept="image/jpg, image/jpeg, image/png"
    />
    <br />
    <img :src="picPath" alt="">

    <h4>识别结果:</h4>
    <table>
      <tr>
        <th>物品名称</th>
        <th>所属类目</th>
        <th>识别度</th>
      </tr>
<!--      <tr v-if="data" v-for="item in data">-->
<!--        <td>{{ item.keyword }}</td>-->
<!--        <td>{{ item.root }}</td>-->
<!--        <td>{{ item.score }}</td>-->
<!--      </tr>-->
    </table>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import axios from 'axios';

export default defineComponent({
  data() {
    return {
      data: "",
      picPath: ""
    };
  },
  methods: {
    async shangc(e: Event) {
      let file = (e.target as HTMLInputElement).files![0];
      if (!file) {
        return false;
      }

      let fileSize = file.size;
      if (fileSize > 10 * 1024 * 1024) {
        alert("文件大小不能大于10M！");
        (e.target as HTMLInputElement).value = "";
        return false;
      } else if (fileSize <= 0) {
        alert("文件大小不能为0M！");
        (e.target as HTMLInputElement).value = "";
        return false;
      }

      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = async (e) => {
        let imgFile = (e.target as FileReader).result as string;
        let arr = imgFile.split(",");
        this.picPath = 'data:image/png;base64,' + arr[1];

        const response = await axios.post("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general", {
          access_token: "获取到的access_token",
          image: arr[1]
        });
        this.data = response.data.result;
      };
    }
  }
});
</script>

<style scoped>
table, th, td {
  border: 1px solid orangered;
}
img {
  width: 400px;
  height: 400px;
  border: 1px solid red;
}
</style>
