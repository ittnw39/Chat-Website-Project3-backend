package com.elice.spatz.domain.userfeature.service;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.userfeature.model.dto.request.BlockCreateDto;
import com.elice.spatz.domain.userfeature.model.dto.request.FriendRequestCreateDto;
import com.elice.spatz.domain.userfeature.model.dto.request.RequestMapper;
import com.elice.spatz.domain.userfeature.model.dto.response.BlockDto;
import com.elice.spatz.domain.userfeature.model.dto.response.FriendRequestDto;
import com.elice.spatz.domain.userfeature.model.dto.response.ResponseMapper;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import com.elice.spatz.domain.userfeature.model.entity.Friendship;
import com.elice.spatz.domain.userfeature.model.entity.Status;
import com.elice.spatz.domain.userfeature.repository.BlockRepository;
import com.elice.spatz.domain.userfeature.repository.FriendRequestRepository;
import com.elice.spatz.domain.userfeature.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.elice.spatz.domain.userfeature.model.entity.Status.ACCEPTED;
import static com.elice.spatz.domain.userfeature.model.entity.Status.REJECTED;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class UserFeatureService {
    private final BlockRepository blockRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    // 1. 차단 요청
    @Transactional
    public void createBlock(BlockCreateDto blockCreateDto){
        Block newBlock = requestMapper.blockCreateDtoToBlock(blockCreateDto);
        blockRepository.save(newBlock);
    }
    // 2. 차단 조회
    @Transactional
    public Page<BlockDto> getBlocks(long blockerId, Pageable pageable){
        Page<Block> blocks = blockRepository.findAllByBlockerIdAndBlockStatusIsTrue(blockerId, pageable);
        List<BlockDto> blockDtoList = new ArrayList<>();

        for(Block block : blocks) {
            BlockDto blockDto = responseMapper.blockToBlockDto(block);
            blockDtoList.add(blockDto);
        }
        return new PageImpl<>(blockDtoList, pageable, blocks.getTotalElements());
    }
    // 3. 차단 해제
    @Transactional
    public void unBlock(long id) {
        Block newBlock = blockRepository.findById(id).orElseThrow();
        newBlock.setBlockStatus(false);
        blockRepository.save(newBlock);
    }

    // 1. 친구 요청
    @Transactional
    public void createFriendRequest(FriendRequestCreateDto friendRequestCreateDto){
        FriendRequest friendRequest = requestMapper.friendRequestCreateDtoToFriendRequest(friendRequestCreateDto);
        friendRequestRepository.save(friendRequest);
    }
    // 2. 보낸/받은 요청 조회
    @Transactional
    public Page<FriendRequestDto> getFriendRequests(String status, long userId, Pageable pageable){
        Page<FriendRequest> friendRequests;

        if (status.equals("sent")){
            friendRequests = friendRequestRepository.findAllByRequesterId(userId, pageable);
        } else if (status.equals("received")){
            friendRequests = friendRequestRepository.findAllByRecipientId(userId, pageable);
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        List<FriendRequestDto> friendRequestDtoList = friendRequests.getContent().stream()
                .map(responseMapper::friendRequestToFriendRequestDto)
                .collect(Collectors.toList());

        return new PageImpl<>(friendRequestDtoList, pageable, friendRequests.getTotalElements());
    }
    // 3. 받은 요청 응답
    @Transactional
    public void responseReceivedFriendRequest(long id, String status){
        FriendRequest receivedFriendRequest = friendRequestRepository.findById(id).orElseThrow();
        if(status.equals("ACCEPTED")){
            receivedFriendRequest.setRequestStatus(ACCEPTED);
            Users requester = receivedFriendRequest.getRequester();
            Users recipient = receivedFriendRequest.getRecipient();

            Friendship friendship = new Friendship();
            friendship.setUser(requester);
            friendship.setFriend((recipient));
            friendship.setFriendStatus(TRUE);

            friendshipRepository.save(friendship);
        } else if (status.equals("REJECTED")){
            receivedFriendRequest.setRequestStatus(REJECTED);
        }
        else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        friendRequestRepository.save(receivedFriendRequest);
    }
    // 4. 보낸 요청 삭제
    @Transactional
    public void deleteSentFriendRequest(long id){
        friendRequestRepository.deleteById(id);
    }

    // 1. 친구 조회
    @Transactional
    public Page<Friendship> getFriendShips(long userId, Pageable pageable){
        return friendshipRepository.findAllByUserId(userId, pageable);
    }
    // 2. 친구 해제
    @Transactional
    public void deleteFriendShip(long id){
        Friendship friendship = friendshipRepository.findById(id).orElseThrow();
        friendship.setFriendStatus(false);
    }
}
