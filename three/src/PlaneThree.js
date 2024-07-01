import { floorColors } from "./lib/three/objectConf/colors";
import { floorName } from "./lib/three/objectConf/objectNames";
import * as THREE from "./lib/three/three.module.js";

export const TwoDigitStart = () => {
  let isDragging = false;
  let previousMousePosition = { x: 0, y: 0 };
  const maxZoom = 500;
  const minZoom = 50;
  let cameraZoom = 250;
  const zoomSensitivity = 0.005;

  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(
    75,
    window.innerWidth / window.innerHeight,
    0.1,
    1000
  );
  const renderer = new THREE.WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);

  // 카메라 설정
  camera.position.set(0, cameraZoom, 0);
  camera.lookAt(0, 0, 0);

  // 기본적인 그리드 헬퍼 추가
  const gridHelper = new THREE.GridHelper(2000, 100);
  scene.add(gridHelper);

  // 큐브 추가 (예시)
  const geometry = new THREE.BoxGeometry(10, 1, 10);
  const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
  const cube = new THREE.Mesh(geometry, material);
  scene.add(cube);

  // 마우스 이벤트 핸들러
  const onMouseDown = (event) => {
    isDragging = true;
    previousMousePosition = { x: event.clientX, y: event.clientY };
  };

  const onMouseMove = (event) => {
    if (isDragging) {
      const deltaMove = {
        x: event.clientX - previousMousePosition.x,
        y: event.clientY - previousMousePosition.y,
      };

      // 카메라 위치 업데이트
      camera.position.x -= deltaMove.x / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
      camera.position.z -= deltaMove.y / (maxZoom / cameraZoom); // 마우스 이동 속도와 동일하게 업데이트
      camera.lookAt(camera.position.x, 0, camera.position.z);

      previousMousePosition = { x: event.clientX, y: event.clientY };
    }
  };

  const onMouseUp = () => {
    isDragging = false;
  };

  const onWheel = (event) => {
    const delta = event.deltaY;
    const newZoom = Math.min(maxZoom, Math.max(minZoom, cameraZoom - delta));
    cameraZoom = newZoom;
    camera.position.y = newZoom;
    camera.lookAt(camera.position.x, 0, camera.position.z);
  };

  // 이벤트 리스너 추가
  window.addEventListener("mousedown", onMouseDown);
  window.addEventListener("mousemove", onMouseMove);
  window.addEventListener("mouseup", onMouseUp);
  window.addEventListener("wheel", onWheel);

  // 애니메이션 루프
  const animate = () => {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
  };

  animate();
};
