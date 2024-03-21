
-- Insert data into challenge table
-- challengeID 인증 타입 → 1,2번(TEXT), 3번(PICTURE), 4번(GITHUB), 5번(LINK)
INSERT INTO `challenge` (`create_date`, `modified_date`, `description`, `is_free`, `title`, `verification_description`, `verification_type`)
VALUES
    ('2024-03-16 00:00:00.0000', '2024-03-16 00:00:00.0000', '이 챌린지는 백엔드 직군을 대상으로 한 기술 면접 챌린지입니다. 이곳에서 참가자들이 다양한 기술 면접 질문을 대비할 수 있도록 고안되었습니다. 매일 새로운 기술 면접 질문을 통해 기술 면접에 대해 꾸준히 대비할 수 있는 환경을 제공하여, 참가자들이 기술적인 역량을 향상시키고 면접에 자신감을 가질 수 있도록 돕습니다. 함께 성장하고 싶은 모든 분들의 도전을 기다립니다. 백엔드 기술면접 챌린지에서 여러분을 만나볼 수 있기를 기대합니다! 🌟',
     0, '백엔드 기술면접 챌린지', '- 매일 새로운 기술 면접 질문이 제공됩니다. - 참가자들은 주어진 질문에 대한 자신의 답변을 작성하여 인증을 제출합니다. - 다른 참가자들의 답변을 공유하며 좋아요 기능을 통해 추천순으로 다른 참여자들의 인기있는 답변을 조회할 수 있습니다. - 정확하고 상세한 답변은 다른 참가자들에게도 도움이 됩니다. - 질문에 맞지 않는 답변을 제출 시 이후 인증이 취소 될 수 있습니다. 주어진 질문에 충실하고 정확한 내용을 작성해 주시기 바랍니다.', 'TEXT'),
    ('2024-03-16 00:00:00.0000', '2024-03-16 00:00:00.0000',  '이 챌린지는 참여자들이 다양한 주제로 참여하기 떄문에 최신 프론트엔드 기술 트렌드를 따라잡고, 다양한 IT 아티클이나 서비스를 분석하여 여러분의 지식을 넓힐 수 있는 기회를 제공합니다. 참여자들의 분석과 해석을 공유하며 새로운 관점을 얻을 수 있고, 풍부한 학습 경험을 가지실 수 있습니다. 프론트엔드 기술에 대한 이해를 넓히고, 최신 트렌드를 파악하는 기회를 원한다면, 이 챌린지는 여러분을 위한 최적의 선택입니다. 매일 조금씩, 꾸준히 학습하며 여러분의 프론트엔드 개발 역량을 한 단계 업그레이드시켜 보세요! 이 챌린지에 참여함으로써, 최신 프론트엔드 기술과 트렌드에 대한 깊은 이해를 얻고자 하는 분, 다양한 IT 아티클과 서비스를 분석하여 자신의 지식을 넓히고자 하는 분, 그리고 자신의 프론트엔드 개발 역량을 체계적으로 강화하고자 하는 모든 분들에게 탁월한 기회가 될 것입니다. 함께 성장하고 싶은 모든 분들의 도전을 기다리고 있으며, 프론트엔드 아티클 공유 챌린지에서 여러분을 만나볼 수 있기를 기대합니다! 🌟',
     0, '프론트엔드 아티클 공유 챌린지', '1. **매일 선정한 아티클에 대한 분석글을 해당 아티클의 링크와 직접 작성한 글로 인증합니다.** *step 1. 아티클 주제 선정 : 웹사이트 로딩 속도 개선을 위한 최신 기술* *step 2. 아티클 분석글 작성 : 이미지 최적화, 코드 스폴리팅, 캐싱 전략, HTTP/2와 HTTP/3* 1. **인증글에는 해당 아티클이나 서비스에 대한 요약, 분석, 그리고 개인적인 의견이 포함되어야 하며, 적절하지 못한 인증의 경우 무효 처리가 될 수 있습니다.** 참여자는 매일 선택한 IT 아티클이나 서비스 분석글을 링크와 글로 인증해야 합니다. 이 과정을 통해, 참여자들은 새로운 정보를 습득하고 자신의 생각을 정리하는 능력을 키울 수 있습니다.', 'LINK'),


    ('2024-03-16 00:00:00.0000', '2024-03-16 00:00:00.0000', '이 챌린지는 개발자들을 대상으로 한 1일 1커밋 챌린지입니다. 참가자들은 매일 개발 학습 결과물을 깃허브에 커밋하며, 일상 속에서 꾸준히 개발 공부를 지속하는 습관을 기르게 됩니다. 개발에 대한 열정이 있고, 매일 조금씩이라도 자신을 발전시키고자 하는 모든 분들을 환영합니다. 1일 1커밋 챌린지를 통해 함께 성장의 길을 걸어가요. 여러분의 새로운 시작을 함께 할 수 있기를 기대합니다. 🌼',
     1, '1일 1커밋 챌린지', '- 챌린지 신청 전 프로필에서 깃허브 연동을 완료해야합니다. - 인증 시도 시 연동된 깃허브 계정에서 커밋 내역을 받아와 성공 여부를 확인합니다. - 챌린지 인증은 매일 1회만 가능합니다.', 'GITHUB'),


    ('2024-03-16 00:00:00.0000', '2024-03-16 00:00:00.0000', '이 챌린지는 하루 핸드폰 사용 시간을 4시간 이하로 제한하는 생활 습관 개선 챌린지입니다. 이곳에서 참가자들은 매일 어제 하루 핸드폰 사용 시간 내역을 공유하면서 하루를 되돌아보도록 고안되었습니다. 이 챌린지를 통해 참가자들이 디지털 기기에 대한 의존도를 줄이고, 조금 더 의미있는 활동에 더 많은 시간을 할애할 수 있도록 돕습니다. **휴대폰 사용시간을 감소시 얻을 수 있는 이점들:** - **집중력 향상:** 휴대폰 사용을 줄이면 주의를 집중시키고 작업이나 공부에 더 많은 시간을 할애할 수 있습니다. 따라서 일상적인 활동에 대한 집중력이 향상됩니다. - **시간 관리 개선:** 휴대폰 사용을 줄이면 시간을 더 효율적으로 관리할 수 있습니다. 시간이 더 많이 확보되어 운동, 취미 활동, 가족과의 시간 등에 더 많은 시간을 할애할 수 있습니다. - **스트레스 감소:** 일상적으로 휴대폰 사용량이 줄어들면 디지털 스트레스가 감소할 수 있습니다. 소셜 미디어의 부정적인 영향을 줄이고, 비정기적인 알림에 의한 스트레스가 감소할 수 있습니다. - **뇌 건강:** 지나치게 휴대폰을 사용하는 것은 뇌에 부담을 줄 수 있습니다. 휴대폰 사용을 줄이면 뇌의 휴식과 회복에 도움이 되며, 뇌 건강을 유지할 수 있습니다. - **사회적 관계 강화:** 휴대폰을 덜 사용하면 실제로 있는 사람들과 더 많은 시간을 보내고, 직접적인 대화와 상호작용을 늘릴 수 있습니다. 이는 사회적 관계를 강화하고 소통 능력을 향상시킬 수 있습니다. 함께 생할 습관을 개선하고 싶은 모든 분들의 도전을 기다립니다. 스크린타임 4시간 챌린지에서 여러분을 만나볼 수 있기를 기대합니다! ⏰',
     1, '스크린타임 4시간 챌린지', '- 아이폰의 경우 [설정]에서 내장 프로그램인 ‘스크린 타임’ 혹은 ‘배터리’ 를 통해 지난 하루 핸드폰을 사용한 시간을 캡쳐해 공유합니다. - 안드로이드의 경우 내장 앱인 ‘디지털 웰빙’ 혹은 ‘넌얼마나쓰기’ 와 같은 앱을 통해 지난 하루 핸드폰을 사용한 시간을 캡쳐해 공유합니다. 인증 시 캡쳐 이미지를 등록하면 우선 성공으로 간주합니다. - 인증 시 휴대폰 사용 시간과 날짜가 명확하게 보이도록 캡처해주세요. - 추후 관리자의 확인을 통해 등록된 이미지가 부적합하다고 판단될 시 실패로 처리될 수 있습니다. - 챌린지 인증은 매일 1회만 가능합니다.', 'PICTURE');


-- Insert test data for the challenge_group table
INSERT INTO `challenge_group` (`create_date`, `modified_date`, `participant_count`, `challenge_id`, `group_index`, `end_date`, `title`, `start_date`)
VALUES
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 9, 1, 1, '2024-03-10 00:00:00.0000', '백엔드 기술면접 챌린지 1기', '2024-02-26 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 10, 1, 2, '2024-03-24 00:00:00.0000', '백엔드 기술면접 챌린지 2기', '2024-03-11 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 23, 1, 3, '2024-04-07 00:00:00.0000', '백엔드 기술면접 챌린지 3기', '2024-03-25 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 9, 2, 1, '2024-03-10 00:00:00.0000', '프론트엔드 아티클 공유 챌린지 1기', '2024-02-26 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 10, 2, 2, '2024-03-24 00:00:00.0000', '프론트엔드 아티클 공유 챌린지 2기', '2024-03-11 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 23, 2, 3, '2024-04-07 00:00:00.0000', '프론트엔드 아티클 공유 챌린지 3기', '2024-03-25 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 15, 3, 1, '2024-03-10 00:00:00.0000', '1일 1커밋 챌린지 1기', '2024-02-26 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 23, 3, 2, '2024-03-24 00:00:00.0000', '1일 1커밋 챌린지 2기', '2024-03-11 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 11, 3, 3, '2024-04-07 00:00:00.0000', '1일 1커밋 챌린지 3기', '2024-03-25 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 8, 4, 1, '2024-03-10 00:00:00.0000', '스크린타임 4시간 챌린지 1기', '2024-02-26 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 10, 4, 2, '2024-03-24 00:00:00.0000', '스크린타임 4시간 챌린지 2기', '2024-03-11 00:00:00.0000'),
    ('2024-03-09 00:00:00.0000', '2024-03-09 00:00:00.0000', 29, 4, 3, '2024-04-07 00:00:00.0000', '스크린타임 4시간 챌린지 3기', '2024-03-25 00:00:00.0000');


-- Insert test data for the quiz table
-- GET : api/v1/verify/{challengeGroupId} -> 2번 : 프론트 진행 퀴즈 / 5번 : 백엔드 진행 퀴즈
INSERT INTO `quiz` (`challenge_group_id`, `date`, `question`)
VALUES
    (2, '2024-03-11 00:00:00.0000', '3월 11일 백엔드 퀴즈 문제'),
    (2, '2024-03-12 00:00:00.0000', '3월 12일 백엔드 퀴즈 문제'),
    (2, '2024-03-13 00:00:00.0000', '3월 13일 백엔드 퀴즈 문제'),
    (2, '2024-03-14 00:00:00.0000', '3월 14일 백엔드 퀴즈 문제'),
    (2, '2024-03-15 00:00:00.0000', '3월 15일 백엔드 퀴즈 문제'),
    (2, '2024-03-16 00:00:00.0000', '3월 16일 백엔드 퀴즈 문제'),
    (2, '2024-03-17 00:00:00.0000', '3월 17일 백엔드 퀴즈 문제'),
    (2, '2024-03-18 00:00:00.0000', '3월 18일 백엔드 퀴즈 문제'),
    (2, '2024-03-19 00:00:00.0000', '3월 19일 백엔드 퀴즈 문제'),
    (2, '2024-03-20 00:00:00.0000', '3월 20일 백엔드 퀴즈 문제'),
    (2, '2024-03-21 00:00:00.0000', '3월 21일 백엔드 퀴즈 문제'),
    (2, '2024-03-22 00:00:00.0000', '3월 22일 백엔드 퀴즈 문제'),
    (2, '2024-03-23 00:00:00.0000', '3월 23일 백엔드 퀴즈 문제'),
    (2, '2024-03-24 00:00:00.0000', '3월 24일 백엔드 퀴즈 문제');
