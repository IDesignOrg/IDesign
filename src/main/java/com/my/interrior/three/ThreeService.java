package com.my.interrior.three;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.three.SaveProjectRequest.DataRequest;

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
    private final ChildRelationshipRepository childRelationshipRepository;

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
        Map<Long, DataEntity> dataMap = new HashMap<>();

        // Step 1: 모든 데이터 엔티티를 저장
        for (SaveProjectRequest.DataRequest dataRequest : request.getDataEntities()) {
            DataEntity data = new DataEntity();
            data.setOid(dataRequest.getOid());
            data.setType(dataRequest.getType());
            data.setRotation(dataRequest.getRotation());
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

        // Step 2: 자식 관계를 설정
        for (SaveProjectRequest.DataRequest dataRequest : request.getDataEntities()) {
            DataEntity parent = dataMap.get(dataRequest.getOid());
            		log.info("parent: {}", parent);
            if (parent != null && dataRequest.getChildren() != null) {
                for (DataRequest childOid : dataRequest.getChildren()) {
                    log.info("childOid : {}", childOid);
                    if (childOid != null) {
                        ChildRelationshipEntity relationship = new ChildRelationshipEntity();
                        relationship.setParent(parent);
                        //지금 oid의 값이 null이 들어온다.
                        relationship.setChild(null);
                        childRelationshipRepository.save(relationship);
  
                    } else {
                        log.warn("oid를 가지고있는 child:  {} dataMap에 없음", childOid);
                    }
                }
            } else {
                if (parent == null) {
                    log.warn("oid를 가지고 있는 parent:  {} dataMap에 없음", dataRequest.getOid());
                }
                if (dataRequest.getChildren() == null) {
                    log.warn("부모 oid의 children이 없음 {}", dataRequest.getOid());
                }
            }
        }
    }
}

