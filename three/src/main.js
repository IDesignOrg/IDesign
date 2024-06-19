import * as THREE from "https://cdn.jsdelivr.net/npm/three@0.117.1/build/three.module.js";
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(
  75,
  window.innerWidth / window.innerHeight,
  0.1,
  1000
);

camera.position.z = 5;
const renderer = new THREE.WebGLRenderer();

const geometry = new THREE.BoxGeometry(1, 1, 1);
const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
const cube = new THREE.Mesh(geometry, material);
scene.add(cube);

camera.position.z = 5;
function animate() {
  renderer.render(scene, camera);
}
renderer.setAnimationLoop(animate);
const changeRenderer = () => {
  renderer.setSize(window.innerWidth, window.innerHeight);
};

window.addEventListener("resize", changeRenderer);
changeRenderer();
document.body.appendChild(renderer.domElement);
