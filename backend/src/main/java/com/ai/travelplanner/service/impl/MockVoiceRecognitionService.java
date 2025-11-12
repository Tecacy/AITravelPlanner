package com.ai.travelplanner.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ai.travelplanner.service.VoiceRecognitionService;

@Service
public class MockVoiceRecognitionService implements VoiceRecognitionService {

    @Override
    public String transcribe(byte[] audio, String mimeType) {
        return "（示例）语音识别结果：" + Instant.now();
    }
}

