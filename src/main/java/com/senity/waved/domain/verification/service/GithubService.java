package com.senity.waved.domain.verification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GithubService {
    private final GithubApi githubApi;

    public boolean hasCommitsToday(String githubId, String token) throws IOException {
        GHCommit commits = githubApi.getCommits(githubId, token);
        ZonedDateTime commitDate = commits.getCommitDate().toInstant().atZone(ZoneId.of("GMT"));
        // if (latestGroup.getStartDate().plusHours(9).equals(ZonedDateTime.now(ZoneId.of("GMT")).plusHours(9).truncatedTo(ChronoUnit.DAYS))
        log.error("----------------------commitDate : ", commitDate);
        log.error("----------------------today      : ", ZonedDateTime.now(ZoneId.of("GMT")).plusHours(9).truncatedTo(ChronoUnit.DAYS));
        return commitDate.plusHours(9).truncatedTo(ChronoUnit.DAYS).equals(ZonedDateTime.now(ZoneId.of("GMT")).plusHours(9).truncatedTo(ChronoUnit.DAYS));
    }
}
