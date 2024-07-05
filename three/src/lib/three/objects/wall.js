import { THREE } from "../../../three.js";

const wallHeight = 50;
const wallDepth = 5;
const colors = ["red", "blue", "black", "green"];

export class D3Wall {
  constructor({ points }) {
    const walls = new THREE.Group();
    walls.name = "walls";
    // wall.add(points.)
    return D3WallCreator(points);
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
  const dx = p2.x - p1.x;
  const dz = p2.z - p1.z;
  return Math.atan2(dz, dx);
};

const calculateDistance = (p1, p2) => {
  const dx = p2.x - p1.x;

  const dz = p2.z - p1.z;
  return Math.sqrt(dx * dx + dz * dz);
};

const D3WallCreator = (points) => {
  const walls = new THREE.Group();
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
    cube.rotation.y = angle;
    walls.add(cube);
  }
  return walls;
};

// import { wallColor } from "../objectConf/colors.js";
// import { ceilingHeight } from "../objectConf/length.js";
// import { wallName } from "../objectConf/objectNames.js";
// import * as THREE from "../three.module.js";

// class Wall {
//   constructor({
//     x,
//     y = 0,
//     z,
//     color = wallColor,
//     width,
//     length = ceilingHeight,
//     rotationY = 0,
//   }) {
//     const wallGeometry = new THREE.PlaneGeometry(width, length);
//     const wallMaterial = new THREE.MeshBasicMaterial({
//       color: color,
//       side: THREE.BackSide,
//     });
//     const wall = new THREE.Mesh(wallGeometry, wallMaterial);
//     wall.name = wallName;
//     wall.position.set(x, y, z);
//     wall.rotation.y = rotationY;

//     return wall;
//   }
// }

// export { Wall };
