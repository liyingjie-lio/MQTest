// 定义路由
import Index from "../pages/Index.vue";
import DocumentVerification from "../pages/DocumentVerification.vue";
import FaceRecognition from "../pages/FaceRecognition.vue";
import ImageRecognition from "../pages/ImageRecognition.vue";
import VoiceRecognition from "../pages/VoiceRecognition.vue";

const routes = [
    { path: '/', component: Index },
    { path: '/voice-recognition', component: VoiceRecognition },
    { path: '/image-recognition', component: ImageRecognition },
    { path: '/face-recognition', component: FaceRecognition },
    { path: '/document-verification', component: DocumentVerification },
]

export default routes;