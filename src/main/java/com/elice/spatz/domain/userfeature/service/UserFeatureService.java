package com.elice.spatz.domain.userfeature.service;

import com.elice.spatz.domain.userfeature.model.dto.request.BlockCreateDto;
import com.elice.spatz.domain.userfeature.model.dto.request.RequestMapper;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class UserFeatureService {
    private final BlockRepository blockRepository;
    private final RequestMapper requestMapper;

    // 1. 차단 요청
    @Transactional
    public void createBlock(BlockCreateDto blockCreateDto){
        Block newBlock = requestMapper.blockCreateDtoToBlock(blockCreateDto);
        blockRepository.save(newBlock);
    }
    // 2. 차단 조회
    @Transactional
    public Page<Block> getBlocks(Pageable pageable, long blockerId){
        return blockRepository.findAllByBlockerIdAndBlockStatusIsTrue(blockerId, pageable);
    }
    // 3. 차단 해제
    @Transactional
    public void unBlock(long id) {
        Block newBlock = blockRepository.findById(id).orElseThrow();
        newBlock.setBlockStatus(false);
        blockRepository.save(newBlock);
    }
}
