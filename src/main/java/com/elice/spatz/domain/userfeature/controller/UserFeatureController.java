package com.elice.spatz.domain.userfeature.controller;

import com.elice.spatz.domain.userfeature.model.dto.request.BlockCreateDto;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.service.UserFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserFeatureController {
    private final UserFeatureService userFeatureService;

    // 1. 차단 요청
    @PostMapping("/block")
    public ResponseEntity<String> createBlock(@RequestBody BlockCreateDto blockCreateDto){
        userFeatureService.createBlock(blockCreateDto);
        return ResponseEntity.ok("차단이 완료되었습니다.");
    }

    // 2. 차단 조회
    @GetMapping("/blocks")
    public ResponseEntity<Page<Block>> getBlocks(@RequestParam long blockerId , @PageableDefault(page=0, size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Block> blocks = userFeatureService.getBlocks(pageable, blockerId);
        return ResponseEntity.ok(blocks);
    }

    // 3. 차단 해제
    @PatchMapping("/unblock")
    public ResponseEntity<String> unBlock(@RequestParam long blockId){
        userFeatureService.unBlock(blockId);
        return ResponseEntity.ok("차단 해제가 완료되었습니다.");
    }
}
