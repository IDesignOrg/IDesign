import { THREE } from "../loader/three.js";
import {
  caclulateIntersectionPoint,
  calculateAngle,
  calculateDistance,
  calculateOffsetPoints,
} from "../calculater.js";
import { wallName, wallsName } from "../objectConf/objectNames.js";
import { Shape } from "./floor.js";
import { wallHeight } from "../objectConf/length.js";
import { wallMaterial } from "../loader/papers.js";

export class D3Wall extends THREE.Group {
  constructor({ points, center }) {
    super();
    this.userData = {
      ...this.userData,
    };
    this.name = "walls";
    for (let i = 0; i < points.length; i++) {
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint);
      const wallGeo = new THREE.PlaneGeometry(width, wallHeight);

      const material = wallMaterial;
      const wall = new THREE.Mesh(wallGeo, material);
      wall.name = "wall";
      wall.userData = {
        ...wall.userData,
      };
      wall.position.set(
        (currentPoint.x + nextPoint.x) / 2 - center.x,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2 - center.z
      );
      wall.userData = {
        ...wall.userData,
      };
      wall.rotation.y = -angle;
      this.add(wall);
    }

    this.position.y = 2;
  }
}

export class D2Wall extends THREE.Group {
  constructor({ points, center, color }) {
    super();
    // this.parentPosiion = parentPosition
    this.name = wallsName;
    const offsetLines = [];
    points.forEach((point, idx) => {
      const i = idx;
      const distance = 25;
      const p1 = point;
      const p2 = points[(i + 1) % points.length];
      const offsetPoints = calculateOffsetPoints(p1, p2, distance);
      // console.log(p1, p2, offsetPoints);
      offsetLines.push(offsetPoints);
    });
    // console.log(offsetLines);
    // const newPoints = []

    const pointsArray = [];
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
      // console.log(x1, z1, x2, z2, x3, z3, x4, z4, offsetLines  );
      let p = caclulateIntersectionPoint(x1, z1, x2, z2, x3, z3, x4, z4);
      const x = p.x,
        y = p.y;

      pointsArray.push({ x, z: y, y: 1 });
    });

    points.forEach((point, idx) => {
      const current = point;
      const nextIdx = (1 + idx) % points.length;

      const newPoints = [
        current,
        pointsArray[idx],
        pointsArray[nextIdx],
        points[nextIdx],
      ];

      this.newDraw({ newPoints, color });
    });

    this.position.set(-center.x, center.y, -center.z);
  }

  newDraw = ({ newPoints, color }) => {
    const shape = new Shape({ points: newPoints });
    const geometry = new THREE.ShapeGeometry(shape);
    const material = new THREE.MeshBasicMaterial({ color });
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
