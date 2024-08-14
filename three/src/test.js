import * as THREE from "three";

// Helper function to calculate the midpoint of a line segment
function getMidpoint(p1, p2) {
  return new THREE.Vector3(
    (p1.x + p2.x) / 2,
    (p1.y + p2.y) / 2,
    (p1.z + p2.z) / 2
  );
}

// Helper function to calculate direction vector
function getDirection(p1, p2) {
  return new THREE.Vector3().subVectors(p2, p1).normalize();
}

// Calculate the position where the plane should be placed
function getPlanePositionOnEdge(p1, p2, planeWidth, planeHeight) {
  const midpoint = getMidpoint(p1, p2);
  const direction = getDirection(p1, p2);

  // Calculate offset based on plane width
  const offset = new THREE.Vector3(-direction.z, 0, direction.x).multiplyScalar(
    planeWidth / 2
  );

  return midpoint.add(offset);
}

// Create plane mesh
function createPlane(width, height) {
  const geometry = new THREE.PlaneGeometry(width, height);
  const material = new THREE.MeshBasicMaterial({
    color: 0xff0000,
    side: THREE.DoubleSide,
  });
  return new THREE.Mesh(geometry, material);
}

// Define vertices of a polygon
const vertices = [
  new THREE.Vector3(-201.409, 1, -211.832),
  new THREE.Vector3(-53.328, 1, -211.832),
  new THREE.Vector3(-53.328, 1, -83.425),
  new THREE.Vector3(-201.409, 1, -83.425),
];

// Plane dimensions
const planeWidth = 10;
const planeHeight = 10;

// Scene setup
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

// Create planes on each edge of the polygon
for (let i = 0; i < vertices.length; i++) {
  const startVertex = vertices[i];
  const endVertex = vertices[(i + 1) % vertices.length];

  const planePosition = getPlanePositionOnEdge(
    startVertex,
    endVertex,
    planeWidth,
    planeHeight
  );
  const plane = createPlane(planeWidth, planeHeight);
  plane.position.copy(planePosition);

  // Align the plane to face outward from the edge
  const direction = getDirection(startVertex, endVertex);
  const rotation = new THREE.Quaternion().setFromUnitVectors(
    new THREE.Vector3(0, 0, 1),
    direction
  );
  plane.setRotationFromQuaternion(rotation);

  scene.add(plane);
}

// Camera setup
camera.position.z = 500;

// Animation loop
// function animate() {
//   requestAnimationFrame(animate);
//   renderer.render(scene, camera);
// }
// animate();
