package com.senity.waved.domain.verification.service;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GithubApi {

    GitHub github;

    public GHCommit getCommits(String userId, String githubToken) {
        try {
            connectToGithub(githubToken);
        } catch (IOException e) {
            throw new IllegalArgumentException("gitHub 연결에 실패했습니다.");
        }

        GHCommit firstCommit = null;

        GHCommitSearchBuilder builder = github.searchCommits()
                .author(userId)
                .sort(GHCommitSearchBuilder.Sort.AUTHOR_DATE);

        PagedSearchIterable<GHCommit> commits = builder.list().withPageSize(1); // 페이지 크기를 1로 설정하여 첫 번째 커밋만 가져옴

        // 첫 번째 커밋 가져오기
        if (commits.iterator().hasNext()) {
            firstCommit = commits.iterator().next();
        }

        return firstCommit;
    }

    private void connectToGithub(String githubToken) throws IOException {
        github = new GitHubBuilder().withOAuthToken(githubToken).build();
        github.checkApiUrlValidity();
    }
}