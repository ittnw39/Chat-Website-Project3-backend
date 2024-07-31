package com.elice.spatz.domain.server.dto;

import com.elice.spatz.domain.server.entity.Servers;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ServerDto {
    private String name;

    public Servers toEntity(){
            return Servers.builder()
                    .name(this.name)
                    .build();
    }
}
