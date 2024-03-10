package com.senity.waved.base.security;

import com.senity.waved.domain.member.entity.AuthLevel;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");

        memberRepository.findByEmail(email).orElseGet(() -> createNewMember(email, generateRandomNickname()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "name");
    }

    private Member createNewMember(String email, String nickname) {
        AuthLevel authLevel = AuthLevel.MEMBER;
        List<String> adminMembers = Arrays.asList("waved7777@gmail.com", "imholy96@gmail.com"
                , "vywns9978@gmail.com", "waved8888@gmail.com", "fetest1228@gmail.com");

        if (adminMembers.contains(email)) {
            authLevel = AuthLevel.ADMIN;
        }

        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .authLevel(authLevel)
                .build();

        return memberRepository.save(newMember);
    }

    private String generateRandomNickname() {
        UUID uuid = UUID.randomUUID();
        String hash = Integer.toHexString(uuid.hashCode());
        return "서퍼" + hash.substring(0, Math.min(hash.length(), 6));
    }
}
