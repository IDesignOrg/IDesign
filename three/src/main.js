"use strict";

import { THREE } from "./three.js";
import {
  calculateCenter,
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
import { D2Room } from "./lib/three/objects/Room.js";
import { D2Floor } from "./lib/three/objects/floor.js";
// import { RotationController } from "./lib/three/objects/RotationController.js";

// const wallpaper = "../public/img/wallpaper.png";
const wallpaper = "../public/img/wallpaper.png";
// const wallpaper = new Image();

export const floorY = 2;
let zzzzz = [];

const hudIcon = document.getElementById("hud-icon");
const modeToggles = document.getElementById("modeToggles");

let D3Walls = [];

// zoom in/out & drag and drop
let isDragging = false;
let previousMousePosition = { x: 0, y: 0 };
export const maxZoom = 500;
export const minZoom = 5;
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

//
//
//
//
//
const gro = new THREE.Group();
gro.position.set(10, 2, 10);
const pg = new THREE.PlaneGeometry(50, 50);
const m = new THREE.MeshBasicMaterial({
  color: 0xffff00,
  side: THREE.DoubleSide,
});
const p = new THREE.Mesh(pg, m);
// p.position.set(10, 2, 10);
p.position.y = 2;
p.rotation.x = Math.PI / 2;
gro.add(p);
scene.add(gro);

const pg2 = new THREE.PlaneGeometry(51, 51);
const m2 = new THREE.MeshBasicMaterial({
  color: "red",
  side: THREE.DoubleSide,
});
const p2 = new THREE.Mesh(pg2, m2);
p2.position.set(10, 2, 10);
p2.position.y = 2;
p2.rotation.x = Math.PI / 2;
scene.add(p2);
//
//
//
//
//
//
//
//
//
//
//
//

export class RotationController extends THREE.Group {
  constructor({ cameraZoom = 250 }) {
    super();
    this.name = "rotationController";
    this.isDragging = false;
    this.init(cameraZoom);
  }

  init = (cameraZoom) => {
    const innerRadius = 30;
    const outerRadius = 20;
    const thetaSegments = 30;
    const phiSegments = 8;
    const thetaStart = 1;
    const thetaLength = Math.PI * 2;

    // controllerBackground
    const RingGeometry = new THREE.RingGeometry(
      innerRadius,
      outerRadius,
      thetaSegments,
      phiSegments,
      thetaStart,
      thetaLength
    );
    const ringMaterial = new THREE.MeshBasicMaterial({
      color: "blue",
      side: THREE.DoubleSide,
      transparent: true,
      opacity: 0.5,
      depthWrite: false,
      depthTest: false,
    });
    const ring = new THREE.Mesh(RingGeometry, ringMaterial);
    ring.name = "controllerBackground";
    ring.rotation.x = -Math.PI / 2;
    ring.renderOrder = 1;
    this.setScale({ object: ring, cameraZoom });
    this.add(ring);

    // controllerRing
    const thetaLengthRinged = Math.PI / 3;
    const RingedGeometry = new THREE.RingGeometry(
      innerRadius,
      outerRadius,
      thetaSegments,
      phiSegments,
      thetaStart,
      thetaLengthRinged
    );
    const ringedMaterial = new THREE.MeshBasicMaterial({
      color: "red",
      side: THREE.DoubleSide,
      depthWrite: false,
      depthTest: false,
    });
    const controllerRing = new THREE.Mesh(RingedGeometry, ringedMaterial);
    controllerRing.name = "controllerRing";
    controllerRing.rotation.x = -Math.PI / 2;
    controllerRing.renderOrder = 2;
    this.setScale({ object: controllerRing, cameraZoom });
    this.add(controllerRing);
  };

  onMouseUp = () => {
    this.isDragging = false;
  };

  onMouseDown = () => {
    this.isDragging = true;
  };

  onMouseMove = ({ points }) => {
    const dx = points.x - this.position.x;
    const dz = points.z - this.position.z;

    // 각도 계산 (atan2 사용)
    const angle = Math.atan2(dz, dx) + Math.PI / 2; // rotation.x가 -Math.PI / 2이기 때문에 보정
    if (this.isDragging) {
      this.rotation({ angle });
      return;
    }
    const controllerRing = this.getObjectByName("controllerRing");

    // 마우스 위치 (points)에서 ring의 중심까지의 벡터 계산

    // controllerRing의 rotation.z 업데이트
    controllerRing.rotation.z = -angle;
    document.body.style.cursor = "pointer";
  };

  rotation = ({ angle }) => {
    const floor = this.parent.getObjectByName("floor");
    const copy = floor.position.clone();
    this.parent.rotation.y = angle;
  };

  setScale = ({ object, cameraZoom }) => {
    const scaleAmount = cameraZoom * (minZoom / maxZoom);
    object.scale.set(scaleAmount, scaleAmount, scaleAmount);
  };
}

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

// const box = new THREE.Box3();
// box.setFromCenterAndSize(
//   new THREE.Vector3(1, 1, 1),
//   new THREE.Vector3(2, 1, 3)
// );

// const helper = new THREE.Box3Helper(box, 0xffff00);
// scene.add(helper);
//

//
//
//

// const rotationController = new RotationController({ cameraZoom });
// scene.add(rotationController);

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
      obj.object.name === "floor" ||
      obj.object.name === "desk" ||
      obj.object.name === "Desk" ||
      obj.object.name === "chair" ||
      obj.object.name === "circle" ||
      obj.object.name.includes("controller")
  );
};

const updateShadows = ({ object, background }) => {
  const points = [
    new THREE.Vector3(object.clickedPoints.x, floorY, object.clickedPoints.z),
    new THREE.Vector3(background.point.x, floorY, object.clickedPoints.z),
    new THREE.Vector3(background.point.x, floorY, background.point.z),
    new THREE.Vector3(object.clickedPoints.x, floorY, background.point.z),
  ];
  console.log("last point = ", points);
  zzzzz = [...points];
  object.drawLines(points);
  // const floor = object.getObjectByName("floor");
  // if (!floor) return;
  // const coordsArr = getCoordsFromVectex(floor);
  // const originPoint = coordsArr[0];
  // if (!originPoint) return;
  // const parent = floor.parent;
  // if (!parent) return;
  // const points = [
  //   new THREE.Vector3(originPoint.x, floorY, originPoint.z),
  //   new THREE.Vector3(background.point.x, floorY, originPoint.z),
  //   new THREE.Vector3(background.point.x, floorY, background.point.z),
  //   new THREE.Vector3(originPoint.x, floorY, background.point.z),
  // ];
  // parent.userData.points = points;
  // parent.drawShadow({ points });
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
      case "circle":
        room = object.parent.parent;
        const cg = room.getObjectByName("circleGroup");
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
          // isDBClick: true,
          isDragging: true,
          circleIdx: clickedIdx,
          name,
        };
        return;

      case "floor":
        //
        room = object.parent;
        room.onClick();
        isChangingObject = {
          ...isChangingObject,
          changingObjectId: room.id,
          isDBClick: true,
          isDragging: false,
        };
        break;
    }
  } else if (isChangingObject.isDBClick && objectArr.length > 0) {
    const { object } = objectArr[0];
    const room = object.parent.parent;
    const rotationController = room.getObjectByName("rotationController");
    rotationController.onMouseDown();
    return;
  } else if (isCreating.isSelect) {
    //무엇가를 만들고 있는중
    const { target, isDragging } = isCreating;
    switch (target) {
      case "floor":
        if (isDragging) {
          // 만들기 종료
          const obj = scene.getObjectByName("shadow");

          // floor.material = material;
          obj.name = "room";
          obj.createRoom({ cameraZoom });
          // const zz = new D2Floor({ points: zzzzz });
          // scene.add(zz);
          isCreating = {
            isSelect: false,
            isDragging: false,
            target: null,
          };
        } else {
          // floor를 만드는 중
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
          const room = new D2Room({ points });

          // const room = create2DObject({
          //   points,
          //   name: "shadow",
          //   objectName: "room",
          // });
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
    return;
  }
  const raycaster = getIntersects(event);
  const background = raycaster.find((obj) => obj.object.name === "background");
  if (!background) return;
  const arr = getIntersectsArray(raycaster);

  if (arr.length > 0) {
    document.body.style.cursor = "pointer";
  } else if (arr.length === 0) {
    document.body.style.cursor = "default";
  }

  if (isCreating.isSelect && isCreating.isDragging) {
    // 바닥 그리기
    const obj = scene.getObjectByName("shadow");
    if (!obj) return;

    updateShadows({ object: obj, background });
    return;
  } else if (isCreating.target === "chair" || isCreating.target === "desk") {
    // 의자, 책상 등 오브젝트 만들기
    const floor = raycaster.find((obj) => obj.object.name === "floor");
    if (!floor) return;
    const point = new THREE.Vector3(floor.point.x, floorY, floor.point.z);
    const object = scene.getObjectByName("shadow");
    object.visible = true;
    object.position.set(point.x, floorY, point.z);
    return;
  } else if (isChangingObject.isDragging) {
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    const background = raycaster.find(
      (obj) => obj.object.name === "background"
    );
    if (!background) return;
    const points = new THREE.Vector3(
      background.point.x,
      floorY,
      background.point.z
    );
    room.updateLines({ points, circleIdx: isChangingObject.circleIdx });
  } else if (isChangingObject.isDBClick) {
    const room = scene.getObjectById(isChangingObject.changingObjectId);
    const rotationController = room.getObjectByName("rotationController");
    const points = new THREE.Vector3(
      background.point.x,
      floorY,
      background.point.z
    );
    // console.log(arr);
    if (
      !rotationController.isDragging &&
      arr.length > 0 &&
      arr[0].object.name.includes("controller")
    ) {
      rotationController.onMouseMove({ points });
    } else if (rotationController.isDragging) {
      rotationController.onMouseMove({ points });
    }
  }
  // if (isDragging) {
  //   // 화면 옮기기
  //   const deltaMove = {
  //     x: event.clientX - previousMousePosition.x,
  //     y: event.clientY - previousMousePosition.y,
  //   };

  //   // 카메라 위치 업데이트
  //   camera.position.x -= deltaMove.x / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
  //   camera.position.z -= deltaMove.y / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
  //   camera.lookAt(camera.position.x, 0, camera.position.z);

  //   previousMousePosition = { x: event.clientX, y: event.clientY };
  //   return;
  // }

  // if (isChangingObject.isDBClick) {
  //   console.log("zzzzz");
  //   if (xx) {
  //     return;
  //   }
  //   let controller = raycaster.find((obj) =>
  //     obj.object.name.includes("controller")
  //   );

  //   if (!controller) return;
  //   controller = controller.object;
  //   if (controller.parent) {
  //     controller = controller.parent;
  //   }
  //   let changingObject;
  //   if (controller.isDragging) {
  //     changingObject = scene.getObjectById(isChangingObject.changingObjectId);
  //   }
  //   controller.onMouseMove({
  //     object: changingObject,
  //     points: {
  //       x: background.point.x,
  //       y: RotationControllerY,
  //       z: background.point.z,
  //     },
  //   });
  //   controller.getObjectByName("controllerRing").visible = true;
  // }
};

const onDoubleClick = (event) => {};

const onMouseUp = () => {
  if (controls.enabled) return;
  if (isChangingObject.isDragging) {
    console.log("up!!");

    const room = scene.getObjectById(isChangingObject.changingObjectId);
    room.onMouseUp({ cameraZoom });
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
  // const c = new RotationController({ cameraZoom });
  // c.position.set(0, 2, 0);
  // c.rotation.x = -Math.PI / 2;
  // scene.add(c);
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
      // create3DScene();
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

  //   // w.visible =
  //   //   currentPosition.copy(w.position).sub(camera.position).lengthSq() >
  //   //   camera.position.lengthSq();
  // });

  renderer.render(scene, camera);
};

animate();

export const RotationControllerY = 3;

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
