import { THREE } from "../../../three.js";

const wallHeight = 50;
const wallDepth = 5;
const colors = ["red", "blue", "black", "green"];

export class D3Wall extends THREE.Group {
  constructor({ points }) {
    const walls = super();
    walls.name = "walls";
    for (let i = 0; i < points.length; i++) {
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint) + wallDepth;
      const wall = new THREE.BoxGeometry(width, wallHeight, wallDepth);
      const material = new THREE.MeshBasicMaterial({
        color: colors[i % points.length],
        side: THREE.BackSide,
      });
      const cube = new THREE.Mesh(wall, material);
      cube.position.set(
        (currentPoint.x + nextPoint.x) / 2,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2
      );
      cube.rotation.y = -angle;
      walls.add(cube);
    }
    return walls;
  }
}

export class D2Wall extends THREE.Group {
  constructor({ points }) {
    const walls = super();
    walls.name = "walls";
    const material = new THREE.LineBasicMaterial({
      color: 0x0000ff,
      linewidth: 5,
    });
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    const line = new THREE.Line(geometry, material);
    line.name = "walls";
    return line;
  }
}

const calculateAngle = (p1, p2) => {
  return Math.atan2(p2.z - p1.z, p2.x - p1.x);
};

const calculateDistance = (p1, p2) => {
  const dx = p2.x - p1.x;

  const dz = p2.z - p1.z;
  return Math.sqrt(dx * dx + dz * dz);
};
