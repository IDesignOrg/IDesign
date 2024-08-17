import { THREE } from "../loader/three.js";
import {
  caclulateIntersectionPoint,
  calculateAngle,
  calculateDistance,
  calculateOffsetPoints,
} from "../calculater.js";
import { floorMaterial, wallMaterial } from "../../three.js";
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

// 변의 방향에 수직 방향 벡터 계산 함수

export class D2Wall extends THREE.Group {
  constructor({ points, parentPosition, center }) {
    super();
    // this.parentPosition = parentPosition
    this.name = wallsName;
    const offsetLines = [];
    points.forEach((point, idx) => {
      const i = idx;
      const distance = 25;
      const p1 = point;
      const p2 = points[(i + 1) % points.length];
      const offsetPoints = calculateOffsetPoints(p1, p2, distance);
      offsetLines.push(offsetPoints);
    });
    // console.log(offsetLines);
    // const newPoints = []

    const pointsArray = [];
    // console.log(offsetLines);
    offsetLines.forEach((current, idx) => {
      const prev =
        offsetLines[(idx - 1 + offsetLines.length) % offsetLines.length];

      const x1 = current[0].x;
      const z1 = current[0].z;
      const x2 = current[1].x;
      const z2 = current[1].z;

      const x3 = prev[0].x;
      const z3 = prev[0].z;
      const x4 = prev[1].x;
      const z4 = prev[1].z;

      const { x, y } = caclulateIntersectionPoint(
        x1,
        z1,
        x2,
        z2,
        x3,
        z3,
        x4,
        z4
      );
      // console.log(x, y);
      pointsArray.push({ x, z: y, y: 1 });
    });
    console.log(pointsArray);

    points.forEach((point, idx) => {
      const current = point;
      const nextIdx = (1 + idx) % points.length;

      const newPoints = [
        current,
        pointsArray[idx],
        pointsArray[nextIdx],
        points[nextIdx],
      ];

      this.newDraw({ newPoints });
    });

    this.position.set(-center.x, center.y, -center.z);
  }

  newDraw = ({ newPoints }) => {
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

  draw = (current, nextPoint, idx, nextIdx, prev) => {
    let angle_next = Math.abs(calculateAngle(current, nextPoint) / Math.PI);
    let angle_prev = Math.abs(calculateAngle(prev, current) / Math.PI);
    // console.log(
    //   "prev=",
    //   angle_prev,
    //   "prev도 =",
    //   Math.PI * angle_prev * (180 / Math.PI),
    //   "next = ",
    //   angle_next,
    //   "next도=",
    //   Math.PI * angle_next * (180 / Math.PI)
    // );

    const offset = 25;
    let offsetPrev = offset * Math.abs(angle_prev);
    let offsetNext = offset * Math.abs(angle_next);
    (offsetPrev = 25), (offsetNext = 25);
    // const offsetX =

    const newPoints = [
      { ...current },
      {
        x: current.x + (idx === 0 || idx === 3 ? -offsetPrev : offsetPrev),
        y: current.y,
        z: current.z + (idx === 1 || idx === 0 ? -offsetPrev : offsetPrev),
      },
      {
        x:
          nextPoint.x +
          (nextIdx === 0 || nextIdx === 3 ? -offsetNext : offsetNext),
        y: current.y,
        z:
          nextPoint.z +
          (nextIdx === 1 || nextIdx === 0 ? -offsetNext : offsetNext),
      },
      {
        ...nextPoint,
      },
    ];
    console.log("current=", current, "newPoint=", newPoints);
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
