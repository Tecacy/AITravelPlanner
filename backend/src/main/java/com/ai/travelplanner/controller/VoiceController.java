package com.ai.travelplanner.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ai.travelplanner.dto.ApiResponse;
import com.ai.travelplanner.dto.response.VoiceTranscriptionResponse;
import com.ai.travelplanner.service.VoiceRecognitionService;

@RestController
@Validated
@RequestMapping("/api/voice")
public class VoiceController {

    private final VoiceRecognitionService voiceRecognitionService;

    public VoiceController(VoiceRecognitionService voiceRecognitionService) {
        this.voiceRecognitionService = voiceRecognitionService;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<VoiceTranscriptionResponse>> transcribe(
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure("请上传有效的语音文件"));
        }
        try {
            String text = voiceRecognitionService.transcribe(file.getBytes(), file.getContentType());
            return ResponseEntity.ok(ApiResponse.success(new VoiceTranscriptionResponse(text)));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("语音识别失败：" + ex.getMessage()));
        }
    }
}

