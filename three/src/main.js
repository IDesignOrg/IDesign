"use strict";

import { THREE } from "./three.js";
import {
  getClickedCircleIndex,
  getCoordsFromVectex,
} from "./lib/calculater.js";
import { OrbitControls } from "./lib/three/OrbitControls.js";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import {
  D2Shapes,
  D3Shapes,
  create2DObject,
} from "./lib/three/geomentryFactory.js";
const hudIcon = document.getElementById("hud-icon");
const modeToggles = document.getElementById("modeToggles");

let D3Walls = [];

// zoom in/out & drag and drop
let isDragging = false;
let previousMousePosition = { x: 0, y: 0 };
const maxZoom = 500;
const minZoom = 50;
let cameraZoom = 250;

// create object
let isCreating = {
  isSelect: false,
  isDragging: false,
};

let isChangingObject = {
  isDBClick: false,
  isHover: false, //for mouse pointer and for clicked circle changer
  isDragging: false, //is changer circle moving ?
  changingObjectId: null, // change object...
  circleIdx: null, // 몇 번째 원을 통해 도형을 바꾸는지
};
const loader = new GLTFLoader();
console.log(loader);
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
const renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
camera.position.set(0, cameraZoom, 0);
camera.lookAt(0, 0, 0);
const controls = new OrbitControls(camera, renderer.domElement);
controls.enabled = false;
// controls.update();

// 기본적인 그리드 헬퍼 추가
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
scene.add(gridHelper);

const planeGeometry = new THREE.PlaneGeometry(2000, 2000);
const planeMaterial = new THREE.MeshBasicMaterial({
  color: "rgb(250,251,255)",
  side: THREE.FrontSide,
});
const plane = new THREE.Mesh(planeGeometry, planeMaterial);
plane.position.set(0, -1, 0);
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

// const updateCircles = ({ points, floor }) => {
//   const parent = floor.parent;
//   if (!parent) return;
//   const circles = parent.getObjectByName("circleGroup");
//   parent.remove(circles);
//   const new_circles = new Circles({ points });
//   new_circles.name = "circleGroup";
//   parent.add(new_circles);
// };

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
    new THREE.Vector3(originPoint.x, 1, originPoint.z),
    new THREE.Vector3(background.point.x, 1, originPoint.z),
    new THREE.Vector3(background.point.x, 1, background.point.z),
    new THREE.Vector3(originPoint.x, 1, background.point.z),
  ];
  parent.drawShadow({ points });
};

const onMouseDown = (event) => {
  if (controls.enabled) return;
  let points;
  const raycaster = getIntersects(event);
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  switch (true) {
    case isChangingObject.isDBClick:
      // 오브젝트의 넓이를 드래그(circle)을 통해 옮기고 있는지
      const { changingObjectId } = isChangingObject;
      const floor = scene
        .getObjectById(changingObjectId)
        .children.find((obj) => obj.name === "floor");
      if (!floor) return;
      const circleIdx = getClickedCircleIndex({
        background,
        floor,
      });
      if (circleIdx === null) return;
      isChangingObject = {
        ...isChangingObject,
        isDragging: true,
        circleIdx,
      };
      return;

    case isCreating.isSelect && isCreating.isDragging:
      // not shadow. chaging shadow object to real object
      const obj = scene.getObjectByName("shadow");
      obj.name = "room";
      isCreating = {
        isSelect: false,
        isDragging: false,
      };
      return;

    case isCreating.isSelect:
      // create shadow via dragging
      if (!background) return;
      points = [
        new THREE.Vector3(background.point.x, 1, background.point.z),
        new THREE.Vector3(background.point.x + 20, 1, background.point.z),
        new THREE.Vector3(background.point.x + 20, 1, background.point.z + 20),
        new THREE.Vector3(background.point.x, 1, background.point.z + 20),
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

    default:
      isDragging = true;
      previousMousePosition = { x: event.clientX, y: event.clientY };
      return;
  }
};

const changingSize = (raycaster) => {
  // const background =
  const { circleIdx } = isChangingObject;
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  const { changingObjectId } = isChangingObject; //
  const room = scene.getObjectById(changingObjectId);
  const points = new THREE.Vector3(background.point.x, 1, background.point.z);
  room.updateFloor({ points, circleIdx });
  return;
};

const onMouseMove = (event) => {
  if (controls.enabled) return;
  const raycaster = getIntersects(event);
  switch (true) {
    case isChangingObject.isDBClick && isChangingObject.isDragging:
      changingSize(raycaster);
      return;

    case isChangingObject.isDBClick:
      const circle = raycaster.find((obj) => obj.object.name === "circle");
      if (!circle) {
        //   오브젝트(room)의 체인징 원에 커서를 안올려놨을 경우
        document.body.style.cursor = "default";
      } else {
        //   오브젝트(room)의 체인징 원에 커서를 올려놨을 경우
        isChangingObject = {
          ...isChangingObject,
        };
        document.body.style.cursor = "pointer";
      }
      return;

    case isCreating.isSelect && isCreating.isDragging:
      const background = raycaster.find(
        (obj) => obj.object.name === "background"
      );
      if (!background) return;
      const obj = scene.getObjectByName("shadow");
      if (!obj) return;

      updateShadows({ object: obj, background });
      return;

    case isDragging:
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
    default:
      return;
  }
};

const onDoubleClick = (event) => {
  if (controls.enabled) return;
  const raycaster = getIntersects(event);
  const floor = raycaster.find((obj) => obj.object.name === "floor");
  if (!floor) return;
  const circleGroup = floor.object.parent.children.find(
    (obj) => obj.name === "circleGroup"
  );
  if (!circleGroup) return;
  circleGroup.visible = true;

  isChangingObject = {
    ...isChangingObject,
    changingObjectId: floor.object.parent.id,
    isDBClick: true,
  };
};

const onMouseUp = () => {
  if (controls.enabled) return;
  if (isChangingObject.isDBClick && isChangingObject.isDragging) {
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

const onCreate = (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;
  isCreating = {
    ...isCreating,
    isSelect: true,
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

hudIcon.addEventListener("click", onCreate);
modeToggles.addEventListener("click", onChangeMode);
// 애니메이션 루프
const animate = () => {
  // controls.update();
  requestAnimationFrame(animate);
  if (controls.enabled) {
    controls.update();
  }
  D3Walls.forEach((w) => {
    w.visible =
      currentPosition.copy(w.position).sub(camera.position).lengthSq() >
      camera.position.lengthSq();
  });
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
