package com.elice.spatz.domain.user.service;

import com.elice.spatz.domain.user.dto.UserRegisterDto;
import com.elice.spatz.domain.user.dto.UserRegisterResultDto;
import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserRegisterResultDto register(UserRegisterDto userRegisterDto) {
        Users newUser = new Users(userRegisterDto.getEmail(),
                userRegisterDto.getPassword(),
                userRegisterDto.getNickname(),
                null,
                false,
                "user", true);

        userRepository.save(newUser);

        return new UserRegisterResultDto(true, null);
    }

    // 이미 입력한 이메일에 해당하는 사용자가 존재하면 false 반환 >> 실패의 의미
    // 이미 입력한 이메일에 해당하는 사용자가 없다면 true 반환 >> 성공의 의미
    public boolean preCheckEmail(String email) {
        return userRepository.findUsersByEmail(email).isEmpty();
    }

}
