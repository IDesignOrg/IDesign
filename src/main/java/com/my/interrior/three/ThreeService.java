package com.my.interrior.three;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThreeService {

    private final UserRepository userRepository;
    private final ThreeRepository threeRepository;
    private final PointRepository pointRepository;
    private final DataRepository dataRepository;
//    private final ChildRelationshipRepository childRelationshipRepository;

    @Transactional
    public void saveData(SaveProjectRequest request, String userId) {
        UserEntity user = userRepository.findByUId(userId);
        String projectId = request.getProjectId();

        log.info("userId : {}, projectId : {}, user : {}", userId, projectId, user);

        // 저장하기 전에 ThreeEntity 먼저 저장시켜야 함.
        ThreeEntity threeEntity = new ThreeEntity();
        threeEntity.setProjectId(projectId);
        threeEntity.setUserEntity(user);
        // three 저장
        threeRepository.save(threeEntity);

        // data 저장
        Map<String, DataEntity> dataMap = new HashMap<>();

        // Step 1: 모든 데이터 엔티티를 저장
        for (SaveProjectRequest.DataRequest dataRequest : request.getDataEntities()) {
        	log.info("dataRequest의 값들 : {}", dataRequest);
            DataEntity data = new DataEntity();
            data.setOid(dataRequest.getOid());
            data.setType(dataRequest.getType());
            data.setRotation(dataRequest.getRotation());
            data.setParent(dataRequest.getParent());
            data.setChildren(dataRequest.getChildren());
            data.setThreeEntity(threeEntity);

            data = dataRepository.save(data);
            dataMap.put(data.getOid(), data);

            if (dataRequest.getPoints() != null) {
                for (SaveProjectRequest.PointRequest pointRequest : dataRequest.getPoints()) {
                    PointEntity point = new PointEntity();
                    point.setX(pointRequest.getX());
                    point.setY(pointRequest.getY());
                    point.setZ(pointRequest.getZ());
                    point.setData(data);

                    pointRepository.save(point);
                }
            }
        }
    }
}

