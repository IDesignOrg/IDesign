package com.my.interrior.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginAttemptService {
	
	//로그인 시도 횟수를 저장하는 맵
	//String = userIp, Integer = 로그인 실패 횟수
	private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
	
	// 차단 시간을 저장하는 맵
	private final Map<String, LocalDateTime> blockTimeMap = new ConcurrentHashMap<>();
	
	// 최대 시도 횟수
	private final int MAX_ATTEMPTS = 5;
	
	// 차단 시간(분)
	private final int BLOCK_DURATION_MINUTES = 10;
	
	// 로그인 성공 시 호출되어 시도 횟수 초기화
	public void loginSucceeded(String key) {
		log.info("로그인 성공: {}",key);
		//로그인 시도 횟수 삭제
		attemptsCache.remove(key);
		//차단 시간은 이미 isBlocked에서 확인하지만 안전장치용
		blockTimeMap.remove(key);
	}
	
	//로그인 실패 시 호출되어 시도 횟수를 증가
	public void loginFailed(String key) {
		// 시도횟수 가져오기 없으면 key값과 0을 가져옴
		int attempts = attemptsCache.getOrDefault(key, 0);
		// 시도 횟수 증가
		attempts++;
		//attemptsCache에 key값과 시도횟수를 저장
		attemptsCache.put(key, attempts);
		log.warn("로그인 실패: {}, 시도 횟수: {}/{}", key, attempts, MAX_ATTEMPTS);
		
		//시도 횟수가 최대 시도횟수에 달했을때 실행
		if(attempts >= MAX_ATTEMPTS) {
			//현재 시간 + 차단 시간을 더해서 blockUntil에 저장
			LocalDateTime blockUntil = LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES);
			//map에 key값과 유저 차단 시간을 저장
			blockTimeMap.put(key, blockUntil);
			log.warn("계정이 차단됨: {}, 차단 해제 시간: {}", key, blockUntil);
		}
	}
	
	// 현재 차단 상태인지 확인
	public boolean isBlocked(String key) {
		//해당 맵에 특정 키가있는지 확인
		if (blockTimeMap.containsKey(key)) {
			LocalDateTime blockTime = blockTimeMap.get(key);
			//현재 시간과 차단시간을 확인
			if (LocalDateTime.now().isAfter(blockTime)) {
				//차단 시간이 지났으면 해제
				blockTimeMap.remove(key);
				attemptsCache.remove(key);
				return false;
			}
			//차단시간이 지나지 않았을경우 true로 막음
			return true;
		}
		// 키값이 없으면 보내줌
		return false;
	}
	
	//남은 로그인 시도 횟수를 반환
	public int getAttemptsLeft(String key) {
		return Math.max(0, MAX_ATTEMPTS - attemptsCache.getOrDefault(key, 0));
	}
	
	//차단 해제까지 남은 시간(분)을 반환
	public long getRemainingBlockTime(String key) {
		if (blockTimeMap.containsKey(key)) {
			LocalDateTime blockUntil = blockTimeMap.get(key);
			LocalDateTime now = LocalDateTime.now();
			
			//시간이 지나지 않았을 경우
			if (now.isBefore(blockUntil)) {
				//현재 시간과 차단시간의 간격을 알려줌 +1은 14분59초일떄 15분으로 보이기 위함
				return Duration.between(now, blockUntil).toMinutes() + 1;
			}
		}
		return 0;
	}

}
