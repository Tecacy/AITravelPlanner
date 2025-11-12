import apiClient from './api';

import type { ApiResponse } from '@/types/api';

interface VoiceTranscriptionResponse {
  text: string;
}

export const transcribeVoice = async (audio: Blob) => {
  const formData = new FormData();
  formData.append('file', audio, 'speech.webm');
  const res = await apiClient.post<ApiResponse<VoiceTranscriptionResponse>>('/voice/transcribe', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return res.data.text;
};

