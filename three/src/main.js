import * as THREE from "./lib/three.module.js";
import { OrbitControls } from "./lib/OrbitControls.js";
import { WEBGL } from "./lib/webgl.js";
import { Desk } from "./lib/objects/desk.js";
import { Floor } from "./lib/objects/floor.js";
import {
  deskName,
  floorName,
  shadowName,
  groundName,
} from "./lib/objects/objectNames.js";
import { deskColor, floorColors, shadowColor } from "./lib/objects/colors.js";
import { InitObjects } from "./lib/objects/sceneObjects.js";

const dx = [0, 0, -1, 1];
const dy = [-1, 1, 0, 0];

if (WEBGL.isWebGLAvailable()) {
  const btn = document.getElementById("hud-icon");
  let selectedObject = null;

  const objects = {};

  const scene = new THREE.Scene();
  const mouse = new THREE.Vector2();
  const raycaster = new THREE.Raycaster();
  scene.background = new THREE.Color("white");
  const camera = new THREE.PerspectiveCamera(
    75,
    window.innerWidth / window.innerHeight,
    0.01,
    1000
  );
  camera.position.z = 10;
  camera.position.y = 10;
  camera.position.x = 10;
  camera.lookAt(0, 0, 0);
  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);
  const controls = new OrbitControls(camera, renderer.domElement);
  controls.update();

  const { initGround, initFloor, axeshelper } = new InitObjects();
  scene.add(initGround);
  scene.add(initFloor);
  scene.add(axeshelper);

  const wallsAndCeiling = [];
  // initRoom.children.forEach((child) => {
  //   if (child.material && child.material instanceof THREE.MeshBasicMaterial) {
  //     if (child.rotation.x !== -Math.PI / 2) {
  //       // 바닥을 제외
  //       child.material.transparent = true;
  //       wallsAndCeiling.push(child);
  //     }
  //   }
  // });

  function updateOpacity() {
    const cameraDirection = new THREE.Vector3();
    camera.getWorldDirection(cameraDirection);

    wallsAndCeiling.forEach((wall) => {
      const wallNormal = new THREE.Vector3();
      wall.getWorldDirection(wallNormal);

      const angle = wallNormal.angleTo(cameraDirection);
      const maxAngle = Math.PI / 2; // 90 degrees

      if (angle > maxAngle) {
        wall.material.opacity = 0;
      } else {
        wall.material.opacity = 1 - angle / maxAngle;
      }
    });
  }

  const animate = () => {
    requestAnimationFrame(animate);

    controls.update();
    updateOpacity();
    renderer.render(scene, camera);
  };

  const createDesk = (x, z, object, color, condition) => {
    const desk = new Desk({
      x,
      z,
      object,
      width: 4,
      height: 1.5,
      depth: 2,
      color,
    });
    desk.children.forEach((child) => {
      if (child.material && child.material instanceof THREE.MeshBasicMaterial) {
        child.material.transparent = false;
        child.material.opacity = 1;
      }
    });
    if (condition) {
      objects[shadowName] = desk;
    } else {
      objects[desk.uuid] = desk;
    }
    scene.add(desk);
  };

  // 새로운 방 생성 함수
  const createRoom = (intersects, color, isShadow) => {
    return;
    let nearestFloor;
    let position;

    if (intersects.length === 0) {
      // intersects의 길이가 0일 때는 화면 중심으로 새로운 방 생성
      nearestFloor = initRoom.children.find(
        (child) => child.name === floorName
      );
      position = new THREE.Vector3(0, 0, 0); // 화면 중심
    } else {
      nearestFloor = intersects.find(
        (intersect) => intersect.object.name === floorName
      );
      if (nearestFloor) {
        position = nearestFloor.point;
      } else {
        // intersections 배열에 floor 객체가 없는 경우, 첫 번째 교차점 사용
        nearestFloor = intersects[0];
        position = nearestFloor.point;
      }
    }

    if (nearestFloor) {
      const floor = nearestFloor;
      if (!floor.position) return;

      // 동서남북 방향으로 방 생성
      const directions = [
        { x: 1, z: 0 },
        { x: -1, z: 0 },
        { x: 0, z: 1 },
        { x: 0, z: -1 },
      ];

      let closestDistance = Infinity;
      let closestDirection = null;
      let closestWall = null;

      // 마우스와 가장 가까운 방향 찾기
      directions.forEach((direction) => {
        const newX =
          floor.position.x +
          (direction.x * floor.geometry.parameters.width) / 2;
        const newZ =
          floor.position.z +
          (direction.z * floor.geometry.parameters.height) / 2;
        const distance = Math.sqrt(
          Math.pow(newX - mouse.x, 2) + Math.pow(newZ - mouse.y, 2)
        );

        if (distance < closestDistance) {
          closestDistance = distance;
          closestDirection = direction;
          closestWall = new THREE.Vector3(newX, 0, newZ);
        }
      });

      const newX =
        floor.position.x + closestDirection.x * floor.geometry.parameters.width;
      const newZ =
        floor.position.z +
        closestDirection.z * floor.geometry.parameters.height;
      const newPosition = new THREE.Vector3(newX, 0, newZ);

      // 새로운 위치에 바닥이 없는 경우에만 방 생성
      const newIntersects = raycaster.intersectObjects(scene.children, true);
      const existingFloor = newIntersects.find(
        (intersect) =>
          intersect.object.name === floorName &&
          intersect.point.distanceTo(newPosition) < 1
      );

      if (!existingFloor) {
        const newRoom = new Room({
          floorColor: color,
          wallColor: color,
          ceilingColor: color,
        });
        newRoom.position.copy(closestWall);
        newRoom.name = floorName;
        if (isShadow) {
          if (objects.hasOwnProperty("shadow")) {
            scene.remove(objects["shadow"]);
            delete objects["shadow"];
          }
          objects["shadow"] = newRoom;
        } else {
          objects[newRoom.uuid] = newRoom;
        }
        scene.add(newRoom);
      }
    }
  };

  const getIntersects = (event) => {
    event.preventDefault();

    // 마우스 포인트 계산
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
    // Raycaster 설정
    raycaster.setFromCamera(mouse, camera);

    // 평면과의 교차점 계산

    return raycaster.intersectObjects(scene.children, true);
  };

  const getSize = (object) => {
    // return { x: 20, y: 1, z: 20 };
    const boundingBox = new THREE.Box3().setFromObject(object);

    // 크기 계산
    const size = new THREE.Vector3();
    boundingBox.getSize(size);
    return size;
  };

  const createFloor = (intersects, floorColor, isShadow, event) => {
    let selectedObject = intersects.find(
      (obj) => obj.object.name === floorName
    );
    if (selectedObject) {
      const size = getSize(selectedObject.object);
      let x = selectedObject.point.x > 0 ? 1 : -1;
      let z = selectedObject.point.z > 0 ? 1 : -1;
      const y = isShadow ? 0.1 : 0;

      const cx = x;
      const cy = z;

      x = (selectedObject.object.position.x + size.x / 2) * x;
      z = (selectedObject.object.position.z + size.z / 2) * z;

      // console.log(
      //   `cx+cz = ${cx + cy} px = ${selectedObject.point.x} pz = ${
      //     selectedObject.point.z
      //   } cx = ${cx} cz = ${cy}`
      // );
      console.log(size);
      const floor = new Floor({ x, y, z, floorColor });
      objects[shadowName] = floor;
      scene.add(floor);
    } else {
      selectedObject = intersects.find((obj) => obj.object.name === groundName);
    }

    if (isShadow) {
    }
    //  { x, z } = selectedObject.point;

    // console.log(scene.child.forEach())

    // scene.add(floor);
    // objects[shadowName] = floor;
    // return;
    // const { x, y, z } = camera.position;
    // const cameraDistance = new THREE.Vector3();
    // const target = new THREE.Vector3(x, y, z);
    // const a = getRaycaster(event);
    // // cameraDistance.subVectors(camera.position, target);
    // if (intersects.length > 0) {
    // } else {
    // }
  };

  const creator = (event, shadowName, color) => {
    if (objects.hasOwnProperty(shadowName)) {
      scene.remove(objects[shadowName]);
      delete objects[shadowName];
    }
    const intersects = getIntersects(event);

    if (selectedObject === floorName) {
      const floorColor = shadowName ? shadowColor : color;
      // const isShadow = event.type === "mousemove";
      createFloor(intersects, floorColor, shadowName, event);
      return;
    }
    if (intersects.length < 1) {
      return;
    }
    const conditionName = selectedObject === deskName ? floorName : "null";
    const clickedObject = intersects.find(
      (intersect) => intersect.object.name === conditionName
    );
    if (!clickedObject) return;
    const intersect = clickedObject.object;
    createDesk(
      clickedObject.point.x,
      clickedObject.point.z,
      intersect,
      color,
      shadowName
    );
  };

  const onDocumentMouseDown = (event) => {
    if (controls.enabled) return;
    if (!selectedObject) return;
    let color = null;
    switch (selectedObject) {
      case deskName:
        color = deskColor;
        break;
      case floorName:
        color = floorColors;
        break;
    }
    creator(event, false, color);
  };

  const onDocumentMouseMove = (event) => {
    if (controls.enabled) return;
    if (!selectedObject) return;
    switch (selectedObject) {
      case deskName:
        break;
      case floorName:
        break;
    }
    creator(event, shadowName, shadowColor);
  };

  const onKeyDown = (event) => {
    const { keyCode } = event;
    switch (keyCode) {
      case 27:
        controls.enabled = !controls.enabled;
        if (objects.hasOwnProperty(shadowName)) {
          scene.remove(objects[shadowName]);
          delete objects[shadowName];
        }
        break;
      default:
        break;
    }
  };

  const onClickIcon = (event) => {
    event.stopPropagation();
    const btn = event.target.closest("button");

    if (!btn) return;
    controls.enabled = false;
    switch (btn.innerText) {
      case "책상":
        selectedObject = deskName;
        break;
      case "바닥":
        selectedObject = floorName;
        break;
      default:
        break;
    }
  };

  btn.addEventListener("mousedown", onClickIcon);
  document.addEventListener("mousemove", onDocumentMouseMove, false);
  document.addEventListener("mousedown", onDocumentMouseDown, false);
  document.addEventListener("keydown", onKeyDown, false);

  const onWindowResize = () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
  };

  window.addEventListener("resize", onWindowResize, false);
  animate();
} else {
  const warning = WEBGL.getWebGLErrorMessage();
  document.body.appendChild(warning);
}
