package com.senity.waved.domain.member.service;

import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.ProfileInfoDto;
import org.springframework.security.core.userdetails.User;

public interface MemberService {
    String resolveRefreshToken(String refreshToken);
    void joinAfterOauth(String email, MemberJoinDto joinDto) ;
    void logout(String email, String token);
    void deleteMember(String email);

    ProfileInfoDto getProfileInfo(String email);
    ProfileEditDto getProfileInfoToEdit(String email);

    GithubInfoDto getGithubInfoToEdit(String email);
    void checkGithubConnection(String email, GithubInfoDto github);
    void saveGithubInfo(String email, GithubInfoDto gihub);
    void deleteGithubInfo(String email);
}
