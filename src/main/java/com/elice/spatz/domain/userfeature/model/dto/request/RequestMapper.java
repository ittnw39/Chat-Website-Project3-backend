package com.elice.spatz.domain.userfeature.model.dto.request;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.userfeature.model.entity.Block;
import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import com.elice.spatz.domain.userfeature.model.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    // 차단
    @Mapping(source = "blockerId", target = "blocker", qualifiedByName = "idToUser")
    @Mapping(source = "blockedId", target = "blocked", qualifiedByName = "idToUser")
    @Mapping(source = "blockStatus", target = "blockStatus")
    Block blockCreateDtoToBlock(BlockCreateDto dto);

    // 친구 요청
    @Mapping(source = "requesterId", target = "requester", qualifiedByName = "idToUser")
    @Mapping(source = "recipientId", target = "recipient", qualifiedByName = "idToUser")
    @Mapping(source = "requestStatus", target = "requestStatus")
    FriendRequest friendRequestCreateDtoToFriendRequest(FriendRequestCreateDto dto);

    // 신고 요청
    @Mapping(source = "reporterId", target = "reporter", qualifiedByName = "idToUser")
    @Mapping(source = "reportedId", target = "reported", qualifiedByName = "idToUser")
    @Mapping(source = "reportReason", target = "reportReason")
    @Mapping(source = "reportURL", target = "reportURL")
    @Mapping(source = "reportStatus", target = "reportStatus")
    Report reportCreateDtoToReport(ReportCreateDto dto);

    // 신고 수정
    @Mapping(source = "id", target = "id")
    @Mapping(source = "reporterId", target = "reporter", qualifiedByName = "idToUser")
    @Mapping(source = "reportedId", target = "reported", qualifiedByName = "idToUser")
    @Mapping(source = "reportReason", target = "reportReason")
    @Mapping(source = "reportURL", target = "reportURL")
    @Mapping(source = "reportStatus", target = "reportStatus")
    Report reportUpdateDtoToReport(ReportUpdateDto dto);

    // Users 객체 변환
    @Named("idToUser")
    default Users idToUser(long id) {
        Users user = new Users();
        user.setId(id);
        return user;
    }
}
