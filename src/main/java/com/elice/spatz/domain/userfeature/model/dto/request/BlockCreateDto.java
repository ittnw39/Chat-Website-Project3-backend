package com.elice.spatz.domain.userfeature.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlockCreateDto {
    private long blockerId;
    private long blockedId;
}
