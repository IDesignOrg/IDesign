import * as THREE from "./lib/three.module.js";
import { OrbitControls } from "https://threejsfundamentals.org/threejs/resources/threejs/r122/examples/jsm/controls/OrbitControls.js";
import { WEBGL } from "./lib/webgl.js";
import { Desk } from "./lib/objects/desk.js";
import { Plane } from "./lib/objects/room.js";
import { deskName, floorName, shadowName } from "./lib/objects/objectNames.js";
import { deskColor, shadowColor } from "./lib/objects/colors.js";

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

  // for helper
  const axesHelper = new THREE.AxesHelper(100);
  scene.add(axesHelper);

  const initPlane = new Plane({});

  objects[initPlane.uuid] = initPlane;
  scene.add(initPlane);

  // 각 벽과 천장을 검색하여 불투명도를 설정할 수 있도록 함
  const wallsAndCeiling = [];
  initPlane.children.forEach((child) => {
    if (child.material && child.material instanceof THREE.MeshBasicMaterial) {
      if (child.rotation.x !== -Math.PI / 2) {
        // 바닥을 제외
        child.material.transparent = true;
        wallsAndCeiling.push(child);
      }
    }
  });

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
    if (objects.hasOwnProperty(shadowName)) {
      scene.remove(objects[shadowName]);
      delete objects[shadowName];
    }
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

  const getMousePoint = (event) => {
    event.preventDefault();

    // 마우스 포인트 계산
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

    // Raycaster 설정
    raycaster.setFromCamera(mouse, camera);

    // 평면과의 교차점 계산
    return raycaster.intersectObjects(scene.children, true);
  };

  const creator = (event, shadowName, color) => {
    const intersects = getMousePoint(event);
    if (intersects.length > 0) {
      const clickedObject = intersects.find(
        (intersect) => intersect.object.name === floorName
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
    }
  };

  const onDocumentMouseDown = (event) => {
    creator(event, false, deskColor);
  };

  const onDocumentMouseMove = (event) => {
    if (controls.enabled) return;
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
    switch (btn.innerText) {
      case "책상":
        selectedObject = "desk";
        break;
      case "바닥":
        selectedObject = "plane";
        break;
      default:
        break;
    }
    controls.enabled = !controls.enabled;
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
