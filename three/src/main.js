"use strict";

import { THREE } from "./three.js";
import {
  getClickedCircleIndex,
  getCoordsFromVectex,
} from "./lib/calculater.js";
import { OrbitControls } from "./lib/three/OrbitControls.js";

import {
  D2Shapes,
  D3Shapes,
  create2DObject,
} from "./lib/three/geomentryFactory.js";
import { chairCreator, createDesk } from "./lib/gltfObjects/ObjectFactory.js";
import { EffectComposer } from "three/examples/jsm/postprocessing/EffectComposer.js";
import { RenderPass } from "three/examples/jsm/postprocessing/RenderPass.js";
import { OutlinePass } from "three/examples/jsm/postprocessing/OutlinePass.js";
import { ShaderPass } from "three/examples/jsm/postprocessing/ShaderPass.js";
import { FXAAShader } from "three/examples/jsm/shaders/FXAAShader.js";
import { Shape } from "./lib/three/objects/floor.js";
import { zzz } from "./lib/three/objects/wall.js";

// const wallpaper = "../public/img/wallpaper.png";
const wallpaper = "../public/img/wallpaper.png";
// const wallpaper = new Image();

export const floorY = 2;

const hudIcon = document.getElementById("hud-icon");
const modeToggles = document.getElementById("modeToggles");

let D3Walls = [];

// zoom in/out & drag and drop
let isDragging = false;
let previousMousePosition = { x: 0, y: 0 };
const maxZoom = 500;
const minZoom = 5;
let cameraZoom = 250;

// create object
let isCreating = {
  isSelect: false,
  isDragging: false,
  target: null,
};

let isChangingObject = {
  isDBClick: false,
  isHover: false, //for mouse pointer and for clicked circle changer
  isDragging: false, //is changer circle moving ?
  changingObjectId: null, // change object...
  circleIdx: null, // 몇 번째 원을 통해 도형을 바꾸는지
  name: null,
};

let currentPosition = new THREE.Vector3();
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
const ambientLight = new THREE.AmbientLight(0xffffff, 0.5); // 전역 조명
scene.add(ambientLight);
const pointLight = new THREE.PointLight(0xffffff, 0.5); // 포인트 조명
pointLight.position.set(50, 50, 50);
scene.add(pointLight);

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
//
//
//

// Shape 정의

// const shape = new THREE.Shape();
// const points = [
//   { x: 0, y: 1, z: 0 },
//   { x: 100, y: 1, z: 0 },
//   { x: 100, z: 100, y: 1 },
//   { x: 0, y: 1, z: 100 },
// ];
// shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
// for (let i = 1; i < points.length; i++) {
//   shape.lineTo(points[i].x, -points[i].z);
// }
// shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

// // Geometry 생성
// const geometry = new THREE.ShapeGeometry(shape);

// Texture 로드
const texture = new THREE.TextureLoader().load(
  wallpaper,
  function () {
    console.log("Texture loaded successfully");
  }, // onLoad
  undefined, // onProgress
  function (err) {
    console.error("An error happened while loading the texture", err);
  } // onError
);

// Texture wrapping 설정
texture.wrapS = THREE.RepeatWrapping;
texture.wrapT = THREE.RepeatWrapping;
texture.repeat.set(50, 50); // 이미지 반복 횟수 설정 (필요에 따라 조정)

// Material 생성
export const material = new THREE.MeshBasicMaterial({ map: texture });

// Mesh 생성
// const object = new THREE.Mesh(geometry, material);
// object.rotation.x = -Math.PI / 2;
// object.position.set(0, 0, 0);
// scene.add(object);
//
//
// //
//
//
const planeGeometry = new THREE.PlaneGeometry(2000, 2000);
const planeMaterial = new THREE.MeshBasicMaterial({
  color: "rgb(250,251,255)",
  side: THREE.FrontSide,
});
const plane = new THREE.Mesh(planeGeometry, planeMaterial);
plane.position.set(0, -2, 0);
plane.rotation.x = -Math.PI / 2; // x축을 기준으로 90도 회전
plane.name = "background";
scene.add(plane);

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
      obj.object.name === "floor" ||
      obj.object.name === "desk" ||
      obj.object.name === "Desk" ||
      obj.object.name === "chair" ||
      obj.object.name === "circle"
  );
};

const updateShadows = ({ object, background }) => {
  //object = grop object..
  const floor = object.children.find((obj) => obj.name === "floor");
  if (!floor) return;
  const coordsArr = getCoordsFromVectex(floor);
  const originPoint = coordsArr[0];
  if (!originPoint) return;
  const parent = floor.parent;
  if (!parent) return;
  const points = [
    new THREE.Vector3(originPoint.x, floorY, originPoint.z),
    new THREE.Vector3(background.point.x, floorY, originPoint.z),
    new THREE.Vector3(background.point.x, floorY, background.point.z),
    new THREE.Vector3(originPoint.x, floorY, background.point.z),
  ];
  console.log(points);
  parent.drawShadow({ points });
};

const onMouseDown = (event) => {
  // 3차원시 리턴
  if (controls.enabled) return;
  let points;
  const raycaster = getIntersects(event);
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  if (isChangingObject.isDBClick) {
    const { name, changingObjectId } = isChangingObject;
    switch (name) {
      case "floor":
        const parent = scene.getObjectById(changingObjectId);
        const floor = parent.getObjectByName("floor");
        const circleIdx = getClickedCircleIndex({
          background,
          floor,
        });
        if (!circleIdx) return;
        isChangingObject = {
          ...isChangingObject,
          isDragging: true,
          circleIdx,
        };
        break;
    }

    return;
  }
  if (isCreating.isSelect) {
    // 버튼(hud-icon)을 통해 무언가를 만드는 중
    const { target, isDragging } = isCreating;
    switch (target) {
      case "floor":
        if (isDragging) {
          // floor를 만드는 중
          const obj = scene.getObjectByName("shadow");
          const floor = obj.getObjectByName("floor");

          floor.material = material;

          obj.name = "room";
          isCreating = {
            isSelect: false,
            isDragging: false,
            target: null,
          };
        } else {
          points = [
            new THREE.Vector3(background.point.x, floorY, background.point.z),
            new THREE.Vector3(
              background.point.x + 20,
              floorY,
              background.point.z
            ),
            new THREE.Vector3(
              background.point.x + 20,
              floorY,
              background.point.z + 20
            ),
            new THREE.Vector3(
              background.point.x,
              floorY,
              background.point.z + 20
            ),
          ];
          const room = create2DObject({
            points,
            name: "shadow",
            objectName: "room",
          });
          scene.add(room);
          isCreating = {
            ...isCreating,
            isDragging: true,
          };
          return;

          // floor를 만들기 시작
        }
        break;
      case "chair":
      case "desk":
        const floor = raycaster.find((obj) => obj.object.name === "floor");
        if (!floor) return;
        const room = floor.object.parent;
        const object = scene.getObjectByName("shadow");
        object.name = target;
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
    return;
  }
  const objectArr = getIntersectsArray(raycaster);
  if (objectArr.length > 0) {
    const name = objectArr[0].object.name;
    const { object } = objectArr[0];
    // return;
    switch (name) {
      case "floor":
        const circleGroup = object.parent.getObjectByName("circleGroup");
        if (!circleGroup) break;
        circleGroup.visible = true;
        isChangingObject = {
          ...isChangingObject,
          changingObjectId: object.parent.id,
          isDBClick: true,
          name,
        };
        break;
    }
    return;
  } else {
    isChangingObject = {
      isDBClick: false,
      isHover: false, //for mouse pointer and for clicked circle changer
      isDragging: false, //is changer circle moving ?
      changingObjectId: null, // change object...
      circleIdx: null, // 몇 번째 원을 통해 도형을 바꾸는지
      name: null,
    };
  }
  isDragging = true;
  previousMousePosition = { x: event.clientX, y: event.clientY };
  return;

  // let points;
  // const raycaster = getIntersects(event);
  // const background = raycaster.find((obj) => obj.object.name === "background");
  // if (!background) return;
  // const { target } = isCreating;
  // if (target === "chair" || target === "desk") {
  //   const floor = raycaster.find((obj) => obj.object.name === "floor");
  //   if (!floor) return;
  //   const room = floor.object.parent;
  //   const chair = scene.getObjectByName("shadow");
  //   chair.name = isCreating.target;
  //   room.add(chair);
  //   isCreating = {
  //     isSelect: false,
  //     isDragging: false,
  //     target: null,
  //   };
  // } else {
  //   switch (true) {
  //     case isChangingObject.isDBClick:
  //       // 오브젝트의 넓이를 드래그(circle)을 통해 옮기고 있는지
  //       const { changingObjectId } = isChangingObject;
  //       const floor = scene
  //         .getObjectById(changingObjectId)
  //         .children.find((obj) => obj.name === "floor");
  //       if (!floor) return;
  // const circleIdx = getClickedCircleIndex({
  //   background,
  //   floor,
  // });
  //       if (circleIdx === null) return;
  // isChangingObject = {
  //   ...isChangingObject,
  //   isDragging: true,
  //   circleIdx,
  // };
  //       return;

  //     case isCreating.isSelect && isCreating.isDragging:
  //       // 그림자가 아닌 실제 물체 놓기
  //       const obj = scene.getObjectByName("shadow");
  //       obj.name = "room";
  //       isCreating = {
  //         isSelect: false,
  //         isDragging: false,
  //       };
  //       return;

  //     case isCreating.isSelect:
  //       // 드래깅으로 바닥 그림자 그리기
  //       if (!background) return;
  // points = [
  //   new THREE.Vector3(background.point.x, 1, background.point.z),
  //   new THREE.Vector3(background.point.x + 20, 1, background.point.z),
  //   new THREE.Vector3(
  //     background.point.x + 20,
  //     1,
  //     background.point.z + 20
  //   ),
  //   new THREE.Vector3(background.point.x, 1, background.point.z + 20),
  // ];
  // const room = create2DObject({
  //   points,
  //   name: "shadow",
  //   objectName: "room",
  // });
  // scene.add(room);
  // isCreating = {
  //   ...isCreating,
  //   isDragging: true,
  // };
  // return;

  //     default:
  //       isDragging = true;
  //       previousMousePosition = { x: event.clientX, y: event.clientY };
  //       return;
  //   }
  // }
};

const changingSize = (raycaster) => {
  // const background =
  const { circleIdx } = isChangingObject;
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  const { changingObjectId } = isChangingObject; //
  const room = scene.getObjectById(changingObjectId);
  const points = new THREE.Vector3(
    background.point.x,
    floorY,
    background.point.z
  );
  room.updateFloor({ points, circleIdx });
  return;
};

const onMouseMove = (event) => {
  const raycaster = getIntersects(event);
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
    return;
  }

  if (isCreating.isSelect && isCreating.isDragging) {
    // 바닥 그리기
    const background = raycaster.find(
      (obj) => obj.object.name === "background"
    );
    if (!background) return;
    const obj = scene.getObjectByName("shadow");
    if (!obj) return;

    updateShadows({ object: obj, background });
    return;
  }

  if (isCreating.target === "chair" || isCreating.target === "desk") {
    // 의자, 책상 등 오브젝트 만들기
    const floor = raycaster.find((obj) => obj.object.name === "floor");
    if (!floor) return;
    const point = new THREE.Vector3(floor.point.x, floorY, floor.point.z);
    const object = scene.getObjectByName("shadow");
    object.visible = true;
    object.position.set(point.x, floorY, point.z);
    return;
  }

  const arr = getIntersectsArray(raycaster);

  if (arr.length > 0) {
    document.body.style.cursor = "pointer";
  } else if (arr.length === 0) {
    document.body.style.cursor = "default";

    // outlinePass.selectedObjects = [];
  }
  if (isChangingObject.isDragging) {
    changingSize(raycaster);
    return;
  }

  // outlinePass
  // outlinePass.selectedObjects = [arr[0].object];
  // console.log(outlinePass, arr[0], outlinePass.selectedObjects);

  // // const floo
  // else {
  //   // 바닥을 만들거나 화면을 옮기거나
  //   if (controls.enabled) return; //3D라면 종료

  //   switch (true) {
  //     case isChangingObject.isDBClick && isChangingObject.isDragging:
  //       changingSize(raycaster);
  //       return;

  //     case isChangingObject.isDBClick:
  //       const circle = raycaster.find((obj) => obj.object.name === "circle");
  //       if (!circle) {
  //         //   오브젝트(room)의 체인징 원에 커서를 안올려놨을 경우
  //         document.body.style.cursor = "default";
  //       } else {
  //         //   오브젝트(room)의 체인징 원에 커서를 올려놨을 경우
  //         isChangingObject = {
  //           ...isChangingObject,
  //         };
  //         document.body.style.cursor = "pointer";
  //       }
  //       return;

  //     case isCreating.isSelect && isCreating.isDragging:
  //       const background = raycaster.find(
  //         (obj) => obj.object.name === "background"
  //       );
  //       if (!background) return;
  //       const obj = scene.getObjectByName("shadow");
  //       if (!obj) return;

  //       updateShadows({ object: obj, background });
  //       return;

  //     case isDragging:
  //       const deltaMove = {
  //         x: event.clientX - previousMousePosition.x,
  //         y: event.clientY - previousMousePosition.y,
  //       };

  //       // 카메라 위치 업데이트
  //       camera.position.x -= deltaMove.x / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
  //       camera.position.z -= deltaMove.y / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
  //       camera.lookAt(camera.position.x, 0, camera.position.z);

  //       previousMousePosition = { x: event.clientX, y: event.clientY };
  //       return;
  //     default:
  //       return;
  //   }
  // }
};

const onDoubleClick = (event) => {
  // return;
  // if (controls.enabled) return;
  // const raycaster = getIntersects(event);
  // const arr = raycaster.filter(
  //   (obj) =>
  //     obj.object.name === "desk" ||
  //     obj.object.name === "floor" ||
  //     obj.object.name === "chair" ||
  //     obj.object.name === "Desk"
  // );
  // if (arr[0].object.name === "chair") {
  //   const desk = arr[0].object;
  //   isChangingObject = {
  //     changingObjectId: desk.id,
  //     isDBClick: true,
  //   };
  // } else if (arr[0].object.name === "floor") {
  //   // 바닥 수정
  //   // const floor = raycaster.find((obj) => obj.object.name === "floor");
  //   const floor = arr[0].object;
  //   if (!floor) return;
  //   const circleGroup = floor.parent.children.find(
  //     (obj) => obj.name === "circleGroup"
  //   );
  //   if (!circleGroup) return;
  //   circleGroup.visible = true;
  //   isChangingObject = {
  //     ...isChangingObject,
  //     changingObjectId: floor.parent.id,
  //     isDBClick: true,
  //   };
  // }
};

const onMouseUp = () => {
  if (controls.enabled) return;
  if (isChangingObject.isDBClick && isChangingObject.isDragging) {
    const obj = scene.getObjectById(isChangingObject.changingObjectId);
    const floor = obj.getObjectByName("floor");
    floor.material = material;
    isChangingObject = {
      ...isChangingObject,
      isDragging: false,
      circleIdx: null,
    };
  }
  isDragging = false;
};

const onWheel = (event) => {
  if (controls.enabled) return;
  const delta = event.deltaY;
  const newZoom = Math.min(maxZoom, Math.max(minZoom, cameraZoom - delta));
  cameraZoom = newZoom;
  camera.position.y = newZoom;
  camera.lookAt(camera.position.x, 0, camera.position.z);
};

const onCreateBtnClick = async (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;
  isCreating = {
    isSelect: false,
    isDragging: false,
    target: null,
  };

  const { id } = btn;
  if (id === "chair") {
    const chair = await chairCreator();
    chair.name = "shadow";
    chair.visible = false;
    scene.add(chair);
  } else if (id === "desk") {
    const desk = await createDesk();
    desk.name = "shadow";
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
    if (obj.name === "background" || obj.name === "helper") return;
    D3Objects.push(obj);
    scene.add(D2Shapes({ object: obj }));
  });
  D3Objects.forEach((obj) => scene.remove(obj));
};

const create3DScene = () => {
  if (!scene) return;
  const D3Objects = [];
  const D2Objects = [];
  scene.children.forEach((obj) => {
    if (obj.name === "background" || obj.name === "helper") return;
    D2Objects.push(obj);
    D3Objects.push(D3Shapes({ object: obj, scene }));
  });
  D2Objects.forEach((obj) => scene.remove(obj));
  D3Objects.forEach((obj) => {
    const walls = obj.children.find((child) => child.name === "walls");
    if (walls) {
      walls.children.forEach((wall) => D3Walls.push(wall));
    }
    scene.add(obj);
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
      return;
  }
};

// 이벤트 리스너 추가
window.addEventListener("mousedown", onMouseDown);
window.addEventListener("mousemove", onMouseMove);
window.addEventListener("mouseup", onMouseUp);
window.addEventListener("wheel", onWheel);
window.addEventListener("dblclick", onDoubleClick);

hudIcon.addEventListener("click", onCreateBtnClick);
modeToggles.addEventListener("click", onChangeMode);
// 애니메이션 루프
const animate = () => {
  // controls.update();
  requestAnimationFrame(animate);
  composer.render();
  if (controls.enabled) {
    controls.update();
  }
  // D3Walls.forEach((w) => {
  //   w.visible =
  //     currentPosition.copy(w.position).sub(camera.position).lengthSq() >
  //     camera.position.lengthSq();
  // });
  renderer.render(scene, camera);
};

animate();

// import { TwoDigitStart } from "./PlaneThree.js";
// import { startFrame } from "./frame.js";
// import { WEBGL } from "./lib/webgl.js";
// import { threeJS } from "./three.js";
// // import * as THREE from "./lib/three.module.js";
// // import { OrbitControls } from "./lib/OrbitControls.js";
// // import { Desk } from "./lib/objects/desk.js";
// // import { Room } from "./lib/objects/Room.js";
// // import {
// //   deskName,
// //   floorName,
// //   shadowName,
// //   groundName,
// //   ceilingName,
// //   resizerName,
// // } from "./lib/objectConf/objectNames.js";
// // import {
// //   deskColor,
// //   floorColors,
// //   shadowColor,
// // } from "./lib/objectConf/colors.js";
// // import { InitObjects } from "./lib/objects/sceneObjects.js";
// // import { Floor } from "./lib/objects/floor.js";

// if (WEBGL.isWebGLAvailable()) {
//   TwoDigitStart();
//   // startFrame();
//   // threeJS();
// } else {
//   const warning = WEBGL.getWebGLErrorMessage();
//   document.body.appendChild(warning);
// }

export { scene };
