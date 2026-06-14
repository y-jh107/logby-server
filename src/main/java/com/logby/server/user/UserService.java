package com.logby.server.user;

import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.config.JwtProvider;
import com.logby.server.follow.FollowRepository;
import com.logby.server.log.LogRepository;
import com.logby.server.log.Visibility;
import com.logby.server.user.dto.LoginRequest;
import com.logby.server.user.dto.MyProfileResponse;
import com.logby.server.user.dto.SignUpRequest;
import com.logby.server.user.dto.TokenResponse;
import com.logby.server.user.dto.UpdateProfileRequest;
import com.logby.server.user.dto.UserProfileResponse;
import com.logby.server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .nickname(request.nickname())
            .build();

        return UserResponse.from(userRepository.save(user));
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        return TokenResponse.of(jwtProvider.generateToken(user.getId()));
    }

    public MyProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return MyProfileResponse.from(user);
    }

    @Transactional
    public MyProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String nickname = request.nickname() != null ? request.nickname() : user.getNickname();
        String bio = request.bio() != null ? request.bio() : user.getBio();
        String profileImage = request.profileImage() != null ? request.profileImage() : user.getProfileImage();

        user.updateProfile(nickname, profileImage, bio);
        return MyProfileResponse.from(user);
    }

    public UserProfileResponse getUserProfile(Long targetUserId, Long currentUserId) {
        User user = userRepository.findById(targetUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        long followerCount = followRepository.countByFollowingId(targetUserId);
        long followingCount = followRepository.countByFollowerId(targetUserId);
        long publicLogCount = logRepository.countByUserIdAndVisibility(targetUserId, Visibility.PUBLIC);
        boolean isFollowing = currentUserId != null
            && followRepository.existsByFollowerIdAndFollowingId(currentUserId, targetUserId);

        return UserProfileResponse.of(user, followerCount, followingCount, publicLogCount, isFollowing);
    }

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }
}
