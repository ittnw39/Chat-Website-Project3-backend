package com.elice.spatz.domain.user.service;

import com.elice.spatz.domain.user.dto.UserRegisterDto;
import com.elice.spatz.domain.user.dto.UserRegisterResultDto;
import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.user.repository.UserRepository;
import com.elice.spatz.domain.userfeature.model.dto.response.FriendDto;
import com.elice.spatz.domain.userfeature.model.entity.Friendship;
import com.elice.spatz.domain.userfeature.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

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

    // userFeature: 친구 검색 조회 기능
    @Transactional
    public Page<FriendDto> getFriendshipsByKeyword(String nickName, long userId, Pageable pageable) {
        Page<Users> usersPage = userRepository.findAllByNicknameContainingIgnoreCase(nickName, pageable);

        // 친구 여부 확인
        List<FriendDto> friendDtos = usersPage.getContent().stream()
                .filter(user -> friendshipRepository.existsByUserIdAndFriendId(userId, user.getId()))
                .map(user -> new FriendDto(
                        userId,
                        user.getId(),
                        user.getNickname()
                ))
                .collect(Collectors.toList());

        long totalElements = friendDtos.size();
        return new PageImpl<>(friendDtos, pageable, totalElements);
    }
}
