package com.senity.waved.domain.liked.service;

public interface LikedService {
    void addLikedToVerification(String email, Long verificationId);
    Long countLikesToVerification(Long verificationId);
    void removeLikeFromVerification(String email, Long verificationId);
}
