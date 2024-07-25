package com.elice.modulechat.service;

import com.elice.common.file.dto.FileUploadRequest;
import com.elice.modulefile.service.FileUploadService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {
    
    private final FileUploadService fileUploadService;
    
    public ChatRoomService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    public void createChatRoom() {
        // 채팅방 생성
        // 채팅방 정보 저장
        // 관련 파일을 저장
        FileUploadRequest fileUploadRequest = new FileUploadRequest();
        fileUploadService.uploadFile(fileUploadRequest);
    }
    
}
