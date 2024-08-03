package com.elice.spatz.domain.userfeature.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BlockDto {
    private long blockerId;
    private long blockedId;
}
