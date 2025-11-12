<template>
  <div class="voice-card panel">
    <h3>语音助手</h3>
    <p>按下按钮，描述你的旅行需求或记账信息。</p>
    <el-button
      type="primary"
      size="large"
      :loading="recording"
      @click="toggleRecording"
    >
      <el-icon><Microphone /></el-icon>
      <span>{{ recording ? '录音中...' : '开始语音输入' }}</span>
    </el-button>
    <p v-if="transcript" class="voice-card__transcript">
      <strong>识别结果：</strong>{{ transcript }}
    </p>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { onBeforeUnmount, ref } from 'vue';

import { Microphone } from '@element-plus/icons-vue';

const emit = defineEmits<{
  (event: 'transcribed', value: string): void;
}>();

const recording = ref(false);
const transcript = ref('');
let mediaRecorder: MediaRecorder | null = null;
let audioChunks: BlobPart[] = [];

const toggleRecording = async () => {
  if (recording.value) {
    stopRecording();
    return;
  }
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    audioChunks = [];
    mediaRecorder = new MediaRecorder(stream);
    mediaRecorder.start();
    recording.value = true;

    mediaRecorder.ondataavailable = (event) => {
      audioChunks.push(event.data);
    };
    mediaRecorder.onstop = async () => {
      if (!audioChunks.length) {
        return;
      }
      const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });
      await sendToBackend(audioBlob);
    };
  } catch (error) {
    ElMessage.error('无法访问麦克风，请检查权限设置');
  }
};

const stopRecording = () => {
  recording.value = false;
  mediaRecorder?.stop();
  mediaRecorder = null;
};

const sendToBackend = async (blob: Blob) => {
  try {
    // TODO: 调用后端语音识别接口
    transcript.value = '（示例）语音识别结果文本';
    emit('transcribed', transcript.value);
    ElMessage.success('语音识别示例完成');
  } catch (error) {
    ElMessage.error('语音识别失败，请稍后再试');
  }
};

onBeforeUnmount(() => {
  if (recording.value) {
    stopRecording();
  }
});
</script>

<style scoped>
.voice-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.voice-card__transcript {
  margin: 0;
  color: #4a5568;
}
</style>


