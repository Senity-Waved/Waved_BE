package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyChallengeServiceImpl implements MyChallengeService {

    private final MyChallengeRepository myChallengeRepository;

    public MyChallenge findMyChallengeById(Long myChallengeId) {
        return myChallengeRepository.findById(myChallengeId)
                .orElseThrow(() -> new MyChallengeNotFoundException("MyChallenge를 찾을 수 없습니다."));
    }

}
