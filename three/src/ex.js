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

  // for helper
  const axesHelper = new THREE.AxesHelper(100, 100, 100);
  scene.add(axesHelper);

  const planeGeometry = new THREE.PlaneGeometry(CNT, CNT);
  const planeMaterial = new THREE.MeshBasicMaterial({
    color: 0xcccccc,
    side: THREE.DoubleSide,
  });
  const plane = new THREE.Mesh(planeGeometry, planeMaterial);
  plane.name = "plane";

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
  smallPlane.name = "smallPlane";
  smallPlane.rotation.y = Math.PI / 2;
  smallPlane.position.set(-CNT / 2, SMALL_CNT / 2, 0);
  scene.add(smallPlane);

  // 그리드 헬퍼 추가
  // const gridHelper = new THREE.GridHelper(CNT);
  // scene.add(gridHelper);

  // const de = new Desk({
  //   x: 0,
  //   z: 0,
  //   object: plane,
  //   width: 4,
  //   height: 1.5,
  //   depth: 2,
  // });
  // objects[de.uuid] = de;
  // scene.add(de);
  const animate = () => {
    requestAnimationFrame(animate);

    controls.update();
    renderer.render(scene, camera);
  };

  const createDesk = (x, z, object, color, condition) => {
    if (objects.hasOwnProperty("shadow")) {
      scene.remove(objects["shadow"]);
      delete objects["shadow"];
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
    if (condition) {
      objects["shadow"] = desk;
      // desk.name = "shadow";
    } else {
      // desk.name = "desk";
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
    //  or intersectObject(plane),true => 한 오브젝트에 대해 const intersects = raycaster.intersectObject(plane);
    return raycaster.intersectObjects(scene.children, true); // 씬에 들어있는 각각의 오브젝트에 대해
  };

  const onDocumentMouseDown = (event) => {
    const intersects = getMousePoint(event);
    if (intersects.length > 0) {
      const clickedObject = intersects.find(
        (intersect) => intersect.object.name === "plane"
      );
      if (!clickedObject) return;
      const intersect = clickedObject.object;
      createDesk(clickedObject.point.x, clickedObject.point.z, intersect);
    } else {
    }
  };
  const onDocumentMouseMove = (e) => {
    if (controls.enabled) return;
    const intersects = getMousePoint(e);
    if (intersects.length > 0) {
      const clickedObject = intersects.find(
        (intersect) => intersect.object.name === "plane"
      );
      if (!clickedObject) return;
      createDesk(
        clickedObject.point.x,
        clickedObject.point.z,
        clickedObject.object,
        "rgb(215, 63, 63)",
        "shadow"
      );
    } else {
    }
  };
  const onDocumentKeyDown = () => {};
  const onDocumentKeyUp = () => {};

  const onClickIcon = (e) => {
    e.stopPropagation();
    controls.enabled = !controls.enabled;
  };

  const onDoubleClick = (e) => {
    const intersects = getMousePoint(e);
    if (intersects.length > 0) {
      const clickedObject = intersects[0].object;
      // console.log(intersects);
      const obj = clickedObject.parent;
      obj.rotation.y = THREE.MathUtils.degToRad(90);
    } else {
      console.log("마우스 아웃");
    }
  };

  const onKeyDown = (e) => {
    console.log(e.keyCode);

    const { keyCode } = e;
    switch (keyCode) {
      case 27:
        controls.enabled = !controls.enabled;
        if (objects.hasOwnProperty("shadow")) {
          scene.remove(objects["shadow"]);
          delete objects["shadow"];
        }
        return;

      default:
        return;
    }
  };

  btn.addEventListener("mousedown", onClickIcon);

  document.addEventListener("mousemove", onDocumentMouseMove, false);
  document.addEventListener("mousedown", onDocumentMouseDown, false);
  document.addEventListener("keydown", onDocumentKeyDown, false);
  document.addEventListener("keyup", onDocumentKeyUp, false);
  document.addEventListener("dblclick", onDoubleClick, false);
  document.addEventListener("keydown", onKeyDown);
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
