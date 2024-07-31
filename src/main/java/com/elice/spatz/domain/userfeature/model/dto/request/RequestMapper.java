package com.elice.spatz.domain.userfeature.model.dto.request;

import com.elice.spatz.domain.userfeature.model.dto.response.BlockDto;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import com.elice.spatz.domain.userfeature.model.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    // 차단
    @Mapping(source = "blockerId", target = "blocker.id")
    @Mapping(source = "blockedId", target = "blocked.id")
    Block blockCreateDtoToBlock(BlockCreateDto dto);

    // 친구 요청
    @Mapping(source = "requesterId", target = "requester.id")
    @Mapping(source = "recipientId", target = "recipient.id")
    FriendRequest friendRequestCreateDtoToFriendRequest(FriendRequestCreateDto dto);

    // 신고 요청
    @Mapping(source = "reporterId", target = "reporter.id")
    @Mapping(source = "reportedId", target = "reported.id")
    Report reportCreateDtoToReport(ReportCreateDto dto);

    // 신고 수정
    @Mapping(source = "reporterId", target = "reporter.id")
    @Mapping(source = "reportedId", target = "reported.id")
    Report reportUpdateDtoToReport(ReportUpdateDto dto);
}
