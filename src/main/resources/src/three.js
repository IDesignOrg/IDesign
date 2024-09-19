"use strict";

import axios from "axios";
import { THREE } from "../lib/three/loader/three.js";
import { EffectComposer } from "three/examples/jsm/postprocessing/EffectComposer.js";
import { RenderPass } from "three/examples/jsm/postprocessing/RenderPass.js";
import { OutlinePass } from "three/examples/jsm/postprocessing/OutlinePass.js";
import { ShaderPass } from "three/examples/jsm/postprocessing/ShaderPass.js";
import { FXAAShader } from "three/examples/jsm/shaders/FXAAShader.js";
import { OrbitControls } from "three/addons/controls/OrbitControls.js";

import { D2Room, D3Room } from "../lib/three/objects/Room.js";
import { RotationController } from "../lib/three/objects/rotationController.js";
import {
  calculateCenter,
  getClickedCircleIndex,
} from "../lib/three/calculater.js";
import { WEBGL } from "../lib/webgl.js";
import {
  backgroundName,
  chairName,
  circleGroupName,
  circleName,
  deskName,
  floorName,
  moveConrollerName,
  moveControllerChildrenName,
  rotationConrollerName,
  shadowName,
} from "../lib/three/objectConf/objectNames.js";
import {
  gridHelperY,
  objectY,
  roomY,
  wallY,
} from "../lib/three/objectConf/renderOrders.js";
import { MoveController } from "../lib/three/objects/moveController.js";
import { throttle } from "../lib/throttling.js";
import { debounce } from "../lib/debounce.js";
import { saveFactory } from "../lib/three/saveFactory.js";
import { loadFurnitures } from "../lib/three/loader/furnitures.js";

const save = document.getElementById("save");
const hudIcon = document.getElementById("hud-icon");
const modeToggles = document.getElementById("modeToggles");

const [chair] = await loadFurnitures;

export const furnitureObjects = {
  chair,
};

// zoom in/out & drag and drop
let isDragging = false;
let isRoomClick = {
  isClick: false,
  object: null,
};
let previousMousePosition = { x: 0, y: 0 };
export const maxZoom = 1000;
export const minZoom = 20;
let cameraZoom = 500;

// create object
let isCreating = {
  isSelect: false,
  isDragging: false,
  target: null,
};

let isChangingObject = {
  isDBClick: false, //circle을 통해 드래깅 중인지
  isHover: false, //마우스에 무언가 올라가있
  isDragging: false, //is changer circle moving ?
  changingObjectId: null, // change object...
  circleIdx: null, // 몇 번째 원을 통해 도형을 바꾸는지
  name: null,
  circleId: null,
};

const mouse = new THREE.Vector2();
// const raycaster = new THREE.Raycaster();
const scene = new THREE.Scene();
scene.name = "scene";
scene.background = new THREE.Color("rgb(250,251,255)");
const camera = new THREE.PerspectiveCamera(
  75,
  window.innerWidth / window.innerHeight,
  0.1,
  10000
);
const renderer = new THREE.WebGLRenderer({
  alpha: true,
  antialias: true,
  preserveDrawingBuffer: true,
});
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
const canvas = renderer.domElement;

camera.position.set(0, cameraZoom, 0);
camera.lookAt(0, 0, 0);
const controls = new OrbitControls(camera, renderer.domElement);
controls.enabled = false;
// EffectComposer 및 OutlinePass 설정
const composer = new EffectComposer(renderer);
const renderPass = new RenderPass(scene, camera);
composer.addPass(renderPass);

// export const
const light = new THREE.DirectionalLight(0xffffff, 1.5);
light.position.set(200, 200, 200);
scene.add(light);

const outlinePass = new OutlinePass(
  new THREE.Vector2(window.innerWidth, window.innerHeight),
  scene,
  camera
);

const effectFXAA = new ShaderPass(FXAAShader);
effectFXAA.uniforms["resolution"].value.set(
  1 / window.innerWidth,
  1 / window.innerHeight
);
const raycaster = new THREE.Raycaster();
// 아웃라인 설정
outlinePass.edgeStrength = 3.0;
outlinePass.edgeGlow = 1.0;
outlinePass.edgeThickness = 6;
outlinePass.pulsePeriod = 0;
outlinePass.visibleEdgeColor.set("yellow");
outlinePass.hiddenEdgeColor.set("red");

composer.addPass(outlinePass);
composer.addPass(effectFXAA);

// const axes = new THREE.AxesHelper(1000);
// axes.name = "helper";
// scene.add(axes);
const gridHelper = new THREE.GridHelper(
  4000,
  200,
  "rgb(221,221,221)",
  "rgb(221,221,221)"
);
gridHelper.name = "helper";
gridHelper.position.y = gridHelperY;
scene.add(gridHelper);

const rotationController = new RotationController({ cameraZoom });
rotationController.setScale({ object: rotationController, scaler: 6 });
const moveController = new MoveController();
scene.add(moveController);
scene.add(rotationController);

const planeGeometry = new THREE.PlaneGeometry(8000, 8000);
const planeMaterial = new THREE.MeshBasicMaterial({
  color: "rgb(250,251,255)",
  side: THREE.FrontSide,
});
const background = new THREE.Mesh(planeGeometry, planeMaterial);
background.position.set(0, -2, 0);
background.rotation.x = -Math.PI / 2; // x축을 기준으로 90도 회전
background.name = backgroundName;
scene.add(background);

/*커서에 위치한 오브젝트를 가져옴 */
const getIntersects = (event) => {
  event.preventDefault();
  mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
  mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
  // Raycaster 설정
  raycaster.setFromCamera(mouse, camera);
  // 평면과의 교차점 계산
  return raycaster.intersectObjects(scene.children, true);
};

const getIntersectsArray = (raycaster) => {
  return raycaster.filter(
    (obj) =>
      obj.object.name === floorName ||
      obj.object.name === deskName ||
      obj.object.name === "Desk" ||
      obj.object.name === chairName ||
      obj.object.name === circleName ||
      obj.object.name.includes("controller") ||
      obj.object.name === moveControllerChildrenName
  );
};

//

const updateShadows = ({ object, background }) => {
  const points = [
    new THREE.Vector3(object.clickedPoints.x, wallY, object.clickedPoints.z),
    new THREE.Vector3(background.point.x, wallY, object.clickedPoints.z),
    new THREE.Vector3(background.point.x, wallY, background.point.z),
    new THREE.Vector3(object.clickedPoints.x, wallY, background.point.z),
  ];
  object.drawLines(points);
};

const onMouseDown = (event) => {
  if (controls.enabled) return;
  let points;
  const raycaster = getIntersects(event);
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;

  const objectArr = getIntersectsArray(raycaster);

  if (isChangingObject.isDBClick) {
    if (objectArr.length > 0) {
      let controller = objectArr.find((obj) =>
        obj.object.name.includes("controller")
      );
      if (!controller) {
        controller = objectArr.find((obj) =>
          obj.object.name.includes(moveControllerChildrenName)
        );

        if (!controller) return;
        controller = controller.object.parent;
        controller.onMouseDown();
        return;
      }

      controller = controller.object;
      if (controller.name !== rotationConrollerName)
        controller = controller.parent;
      const rotationController = controller;
      rotationController.onMouseDown();
      return;
    } else {
      const controller = scene.getObjectByName(rotationConrollerName);
      const moveController = scene.getObjectByName(moveConrollerName);
      controller.visible = false;
      moveController.visible = false;

      isChangingObject = {
        isDBClick: false,
        isHover: false,
        isDragging: false,
        changingObjectId: null,
        circleIdx: null,
        circleId: null,
        name: null,
      };
      isRoomClick = {
        isClick: false,
        object: null,
      };
    }
  } else if (isCreating.isSelect) {
    //무엇가를 만들고 있는중
    const { target, isDragging } = isCreating;
    switch (target) {
      case floorName:
        if (isDragging) {
          // floor 만들기 종료
          const obj = scene.getObjectByName(shadowName);

          obj.createRoom({ cameraZoom });
          isCreating = {
            isSelect: false,
            isDragging: false,
            target: null,
          };
        } else {
          // floor를 만드는 중
          points = [
            new THREE.Vector3(background.point.x, wallY, background.point.z),
            new THREE.Vector3(
              background.point.x + 20,
              wallY,
              background.point.z
            ),
            new THREE.Vector3(
              background.point.x + 20,
              wallY,
              background.point.z + 20
            ),
            new THREE.Vector3(
              background.point.x,
              wallY,
              background.point.z + 20
            ),
          ];
          const room = new D2Room({ points });
          scene.add(room);
          isCreating = {
            ...isCreating,
            isDragging: true,
          };
          return;
        }
        break;
      case chairName:
      case deskName:
        const floorObj = raycaster.find((obj) => obj.object.name === "floor");
        if (!floor) return;
        const room = floorObj.object.parent;
        const worldPoint = floorObj.point;
        // 클릭 위치를 group의 로컬 좌표로 변환
        const object = scene.getObjectByName(shadowName);
        const groupLocalPoint = room.worldToLocal(worldPoint.clone());
        const rotation = room.userData.rotation;
        object.userData = {
          ...object.userData,
          rotation,
          points: new THREE.Vector3(
            groupLocalPoint.x,
            objectY,
            groupLocalPoint.z
          ),
        };
        object.position.set(groupLocalPoint.x, objectY, groupLocalPoint.z);
        object.name = target;
        object.rotation.y = -room.userData.rotation;
        room.add(object);
        isCreating = {
          isSelect: false,
          isDragging: false,
          target: null,
        };
        return;

      default:
        return;
    }
  } else if (objectArr.length > 0) {
    // 그 외 방의 모양 변경 등
    const { object } = objectArr[0];
    const { name } = object;

    let room;
    switch (name) {
      case circleName:
        room = object.parent.parent;

        const cg = room.getObjectByName(circleGroupName);

        const clickedIdx = getClickedCircleIndex({ cg, object });
        if (clickedIdx === null) {
          isChangingObject = {
            isDBClick: false,
            isHover: false,
            isDragging: false,
            changingObjectId: null,
            circleIdx: null,
            name: null,
            circleId: null,
          };
          break;
        }
        isChangingObject = {
          ...isChangingObject,
          changingObjectId: room.id,
          isDragging: true,
          circleIdx: clickedIdx,
          circleId: object.id,
          name,
        };

        return;

      case floorName:
        //
        isRoomClick = {
          isClick: true,
          object: object,
        };
        break;
    }
  } else isDragging = true;
  previousMousePosition = { x: event.clientX, y: event.clientY };
};

const onMouseMove = (event) => {
  if (isDragging) {
    // 화면 옮기기
    const deltaMove = {
      x: event.clientX - previousMousePosition.x,
      y: event.clientY - previousMousePosition.y,
    };

    // 카메라 위치 업데이트
    camera.position.x -= deltaMove.x / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
    camera.position.z -= deltaMove.y / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
    camera.lookAt(camera.position.x, 0, camera.position.z);

    previousMousePosition = { x: event.clientX, y: event.clientY };
    isRoomClick = false;
    return;
  }
  const raycaster = getIntersects(event);

  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  const arr = getIntersectsArray(raycaster);

  if (arr.length > 0 && controls.enabled == false) {
    document.body.style.cursor = "pointer";
  } else if (arr.length === 0 || controls.enabled) {
    document.body.style.cursor = "default";
  }

  if (isCreating.isSelect && isCreating.isDragging) {
    // 바닥 그리기
    const obj = scene.getObjectByName(shadowName);
    if (!obj) return;

    updateShadows({ object: obj, background });
    return;
  } else if (
    isCreating.target === chairName ||
    isCreating.target === deskName
  ) {
    // 의자, 책상 등 오브젝트 만들기
    const floor = raycaster.find((obj) => obj.object.name === floorName);
    if (!floor) return;
    const point = new THREE.Vector3(floor.point.x, roomY, floor.point.z);
    const object = scene.getObjectByName(shadowName);
    object.visible = true;
    object.position.set(point.x, roomY, point.z);
    return;
  } else if (isChangingObject.isDragging) {
    // 방 넓이 변경
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    const background = raycaster.find(
      (obj) => obj.object.name === "background"
    );
    if (!background) return;
    const point = new THREE.Vector3(
      background.point.x,
      roomY,
      background.point.z
    );
    room.updateLines({
      point,
      cameraZoom,
      circleIdx: isChangingObject.circleIdx,
      circleId: isChangingObject.circleId,
    });
  } else if (isChangingObject.isDBClick) {
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    const points = new THREE.Vector3(
      background.point.x,
      roomY,
      background.point.z
    );
    const moveController = scene.getObjectByName(moveConrollerName);
    const rotationController = scene.getObjectByName(rotationConrollerName);
    if (moveController.isDragging) {
      moveController.onMouseMove({ room, points, rotationController });
    } else if (
      !rotationController.isDragging &&
      arr.length > 0 &&
      arr.find((ob) => ob.object.name.includes("controller"))
    ) {
      rotationController.onMouseMove({ points });
    } else if (rotationController.isDragging) {
      rotationController.onMouseMove({ points, room });
    }
  }
};

const onMouseUp = () => {
  if (controls.enabled) return;
  if (isRoomClick.isClick) {
    // 방 옮기기
    const room = isRoomClick.object.parent;
    const center = calculateCenter(room.getShadowPoints());
    const moveController = scene.getObjectByName(moveConrollerName);
    const controller = scene.getObjectByName(rotationConrollerName);
    moveController.setPosition(center);
    moveController.visible = true;
    controller.setPosition(center);
    controller.visible = true;
    isChangingObject = {
      ...isChangingObject,
      changingObjectId: room.id,
      isDBClick: true,
      isDragging: false,
    };
  }
  if (isChangingObject.isDragging) {
    //circle을 통해 방 모양 변경
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    room.onMouseUp({ cameraZoom });

    isChangingObject = {
      ...isChangingObject,
      circleId: null,
      isDragging: false,
      circleIdx: null,
    };
    // return;
  }
  const moveController = scene.getObjectByName(moveConrollerName);
  const controller = scene.getObjectByName(rotationConrollerName);
  if (moveController.isDragging) {
    moveController.onMouseUp();
  }
  if (controller.isDragging) {
    // 컨트롤러로 방 회전
    controller.onMouseUp();
  }
  isDragging = false;
};

const wheelThrottle = () => {
  const rooms = scene.children.filter((obj) => obj.name === "room");
  rooms.forEach((room) => {
    room.children.forEach((child) => {
      if (child.name === "area") {
        child.visible = false;
      }
    });
  });
};
const wheelDebounce = () => {
  const scaler = (minZoom / maxZoom) * cameraZoom * 0.2;
  const rotationController = scene.getObjectByName(rotationConrollerName);
  rotationController.setScale({
    object: rotationController,
    scaler: scaler * 5,
  });
  const rooms = scene.children.filter((obj) => obj.name === "room");
  rooms.forEach((room) => {
    room.children.forEach((child) => {
      if (child.name === "area") {
        child.scale.set(scaler, scaler, scaler);
        child.visible = true;
      }
    });
  });
};

const debounceWheel = debounce(wheelDebounce, 200);
const throttleWheel = throttle(wheelThrottle, 100);

const onWheel = (event) => {
  if (controls.enabled) return;
  throttleWheel();
  const delta = event.deltaY;
  const newZoom = Math.min(maxZoom, Math.max(minZoom, cameraZoom - delta));
  cameraZoom = newZoom;
  camera.position.y = newZoom;
  camera.lookAt(camera.position.x, 0, camera.position.z);
  debounceWheel();
};

const isFunitureLoadSuccess = (id) => {
  // gltf파일이 잘 로드되엇나 확인
  return furnitureObjects[id].status === "fulfilled";
};

const onCreateBtnClick = async (e) => {
  e.stopPropagation();
  const btn = e.target.closest("button");
  if (!btn) return;
  isCreating = {
    isSelect: false,
    isDragging: false,
    target: null,
  };

  // return;
  const { id } = btn;
  if (furnitureObjects.hasOwnProperty(id)) {
    if (!isFunitureLoadSuccess(id)) return;
    const funitureShadow = furnitureObjects[id].value.clone();
    funitureShadow.name = shadowName;
    scene.add(funitureShadow);
  }

  isCreating = {
    ...isCreating,
    isSelect: true,
    target: id,
  };
};

const create2DRoom = (room) => {
  const points = room.userData.points;
  const newRoom = new D2Room({ points });
  newRoom.name = "room";
  newRoom.createRoom({ cameraZoom });
  newRoom.userData = {
    ...room.userData,
  };
  room.children.forEach((child) => {
    if (furnitureObjects.hasOwnProperty(child.name)) {
      const newChild = child.clone();

      newRoom.add(newChild);
      newChild.userData = {
        ...child.userData,
      };
      newChild.position.copy(child.position);
    }
    newRoom.rotation.y = room.userData.rotation;
  });

  scene.add(newRoom);
};

const create2DScene = () => {
  if (!scene) return;
  const D3Objects = [];
  scene.children.forEach((obj) => {
    if (obj.name === "room") {
      create2DRoom(obj);
      scene.remove(obj);
    }
  });
  D3Objects.forEach((obj) => scene.remove(obj));
};

const create3DScene = () => {
  if (!scene) return;
  scene.children.forEach((obj) => {
    if (obj.name === "room") {
      const newRoom = new D3Room({ object: obj });
      scene.add(newRoom);
      scene.remove(obj);
    }
  });
};

const onChangeMode = (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;
  const { id } = btn;
  switch (id) {
    case "2D":
      if (!controls.enabled) return; //이미 2d라면 종료
      // D3Walls = [];
      camera.position.set(0, 250, 0);
      camera.lookAt(0, 0, 0);
      controls.enabled = false;
      create2DScene();
      return;
    case "3D":
      if (controls.enabled) return; //이미 3d라면 종료
      camera.lookAt(250, 250, 250);
      camera.position.set(250, 250, 250);
      controls.enabled = true;
      create3DScene();
  }
};

function dataURLtoBlob(dataURL) {
  const byteString = atob(dataURL.split(",")[1]);
  const mimeString = dataURL.split(",")[0].split(":")[1].split(";")[0];
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: mimeString });
}

const onSave = async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const project_id = urlParams.get("project_id");
  const dataEntities = saveFactory(scene);

  // 스크린샷 타입
  const strMime = "image/jpeg";
  // base64 썸네일
  const imageData = renderer.domElement.toDataURL(strMime);

  // base64 데이터를 Blob으로 변환
  const blobData = dataURLtoBlob(imageData);
  const userId = "123123";

  // FormData 객체 생성
  const formData = new FormData();

  // Blob 데이터와 파일명으로 파일 추가
  formData.append("file", blobData, `${userId}_${project_id}_screenshot.jpg`);
  console.log("project_id: ", project_id);
  const req = {
    project_id,
    dataEntities,
  };

  formData.append(
    "jsonData",
    new Blob([JSON.stringify(req)], {
      type: "application/json",
    })
  );

  try {
    const { data } = await axios.post(
      "http://localhost:8080/save/project",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );

    if (pDes) localStorage.removeItem("pdes");
    console.log(data);
  } catch (e) {
    console.error(e);
  }

  //
};

const onWindowResize = () => {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
};

const getProjectNode = async () => {
  if (window.location.origin.includes("3000")) return;
  const urlParams = new URLSearchParams(window.location.search);
  const project_id = urlParams.get("project_id");

  const data = await axios.get("/api/get/project_nodes", {
    params: {
      project_id,
    },
  });
  console.log(data);
};

getProjectNode();

canvas.addEventListener("mousedown", onMouseDown);
canvas.addEventListener("mousemove", onMouseMove);
canvas.addEventListener("mouseup", onMouseUp);
canvas.addEventListener("wheel", onWheel);
window.addEventListener("resize", onWindowResize);
window.addEventListener("beforeunload", () => {
  canvas.removeEventListener("mousedown", onMouseDown);
  canvas.removeEventListener("mousemove", onMouseMove);
  canvas.removeEventListener("mouseup", onMouseUp);
  canvas.removeEventListener("wheel", onWheel);

  localStorage.removeItem("pDes");
});

hudIcon.addEventListener("click", onCreateBtnClick);
save.addEventListener("click", onSave);
modeToggles.addEventListener("click", onChangeMode);
// 애니메이션 루프

const animate = () => {
  requestAnimationFrame(animate);
  composer.render();
  if (controls.enabled) {
    controls.update();
  }
  renderer.render(scene, camera);
};

if (!WEBGL.isWebGLAvailable()) {
  // webgl 가능한지 체크
  const warning = WEBGL.getWebGLErrorMessage();
  alert(warning);
} else {
  animate();
}
export const RotationControllerY = 3;
