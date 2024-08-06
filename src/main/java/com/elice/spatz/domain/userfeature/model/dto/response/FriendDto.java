package com.elice.spatz.domain.userfeature.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendDto {
    private Long userId;
    private Long friendId;
    private String userNickname;
    private String friendNickname;
}
