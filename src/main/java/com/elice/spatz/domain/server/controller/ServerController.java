package com.elice.spatz.domain.server.controller;

import com.elice.spatz.domain.server.dto.ServerGetDto;
import com.elice.spatz.domain.server.dto.ServerDto;
import com.elice.spatz.domain.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping("/server")
    public ResponseEntity<ServerDto> getServer(@RequestParam("id") Long id)
    {
        return ResponseEntity.ok(serverService.getServer(id));
    }

    @GetMapping("/servers")
    public ResponseEntity<List<ServerDto>> getServers()
    {
        return ResponseEntity.ok(serverService.getServers());
    }

    @PostMapping("/server")
    public ResponseEntity<ServerDto> createServer(@RequestBody ServerDto serverDto)
    {
        serverService.createServer(serverDto);
        return ResponseEntity.ok(serverDto);
    }

    @PatchMapping("/server")
    public ResponseEntity<ServerDto> patchServer(@RequestParam("id") Long id, @RequestBody ServerDto serverDto)
    {

        serverService.patchServer(id,serverDto);
        return ResponseEntity.status(HttpStatus.OK).body(serverDto);
    }

    @DeleteMapping("/server")
    public ResponseEntity<Void> deleteServer(@RequestParam("id") Long id)
    {

        serverService.deleteServer(id);
        return ResponseEntity.ok(null);
    }
}
