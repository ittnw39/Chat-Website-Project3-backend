package com.elice.spatz.domain.userfeature.model.dto.response;

import com.elice.spatz.domain.userfeature.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestDto {
    private Long id;
    private Long requesterId;
    private Long recipientId;
    private Status requestStatus;
}
