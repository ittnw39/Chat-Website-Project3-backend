package com.elice.spatz.domain.userfeature.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendshipDto {
    private Long id;
    private Long userId;
    private Long friendId;
    private boolean isActive;
}
