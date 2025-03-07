package com.example.music.redis;

import com.example.music.data.SessionData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    final int MAX_TRANSACTION_TIMEOUT = 30;
    final int MAX_USER_TRANSACTION_TIMEOUT = 30;
    final int MAX_OTP_EXPIRY = 90;
    final String SESSION_DATA_MAP = "SESSION_DATA_MAP";
    final String USER_SESSION_KEY = "SESSION_CUST_ID_MAP";
    final String CHECKSUM_MAP = "CHECK_SUM_MAP";
    final String OTP_MAP = "OTP_MAP";
    private final RedissonClient client;

    public void setSession(String sessionId, SessionData sessionData) {
        RMapCache<String, String> userSession = client.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = client.getMapCache(SESSION_DATA_MAP);
        if (userSession != null && userSession.containsKey(sessionData.getUser_id())) {
            String oldSessionId = userSession.get(sessionData.getUser_id());
            if (StringUtils.isNotEmpty(oldSessionId)) {
                sessionMap.remove(oldSessionId); // Xóa thông tin user session cũ
            }
        }

        // Replace/ add new session
        userSession.put(sessionData.getUser_id(), sessionId, MAX_USER_TRANSACTION_TIMEOUT, TimeUnit.MINUTES); // Replace/ add new sang session mới
        sessionMap.put(sessionId, sessionData, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);

    }
    public void clearSession(String sessionId, String userId) {
        RMapCache<String, String> userSession = client.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = client.getMapCache(SESSION_DATA_MAP);
        userSession.remove(userId);
        sessionMap.remove(sessionId);
    }

    public SessionData getSession(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) return null;

        RMapCache<String, String> userSession = client.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = client.getMapCache(SESSION_DATA_MAP);
        SessionData sessionData = (SessionData) sessionMap.get(sessionId);

        // reset time alive
        if (sessionData != null) {
            // Remove
            userSession.remove(sessionData.getUser_id());
            sessionMap.remove(sessionId);

            // push data
            userSession.put(sessionData.getUser_id(), sessionId, MAX_USER_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
            sessionMap.put(sessionId, sessionData, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
        }
        return sessionData;
    }

    public void setCheckSumData(String key, String value) {
        RMapCache<String, String> checkSumMap = client.getMapCache(CHECKSUM_MAP);
        checkSumMap.put(key, value, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
    }

    public void clearCheckSum(String key) {
        RMapCache<String, String> checkSumMap = client.getMapCache(CHECKSUM_MAP);
        checkSumMap.remove(key);
    }

    public String getCheckSum(String key) {
        RMapCache<String, String> checkSumMap = client.getMapCache(CHECKSUM_MAP);
        return checkSumMap.get(key);
    }

    public void setOTP(String key, String value) {
        RMapCache<String, String> otpMap = client.getMapCache(OTP_MAP);
        otpMap.put(key, value, MAX_OTP_EXPIRY, TimeUnit.MINUTES);
    }

    public void clearOTP(String key) {
        RMapCache<String, String> otpMap = client.getMapCache(OTP_MAP);
        otpMap.remove(key);
    }

    public String getOTP(String key) {
        RMapCache<String, String> otpMap = client.getMapCache(OTP_MAP);
        return otpMap.get(key);
    }

}
