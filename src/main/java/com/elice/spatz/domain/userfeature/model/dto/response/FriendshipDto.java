package com.elice.spatz.domain.userfeature.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendshipDto {
    private Long id;
    private Long userId;
    private Long friendId;
    private boolean friendStatus;
}
