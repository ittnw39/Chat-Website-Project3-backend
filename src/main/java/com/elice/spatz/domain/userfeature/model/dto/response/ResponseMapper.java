package com.elice.spatz.domain.userfeature.model.dto.response;

import com.elice.spatz.domain.userfeature.model.dto.request.*;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import com.elice.spatz.domain.userfeature.model.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);

    // 차단
    @Mapping(source = "blocker.id", target = "blockerId")
    @Mapping(source = "blocked.id", target = "blockedId")
    BlockCreateDto blockToBlockCreateDto(Block entity);

    // 친구 요청
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "recipient.id", target = "recipientId")
    FriendRequestCreateDto friendRequestToFriendRequestDto(FriendRequest entity);

    // 신고 요청
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reported.id", target = "reportedId")
    ReportCreateDto reportToReportCreateDto(Report entity);

    // 신고 수정
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reported.id", target = "reportedId")
    ReportUpdateDto reportToReportUpdateDto(Report entity);
}
