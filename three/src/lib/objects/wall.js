import { THREE } from "../loader/three";
import { calculateAngle, calculateDistance } from "../calculater";

export const wallHeight = 50;
const wallDepth = 5;
const colors = ["red", "blue", "black", "green"];

const wallpaper = "../../../../public/img/wallpaper2.jpg";
const texture = new THREE.TextureLoader().load(wallpaper);

export class D3Wall extends THREE.Group {
  constructor({ points, center }) {
    const walls = super();
    walls.name = "walls";
    for (let i = 0; i < points.length; i++) {
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint);
      const wallGeo = new THREE.PlaneGeometry(width, wallHeight);
      const material = new THREE.MeshBasicMaterial({
        map: texture,
        side: THREE.FrontSide,
      });

      const wall = new THREE.Mesh(wallGeo, material);
      wall.position.set(
        (currentPoint.x + nextPoint.x) / 2 - center.x,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2 - center.z
      );
      wall.rotation.y = -angle;
      walls.add(wall);
    }

    walls.position.y = 2;
    return walls;
  }
}

function getNewCoordinates(currentPoint, angle, distance) {
  const angleOffset = Math.PI / 4; // 45도 각도
  const newX = currentPoint.x + Math.cos(angle + angleOffset) * distance;
  const newZ = currentPoint.z + Math.sin(angle + angleOffset) * distance;
  return { x: newX, z: newZ };
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
