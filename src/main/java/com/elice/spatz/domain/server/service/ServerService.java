package com.elice.spatz.domain.server.service;

import com.elice.spatz.domain.server.dto.ServerDto;
import com.elice.spatz.domain.server.entity.Servers;
import com.elice.spatz.domain.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    @Transactional(readOnly = true)
    public ServerDto getServer(Long id)
    {
        Servers server = serverRepository.findById(id).orElseThrow(()->new RuntimeException("없는 엔티티"));

        return new ServerDto(server.getName());
    }

    @Transactional(readOnly = true)
    public List<ServerDto> getServers() {
        List<Servers> servers = serverRepository.findAll();
        return servers.stream()
                .map(server -> new ServerDto(server.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createServer(ServerDto serverDto)
    {
        Servers newServer = serverDto.toEntity();
        serverRepository.save(newServer);
    }

    @Transactional
    public void patchServer(Long id, ServerDto serverDto)
    {
        Servers server = serverRepository.findById(id).orElseThrow(()->new RuntimeException("없는 엔티티"));
        server.setName(serverDto.getName());

        serverRepository.save(server);


    }

    @Transactional
    public void deleteServer(Long id)
    {
        serverRepository.findById(id).orElseThrow(()->new RuntimeException("없는 엔티티"));

        serverRepository.deleteById(id);
    }


}
