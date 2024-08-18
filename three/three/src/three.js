"use strict";

import axios from "axios";

import { EffectComposer } from "three/examples/jsm/postprocessing/EffectComposer.js";
import { RenderPass } from "three/examples/jsm/postprocessing/RenderPass.js";
import { OutlinePass } from "three/examples/jsm/postprocessing/OutlinePass.js";
import { ShaderPass } from "three/examples/jsm/postprocessing/ShaderPass.js";
import { FXAAShader } from "three/examples/jsm/shaders/FXAAShader.js";

import { D2Shapes, D3Shapes } from "./lib/three/geomentryFactory.js";
import { D2Room } from "./lib/objects/Room.js";
import { THREE } from "./lib/loader/three.js";
import { debounce } from "./lib/debounce.js";
import { create3DRoom } from "./lib/Dimension/dimension.js";
import { chairCreator, createChair } from "./lib/gltfObjects/ObjectFactory.js";
import { RotationController } from "./lib/objects/rotationController.js";
import { calculateCenter, getClickedCircleIndex } from "./lib/calculater.js";
import { OrbitControls } from "./lib/loader/OrbitControls.js";
import { WEBGL } from "./lib/webgl.js";
import {
  backgroundName,
  chairName,
  circleGroupName,
  circleName,
  deskName,
  floorName,
  moveConrollerName,
  moveControllerChildrenName,
  roomName,
  rotationConrollerName,
  shadowName,
} from "./lib/objectConf/objectNames.js";
import { roomY } from "./lib/objectConf/renderOrders.js";
import { MoveController } from "./lib/objects/moveController.js";
import { throttle } from "./lib/throttling.js";

export const MILLPerWidth = 0.1;

const save = document.getElementById("save");
const hudIcon = document.getElementById("hud-icon");
const modeToggles = document.getElementById("modeToggles");
const canvas = document.getElementById("canvas");
console.log("canvas = ", canvas);

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
};

const mouse = new THREE.Vector2();
const raycaster = new THREE.Raycaster();
const scene = new THREE.Scene();
scene.background = new THREE.Color("rgb(250,251,255)");
const camera = new THREE.PerspectiveCamera(
  75,
  window.innerWidth / window.innerHeight,
  0.1,
  10000
);
const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
camera.position.set(0, cameraZoom, 0);
camera.lookAt(0, 0, 0);
const controls = new OrbitControls(camera, renderer.domElement);
controls.enabled = false;
// EffectComposer 및 OutlinePass 설정
const composer = new EffectComposer(renderer);
const renderPass = new RenderPass(scene, camera);
composer.addPass(renderPass);

const floorPaper = "../public/img/floor.jpg";
const texture = new THREE.TextureLoader().load(
  floorPaper,
  function () {
    console.log("Texture loaded successfully");
  }, // onLoad
  undefined, // onProgress
  function (err) {
    console.error("An error happened while loading the texture", err);
  } // onError
);

const wallpaper = "../public/img/wallpaper.jpeg";
const texture2 = new THREE.TextureLoader().load(wallpaper);
export const wallMaterial = new THREE.MeshBasicMaterial({ map: texture2 });
export const floorMaterial = new THREE.MeshBasicMaterial({ map: texture });
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

// 아웃라인 설정
outlinePass.edgeStrength = 3.0;
outlinePass.edgeGlow = 1.0;
outlinePass.edgeThickness = 6;
outlinePass.pulsePeriod = 0;
outlinePass.visibleEdgeColor.set("yellow");
outlinePass.hiddenEdgeColor.set("red");

composer.addPass(outlinePass);
composer.addPass(effectFXAA);

const axes = new THREE.AxesHelper(1000);
axes.name = "helper";
scene.add(axes);
const gridHelper = new THREE.GridHelper(
  2000,
  200,
  "rgb(221,221,221)",
  "rgb(221,221,221)"
);
gridHelper.name = "helper";
gridHelper.position.y = -1;
scene.add(gridHelper);

const rotationController = new RotationController({ cameraZoom });
rotationController.setScale({ object: rotationController, scaler: 6 });
const moveController = new MoveController();
scene.add(moveController);
scene.add(rotationController);

const planeGeometry = new THREE.PlaneGeometry(2000, 2000);
const planeMaterial = new THREE.MeshBasicMaterial({
  color: "rgb(250,251,255)",
  side: THREE.FrontSide,
});
const plane = new THREE.Mesh(planeGeometry, planeMaterial);
plane.position.set(0, -2, 0);
plane.rotation.x = -Math.PI / 2; // x축을 기준으로 90도 회전
plane.name = backgroundName;
scene.add(plane);

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

const updateShadows = ({ object, background }) => {
  // const points = [
  //   new THREE.Vector3(object.clickedPoints.x, roomY, object.clickedPoints.z),
  //   new THREE.Vector3(background.point.x, roomY, object.clickedPoints.z),
  //   new THREE.Vector3(background.point.x, roomY, background.point.z),
  //   new THREE.Vector3(object.clickedPoints.x, roomY, background.point.z),
  // ];
  const y = roomY;
  const w = 500;
  const points = [
    { x: 0, y, z: 0 },
    { x: w, y, z: 0 },
    { x: w, y, z: w },
    { x: 0, y, z: w },
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

  if (
    !isChangingObject.isDBClick &&
    !isCreating.isSelect &&
    objectArr.length > 0
  ) {
    const name = objectArr[0].object.name;
    const { object } = objectArr[0];

    let room;
    switch (name) {
      case circleName:
        room = object.parent.parent;
        const cg = room.getObjectByName(circleGroupName);
        const clickedIdx = getClickedCircleIndex({ cg, object });
        if (clickedIdx === null) {
          isChangingObject = {
            isDBClick: false,
            isHover: false, //for mouse pointer and for clicked circle changer
            isDragging: false, //is changer circle moving ?
            changingObjectId: null, // change object...
            circleIdx: null, // 몇 번째 원을 통해 도형을 바꾸는지
            name: null,
          };
          break;
        }
        isChangingObject = {
          ...isChangingObject,
          changingObjectId: room.id,
          isDragging: true,
          circleIdx: clickedIdx,
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
  } else if (isChangingObject.isDBClick) {
    if (objectArr.length > 0) {
      // let controller = objectArr.find((obj) =>
      //   obj.object.name.incldues("controller")
      // );
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
          // 만들기 종료
          const obj = scene.getObjectByName(shadowName);

          obj.name = roomName;
          obj.createRoom({ cameraZoom });
          isCreating = {
            isSelect: false,
            isDragging: false,
            target: null,
          };
        } else {
          // floor를 만드는 중
          points = [
            new THREE.Vector3(background.point.x, roomY, background.point.z),
            new THREE.Vector3(
              background.point.x + 20,
              roomY,
              background.point.z
            ),
            new THREE.Vector3(
              background.point.x + 20,
              roomY,
              background.point.z + 20
            ),
            new THREE.Vector3(
              background.point.x,
              roomY,
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

          // floor를 만들기 시작
        }
        break;
      case chairName:
      case deskName:
        let floor = raycaster.find((obj) => obj.object.name === "floor");
        if (!floor) return;
        const room = floor.object.parent;
        let position = floor.point;
        floor = floor.object;
        const object = scene.getObjectByName(shadowName);
        object.name = target;
        const center = room.userData.center;
        const localClickPosition = new THREE.Vector3(10, 0, 0);

        // floor의 월드 행렬을 사용하여 로컬 좌표를 월드 좌표로 변환합니다
        const worldClickPosition = localClickPosition
          .clone()
          .applyMatrix4(floor.matrixWorld);
        console.log(worldClickPosition);
        object.position.x = position.x - center.x;
        object.position.z = position.z - center.z;

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
  }
  isDragging = true;
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
    console.log(point);
    object.position.set(point.x, roomY, point.z);
    return;
  } else if (isChangingObject.isDragging) {
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
    room.updateLines({ point, circleIdx: isChangingObject.circleIdx });
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
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    room.onMouseUp({ cameraZoom });

    isChangingObject = {
      ...isChangingObject,
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
        const box = new THREE.Box3();
        box.setFromObject(child);
        const width = box.max.x - box.min.x;
        const height = box.max.y - box.min.y;
        // const
        child.position.set(-width / 2, child.position.y, -height / 2);
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

const onCreateBtnClick = async (e) => {
  e.stopPropagation();
  const btn = e.target.closest("button");
  if (!btn) return;
  isCreating = {
    isSelect: false,
    isDragging: false,
    target: null,
  };

  const { id } = btn;

  if (id === chairName) {
    const chair = await createChair();
    chair.name = shadowName;
    chair.visible = false;
    scene.add(chair);
  } else if (id === deskName) {
    const desk = await createDesk();
    desk.name = shadowName;
    desk.visible = false;
    scene.add(desk);
  }

  isCreating = {
    ...isCreating,
    isSelect: true,
    target: id,
  };
};

const create2DScene = () => {
  if (!scene) return;
  const D3Objects = [];
  scene.children.forEach((obj) => {
    if (obj.name === "room") {
      D3Objects.push(obj);
      scene.add(D2Shapes({ object: obj, cameraZoom }));
    }
  });
  D3Objects.forEach((obj) => scene.remove(obj));
};

const create3DScene = () => {
  if (!scene) return;
  scene.children.forEach((obj) => {
    if (obj.name === "room") {
      const newRoom = create3DRoom(obj);
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
      if (!controls.enabled) return;
      D3Walls = [];
      camera.position.set(0, 250, 0);
      camera.lookAt(0, 0, 0);
      controls.enabled = false;
      create2DScene();
      return;
    case "3D":
      if (controls.enabled) return;
      camera.lookAt(50, 50, 50);
      camera.position.set(50, 50, 50);
      controls.enabled = true;
      create3DScene();
  }
};

const onSave = async () => {
  const reqData = {
    id: "example_userID",
    project_id: new Date().getTime(),
    data: {},
  };

  // const room = scene.getObjectByName("room");

  // const data = {};

  // function def(obj, parent) {
  //   data[obj.id] = {
  //     oid: obj.id,
  //     parent: parent.id,
  //     type: obj.name,
  //     children:
  //       obj.children.length === 0 || obj.children === null
  //         ? null
  //         : obj.children.map((c) => c.id),
  //     rotation: obj.userData.rotation ? obj.userData.rotation : 0,
  //     angle: obj.userData.rotation ? obj.userData.rotation : 0,
  //     points: obj.userData.points,
  //   };

  //   if (obj.children.length === 0 || obj.children === null) {
  //   } else {
  //     obj.children.forEach((o) => def(o, obj));
  //   }
  // }

  // def(room, scene);

  // {
  //   uuid: string,
  //     project_id: string,
  //     mod_dt: Date,
  //     reg_dt: Date,
  //   // 기타 데이터 저장에 필요한 것들(3D 관련x)
  //     data: {
  //     oid: string, //object id
  //       type: string,
  //       points: [
  //         { x: float, y: float, z: float }, {
  //           ...
  //         } ...
  //       ],
  //       children: null or  string /* oid)*/ or {...data }, /* data항목과 일치함 */
  //     parent:  null or  string  or {...data }, /* data항목과 일치함 */
  //     rotation: float or Double, //아마 더블
  //       angle: float or Double,
  //       }
  // }

  // const data = await axios.post("127.0.0.1/uri", { ...reqData });
  // console.log(data);
};

window.addEventListener("mousedown", onMouseDown);
window.addEventListener("mousemove", onMouseMove);
window.addEventListener("mouseup", onMouseUp);
window.addEventListener("wheel", onWheel);
window.addEventListener("beforeunload", () => {
  window.removeEventListener("mousedown", onMouseDown);
  window.removeEventListener("mousemove", onMouseMove);
  window.removeEventListener("mouseup", onMouseUp);
  window.removeEventListener("wheel", onWheel);
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
  document.body.appendChild(warning);
} else {
  animate();
}
export const RotationControllerY = 3;
