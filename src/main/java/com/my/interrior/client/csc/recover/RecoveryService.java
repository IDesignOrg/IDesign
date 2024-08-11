package com.my.interrior.client.csc.recover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {

    @Autowired
    private RecoveryRepository recoveryRepository;

    // 복구 요청을 저장
    public void saveRecoveryRequest(RecoveryEntity recovery) {
        recoveryRepository.save(recovery);
    }
}
