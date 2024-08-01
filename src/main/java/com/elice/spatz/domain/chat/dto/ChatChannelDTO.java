package com.elice.spatz.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatChannelDTO {
    private Long id;
    private String name;
    private String serverId;
}