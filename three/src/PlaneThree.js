import { floorColors } from "./lib/three/objectConf/colors";
import { floorName } from "./lib/three/objectConf/objectNames";
import * as THREE from "./lib/three/three.module.js";
// import { OrbitControls } from "./lib/three/OrbitControls.js";
export const TwoDigitStart = () => {
  const btns = document.getElementById("hud-icon");

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

  const objects = {};

  const mouse = new THREE.Vector2();
  const raycaster = new THREE.Raycaster();
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
  //   const controls = new OrbitControls(camera, renderer.domElement);
  //   controls.update();
  // 카메라 설정
  camera.position.set(0, cameraZoom, 0);
  camera.lookAt(0, 0, 0);

  // 기본적인 그리드 헬퍼 추가
  scene.add(new THREE.AxesHelper(1000));
  const gridHelper = new THREE.GridHelper(2000, 100);
  scene.add(gridHelper);

  // 큐브 추가 (예시)
  const geometry = new THREE.BoxGeometry(10, 10);
  const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
  const cube = new THREE.Mesh(geometry, material);
  //   cube.rotation.x = Math.PI / 2;
  //   cube.rotation.y = -Math.PI / 2;
  cube.rotation.x = Math.PI / 2;
  cube.position.set(10, 1, 10);
  scene.add(cube);

  const planeGeometry = new THREE.PlaneGeometry(2000, 2000);
  const planeMaterial = new THREE.MeshBasicMaterial({
    color: 0xffff00,
    side: THREE.DoubleSide,
  });
  const plane = new THREE.Mesh(planeGeometry, planeMaterial);
  plane.position.set(0, -1, 0);
  plane.rotation.x = -Math.PI / 2; // x축을 기준으로 90도 회전
  plane.name = "background";
  objects["background"] = plane;
  scene.add(plane);

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

  // 포인트로 도형 생성
  const createPlaneFromPoints = (points) => {
    const shape = new THREE.Shape();
    // z축의 기준으로 반대방향
    // 왜인지는 모름...
    shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
    for (let i = 1; i < points.length; i++) {
      shape.lineTo(points[i].x, -points[i].z);
    }
    shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

    const geometry = new THREE.ShapeGeometry(shape);
    const material = new THREE.MeshBasicMaterial({
      color: "green",
      side: THREE.DoubleSide,
    });
    const mesh = new THREE.Mesh(geometry, material);
    mesh.rotation.x = -Math.PI / 2; // 평면을 바닥과 수평하게 회전

    return mesh;
  };

  const onMouseDown = (event) => {
    if (isCreating.isSelect) {
      if (isCreating.isDragging) {
        isCreating = {
          isSelect: false,
          isDragging: false,
        };
        return;
      }
      const raycaster = getIntersects(event);
      const background = raycaster.find(
        (obj) => obj.object.name === "background"
      );
      if (!background) return;
      const points = [
        new THREE.Vector3(background.point.x, 0, background.point.z),
        new THREE.Vector3(background.point.x + 20, 0, background.point.z),
        new THREE.Vector3(background.point.x + 20, 0, background.point.z + 20),
        new THREE.Vector3(background.point.x, 0, background.point.z + 20),
      ];

      const obj = createPlaneFromPoints(points);
      obj.name = "shadow";

      objects["shadow"] = {
        coords: points,
        obj,
        name: "shadow",
      };

      scene.add(obj);
      isCreating = {
        ...isCreating,
        isDragging: true,
      };

      return;
    }
    isDragging = true;
    previousMousePosition = { x: event.clientX, y: event.clientY };
  };

  const onMouseMove = (event) => {
    if (isCreating.isSelect && isCreating.isDragging) {
      const raycaster = getIntersects(event);
      const background = raycaster.find(
        (obj) => obj.object.name === "background"
      );
      if (!background) return;

      //   const originPoint = background.point;
      const originPoint = { ...objects["shadow"].coords[0] };

      const points = [
        new THREE.Vector3(originPoint.x, 0, originPoint.z),
        new THREE.Vector3(background.point.x, 0, originPoint.z),
        new THREE.Vector3(background.point.x, 0, background.point.z),
        new THREE.Vector3(originPoint.x, 0, background.point.z),
      ];

      scene.remove(objects["shadow"].obj);
      const obj = createPlaneFromPoints(points);
      obj.name = "shadow";
      objects["shadow"] = {
        coords: points,
        obj,
        name: "shadow",
      };
      scene.add(obj);

      return;
    }
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

  const onCreate = (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    isCreating = {
      ...isCreating,
      isSelect: true,
    };
  };

  // 이벤트 리스너 추가
  window.addEventListener("mousedown", onMouseDown);
  window.addEventListener("mousemove", onMouseMove);
  window.addEventListener("mouseup", onMouseUp);
  window.addEventListener("wheel", onWheel);
  btns.addEventListener("click", onCreate);

  // 애니메이션 루프
  const animate = () => {
    // controls.update();
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
  };

  animate();
};
