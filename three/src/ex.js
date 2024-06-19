import * as THREE from "./three.module.js";
// import { OrbitControls } from "./three-orbitcontrols/OrbitControls.js";
import { OrbitControls } from "https://threejsfundamentals.org/threejs/resources/threejs/r122/examples/jsm/controls/OrbitControls.js";
import { GLTFLoader } from "https://threejsfundamentals.org/threejs/resources/threejs/r122/examples/jsm/loaders/GLTFLoader.js";
// import { OrbitControls } from "OrbitControls.js";
// import { OrbitControls} from '.'
import { WEBGL } from "./webgl.js";
import { Desk } from "./lib/objects/desk.js";

if (WEBGL.isWebGLAvailable()) {
  const btn = document.getElementById("hud-icon");
  // let isIconClick = false;

  const objects = {};

  const CNT = 20;
  const SMALL_CNT = 8;
  const DESK_WIDTH = 4;
  const DESK_DEPTH = 2;

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

  const planeGeometry = new THREE.PlaneGeometry(CNT, CNT);
  const planeMaterial = new THREE.MeshBasicMaterial({
    color: 0xcccccc,
    side: THREE.DoubleSide,
  });
  const plane = new THREE.Mesh(planeGeometry, planeMaterial);
  plane.name = "plane";

  objects[plane.uuid] = plane;

  plane.rotation.x = Math.PI / 2; // 평면을 수직으로 회전
  scene.add(plane);

  // 10x10 평면 생성 (30x30 평면도 왼쪽에 위치)
  const smallPlaneGeometry = new THREE.PlaneGeometry(SMALL_CNT, SMALL_CNT);
  const smallPlaneMaterial = new THREE.MeshBasicMaterial({
    side: THREE.DoubleSide,
    color: "rgb(240,238,232)",
  });
  const smallPlane = new THREE.Mesh(smallPlaneGeometry, smallPlaneMaterial);
  smallPlane.rotation.y = Math.PI / 2;
  smallPlane.position.set(-CNT / 2, SMALL_CNT / 2, 0);
  scene.add(smallPlane);

  // 그리드 헬퍼 추가
  // const gridHelper = new THREE.GridHelper(CNT);
  // scene.add(gridHelper);

  const de = new Desk({
    x: 0,
    z: 0,
    object: plane,
    width: 4,
    height: 1.5,
    depth: 2,
  });
  objects[de.uuid] = de;
  scene.add(de);
  console.log(objects);
  const animate = () => {
    requestAnimationFrame(animate);

    controls.update();
    renderer.render(scene, camera);
  };

  const createDesk = (x, z, object) => {
    // desk 생성
    const desk = new Desk({ x, z, object, width: 4, height: 1.5, depth: 2 });
    // console.log(d);
    objects[desk.uuid] = desk;

    scene.add(desk);
    console.log(objects);
  };

  const getMousePoint = (event) => {
    event.preventDefault();

    // 마우스 포인트 계산
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

    // Raycaster 설정
    raycaster.setFromCamera(mouse, camera);

    // 평면과의 교차점 계산
    // const intersects = raycaster.intersectObject(plane);
    //  or intersectObject(plane),true => 한 오브젝트에 대해
    return raycaster.intersectObjects(scene.children, true); // 씬에 들어있는 각각의 오브젝트에 대해
  };

  const onDocumentMouseDown = (event) => {
    const intersects = getMousePoint(event);
    if (intersects.length > 0) {
      const clickedObject = intersects[0].object;
      if (clickedObject.name === "plane") {
      } else {
      }
      const intersect = intersects[0];
      createDesk(intersect.point.x, intersect.point.z, clickedObject);
    } else {
      console.log("마우스 아웃");
    }
  };
  const onDocumentMouseMove = (e) => {
    // console.log("move!!");
    if (controls.enabled) return;
  };
  const onDocumentKeyDown = () => {};
  const onDocumentKeyUp = () => {};

  const onClickIcon = () => {
    // isIconClick = !isIconClick;
    controls.enabled = !controls.enabled;
    console.log("icon click", controls.enabled);
  };

  const onDoubleClick = (e) => {
    const intersects = getMousePoint(e);
    console.log(intersects);
    if (intersects.length > 0) {
      const clickedObject = intersects[0].object;
      console.log(clickedObject);
    } else {
      console.log("마우스 아웃");
    }
  };

  document.addEventListener("mousemove", onDocumentMouseMove, false);
  document.addEventListener("mousedown", onDocumentMouseDown, false);
  // controls.addEventListener("mousedown", myOnMouseDownFunction, false);
  document.addEventListener("keydown", onDocumentKeyDown, false);
  document.addEventListener("keyup", onDocumentKeyUp, false);
  document.addEventListener("dblclick", onDoubleClick, false);
  btn.addEventListener("click", onClickIcon);
  const onWindowResize = () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);
  };
  window.addEventListener("resize", onWindowResize, false);
  animate();
} else {
  var warning = WEBGL.getWebGLErrorMessage();
  document.body.appendChild(warning);
}
