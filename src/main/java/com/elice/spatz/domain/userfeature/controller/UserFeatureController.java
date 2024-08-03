package com.elice.spatz.domain.userfeature.controller;

import com.elice.spatz.domain.user.service.UserService;
import com.elice.spatz.domain.userfeature.model.dto.request.BlockCreateDto;
import com.elice.spatz.domain.userfeature.model.dto.request.FriendRequestCreateDto;
import com.elice.spatz.domain.userfeature.model.dto.response.BlockDto;
import com.elice.spatz.domain.userfeature.model.dto.response.FriendDto;
import com.elice.spatz.domain.userfeature.model.dto.response.FriendRequestDto;
import com.elice.spatz.domain.userfeature.service.UserFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserFeatureController {
    private final UserFeatureService userFeatureService;
    private final UserService  userService;

    // 1. 차단 요청
    @PostMapping("/block")
    public ResponseEntity<String> createBlock(@RequestBody BlockCreateDto blockCreateDto){
        userFeatureService.createBlock(blockCreateDto);
        return ResponseEntity.ok("차단이 완료되었습니다.");
    }
    // 2. 차단 조회
    @GetMapping("/blocks")
    public ResponseEntity<Page<BlockDto>> getBlocks(@RequestParam long blockerId , @PageableDefault(page=0, size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BlockDto> blocks = userFeatureService.getBlocks(blockerId, pageable);
        return ResponseEntity.ok(blocks);
    }
    // 3. 차단 해제
    @PatchMapping("/unblock")
    public ResponseEntity<String> unBlock(@RequestParam long blockId){
        userFeatureService.unBlock(blockId);
        return ResponseEntity.ok("차단 해제가 완료되었습니다.");
    }

    // 1. 친구 요청
    @PostMapping("/friend-request")
    public ResponseEntity<String> createFriendRequest(@RequestBody FriendRequestCreateDto friendRequestCreateDto){
        userFeatureService.createFriendRequest(friendRequestCreateDto);
        return ResponseEntity.ok("친구 요청이 완료되었습니다.");
    }
    // 2. 보낸/받은 요청 조회
    @GetMapping("/friend-requests")
    public ResponseEntity<Page<FriendRequestDto>> getSentFriendRequests(@RequestParam String status, @RequestParam long userId, @PageableDefault(page=0, size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<FriendRequestDto> friendRequests = userFeatureService.getFriendRequests(status, userId, pageable);
        return ResponseEntity.ok(friendRequests);
    }
    // 3. 받은 요청 응답
    @PatchMapping("/friend-request")
    public ResponseEntity<String> responseReceivedFriendRequest(@RequestParam long friendRequestId, @RequestParam String status){
        userFeatureService.responseReceivedFriendRequest(friendRequestId, status);
        return ResponseEntity.ok("받은 요청에 대한 응답이 완료되었습니다.");
    }
    // 4. 보낸 요청 삭제
    @DeleteMapping("/friend-request")
    public ResponseEntity<String> deleteSentFriendRequest(@RequestParam long friendRequestId){
        userFeatureService.deleteSentFriendRequest(friendRequestId);
        return ResponseEntity.ok("보낸 요청이 삭제되었습니다.");
    }

    // 1. 친구 조회
    @GetMapping("/friendships")
    public ResponseEntity<Page<FriendDto>> getFriendships(@RequestParam long userId, @PageableDefault(page=0, size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<FriendDto> friendDtos = userFeatureService.getFriendShips(userId, pageable);
        return ResponseEntity.ok(friendDtos);
    }
    // 2. 친구 검색 조회
    @GetMapping("/friendships/keyword")
    public ResponseEntity<Page<FriendDto>> getFriendshipsByKeyword(@RequestParam String keyword, @RequestParam long userId, @PageableDefault(page=0, size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FriendDto> friendDtosByKeyword = userFeatureService.getFriendshipsByKeyword(keyword, userId, pageable);
        return ResponseEntity.ok(friendDtosByKeyword);
    }
    // 3. 친구 해제
    @PatchMapping("/un-friendship")
    public ResponseEntity<String> deleteFriendship(@RequestParam long friendshipId){
        userFeatureService.deleteFriendShip(friendshipId);
        return ResponseEntity.ok("친구 삭제가 완료되었습니다.");
    }
}
