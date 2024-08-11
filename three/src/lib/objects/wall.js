import { THREE } from "../loader/three.js";
import { calculateAngle, calculateDistance } from "../calculater.js";
import { floorMaterial, wallMaterial } from "../../main";
import { wallName, wallsName } from "../objectConf/objectNames";
import { roomY } from "../objectConf/renderOrders.js";
import { Shape } from "./floor.js";

export const wallHeight = 50;

export class D3Wall extends THREE.Group {
  constructor({ points, center }) {
    super();
    this.name = "walls";
    for (let i = 0; i < points.length; i++) {
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint);
      const wallGeo = new THREE.PlaneGeometry(width, wallHeight);
      // const material = new THREE.MeshBasicMaterial({
      //   map: texture,
      //   side: THREE.FrontSide,
      // });
      const material = wallMaterial;
      const wall = new THREE.Mesh(wallGeo, material);
      wall.position.set(
        (currentPoint.x + nextPoint.x) / 2 - center.x,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2 - center.z
      );
      wall.rotation.y = -angle;
      this.add(wall);
    }

    this.position.y = 2;
  }
}

// export class D2Wall extends THREE.Group {
//   constructor({ points }) {
//     const walls = super();
//     walls.name = "walls";
//     const material = new THREE.LineBasicMaterial({
//       color: 0x0000ff,
//       linewidth: 5,
//     });
//     const geometry = new THREE.BufferGeometry().setFromPoints(points);
//     const line = new THREE.Line(geometry, material);
//     line.name = "walls";
//     return line;
//   }
// }

export class D2Wall extends THREE.Group {
  constructor({ points, parentPosition, center }) {
    super();
    console.log(" p =", parentPosition);
    this.name = wallsName;
    points.forEach((point, idx) => {
      const current = point;
      const nextIdx = (1 + idx) % points.length;
      const next = points[nextIdx];

      this.draw(current, next, idx, nextIdx);
    });
    this.position.set(-center.x, center.y, -center.z);
  }

  draw = (current, nextPoint, idx, nextIdx) => {
    const offset = 25;
    // const offsetX =
    const newPoints = [
      { ...current },
      {
        x: current.x + (idx === 0 || idx === 3 ? -offset : offset),
        y: current.y,
        z: current.z + (idx === 1 || idx === 0 ? -offset : offset),
      },
      {
        x: nextPoint.x + (nextIdx === 0 || nextIdx === 3 ? -offset : offset),
        y: current.y,
        z: nextPoint.z + (nextIdx === 1 || nextIdx === 0 ? -offset : offset),
      },
      {
        ...nextPoint,
      },
    ];

    const shape = new Shape({ points: newPoints });
    const geometry = new THREE.ShapeGeometry(shape);
    const material = new THREE.MeshBasicMaterial({ color: "red" });
    const mesh = new THREE.Mesh(geometry, material);
    mesh.rotation.x = -Math.PI / 2;
    mesh.name = wallName;
    mesh.userData = {
      ...mesh.userData,
      points: newPoints,
    };
    this.add(mesh);
  };
}
