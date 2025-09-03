package com.carrental.account.dto;

public record AccessTokenResponse(String token, Long expiredAt) {
}
