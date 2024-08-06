package com.elice.spatz.domain.userfeature.model.dto.response;

import com.elice.spatz.domain.userfeature.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendRequestDto {
    private Long requesterId;
    private Long recipientId;
    private Status requestStatus;
}
