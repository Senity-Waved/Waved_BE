package com.senity.waved.domain.verification.service;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@Transactional
@RequiredArgsConstructor
public class GithubService {
    private final GithubApi githubApi;

    public boolean hasCommitsToday(String githubId, String token) throws IOException {
        GHCommit commits = githubApi.getCommits(githubId, token);
        LocalDate commitDate = commits.getCommitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return commitDate.equals(LocalDate.now());
    }
}
