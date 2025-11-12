package com.ai.travelplanner.service;

public interface VoiceRecognitionService {

    String transcribe(byte[] audio, String mimeType);
}

