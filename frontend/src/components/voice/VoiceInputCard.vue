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
    <transition name="fade">
      <div v-if="transcript" class="voice-card__result">
        <el-input
          v-model="transcript"
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 6 }"
          placeholder="语音识别结果将在此处显示，可在确认前进行修改"
        />
        <div class="voice-card__actions">
          <el-button @click="handleCancel">清除</el-button>
          <el-button type="primary" @click="handleConfirm" :disabled="!transcript.trim()">
            确认使用
          </el-button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { onBeforeUnmount, ref } from 'vue';

import { Microphone } from '@element-plus/icons-vue';

import { transcribeVoice } from '@/services/voiceService';

const emit = defineEmits<{
  (event: 'confirmed', value: string): void;
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
      releaseStream(stream);
    };
  } catch (error) {
    ElMessage.error('无法访问麦克风，请检查权限设置');
  }
};

const releaseStream = (stream: MediaStream) => {
  stream.getTracks().forEach((track) => track.stop());
};

const stopRecording = () => {
  recording.value = false;
  mediaRecorder?.stop();
  mediaRecorder = null;
};

const sendToBackend = async (blob: Blob) => {
  try {
    const text = await transcribeVoice(blob);
    transcript.value = text;
    ElMessage.success('语音识别完成，请确认结果');
  } catch (error) {
    ElMessage.error((error as Error).message || '语音识别失败，请稍后再试');
  }
};

const handleConfirm = () => {
  if (!transcript.value.trim()) {
    ElMessage.warning('请确认识别文本后再使用');
    return;
  }
  emit('confirmed', transcript.value.trim());
  ElMessage.success('已使用语音识别内容');
  transcript.value = '';
};

const handleCancel = () => {
  transcript.value = '';
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

.voice-card__result {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.voice-card__actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
