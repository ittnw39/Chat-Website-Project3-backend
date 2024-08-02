package com.elice.spatz.domain.userfeature.model.dto.response;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import com.elice.spatz.domain.userfeature.model.entity.Friendship;
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
    @Mapping(source = "blockStatus", target = "blockStatus")
    BlockDto blockToBlockDto(Block entity);

    // 친구 요청
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "recipient.id", target = "recipientId")
    FriendRequestDto friendRequestToFriendRequestDto(FriendRequest entity);

    // 친구
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "friend.id", target = "friendId")
    @Mapping(source = "friend.nickname", target = "friendNickname")
    FriendDto friendshipToFriendDto(Friendship entity);

    // 신고 요청
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reported.id", target = "reportedId")
    @Mapping(source = "reportReason", target = "reportReason")
    @Mapping(source = "reportURL", target = "reportURL")
    @Mapping(source = "reportStatus", target = "reportStatus")
    ReportDto reportToReportDto(Report entity);
}
