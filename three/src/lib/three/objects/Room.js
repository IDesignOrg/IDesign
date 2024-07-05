import { getCoordsFromVectex } from "../../calculater.js";
import { floorName, roomName } from "../objectConf/objectNames";
import * as THREE from "../three.module.js";
import { Circles } from "./circles.js";
import { D2Floor } from "./floor.js";
import { D3Wall } from "./wall.js";

export class D3Room {
  constructor({ points, name = roomName }) {
    const room = new THREE.Group();
    room.name = name;

    const floor = createPlaneFromPoints(points);
    floor.name = floorName;
    room.add(floor);

    const walls = new Wall({ points });
    room.add(walls);

    return room;
  }
}

export class D2Room extends THREE.Group {
  constructor({ points }) {
    const roomGroup = super();
    roomGroup.updateFloor = this.updateFloor;
    const mesh = new D2Floor({ points });
    mesh.name = "floor";
    mesh.rotation.x = -Math.PI / 2; // 평면을 바닥과 수평하게 회전
    roomGroup.add(mesh);
    return roomGroup;
  }

  updateFloor = ({ points, circleIdx }) => {
    const floor = this.children.find((obj) => obj.name === "floor");
    const originPoints = getCoordsFromVectex(floor);
    // getStraightLine({ originPoints, points, index: circleIdx });
    originPoints[circleIdx] = new THREE.Vector3(
      getStraightLineX({ originPoints, points, index: circleIdx }),
      0,
      getStraightLineZ({ originPoints, points, index: circleIdx })
    );
    // console.log(
    //   getStraightLineX({ originPoints, points, index: circleIdx }),
    //   getStraightLineZ({ originPoints, points, index: circleIdx })
    // );
    // originPoints[circleIdx] = points;
    // checkAngleAndSnap({ index: circleIdx, points: originPoints });
    const mesh = new D2Floor({ points: originPoints });
    mesh.name = "floor";
    mesh.rotation.x = -Math.PI / 2;
    const newCircleGroup = new Circles({ points: originPoints });
    newCircleGroup.visible = true;
    this.remove(this.getObjectByName("circleGroup"));
    this.remove(floor);
    this.add(newCircleGroup);
    this.add(mesh);
  };
}

const getStraightLineZ = ({ originPoints, points, index }) => {
  const prevIndex = (index - 1 + originPoints.length) % originPoints.length;
  const nextIndex = (index + 1) % originPoints.length;
  const tolerance = 10;
  if (Math.abs(originPoints[prevIndex].z - points.z) <= tolerance) {
    return originPoints[prevIndex].z;
  }
  if (Math.abs(originPoints[nextIndex].z - points.z) <= tolerance) {
    return originPoints[nextIndex].z;
  }
  return points.z;
};

const getStraightLineX = ({ originPoints, points, index }) => {
  const prevIndex = (index - 1 + originPoints.length) % originPoints.length;
  const nextIndex = (index + 1) % originPoints.length;
  const tolerance = 10;

  if (Math.abs(originPoints[prevIndex].x - points.x) <= tolerance) {
    return originPoints[prevIndex].x;
  }

  if (Math.abs(originPoints[nextIndex].x - points.x) <= tolerance) {
    return originPoints[nextIndex].x;
  }

  return points.x;
};

// function getAngle(p1, p2) {
//   const dx = p2.x - p1.x;
//   const dz = p2.z - p1.z;
//   return Math.atan2(dz, dx) * (180 / Math.PI);
// }
// const checkAngleAndSnap = ({ index, points }) => {
//   const prevIndex = (index - 1 + points.length) % points.length;
//   const nextIndex = (index + 1) % points.length;

//   const anglePrev = getAngle(points[prevIndex], points[index]);
//   const angleNext = getAngle(points[index], points[nextIndex]);
//   if (Math.abs(anglePrev - 90) < 5) {
//     snapToAngle(points[prevIndex], points[index], 90);
//   } else if (Math.abs(anglePrev - 180) < 5) {
//     snapToAngle(points[prevIndex], points[index], 180);
//   }
//   if (Math.abs(angleNext - 90) < 5) {
//     snapToAngle(points[index], points[nextIndex], 90);
//     // snapToAngle(points[index], points[nextIndex], 90);
//   } else if (Math.abs(angleNext - 180) < 5) {
//     snapToAngle(points[index], points[nextIndex], 180);
//     // snapToAngle(points[index], points[nextIndex], 180);
//   }
// };

// function snapToAngle(p1, p2, angle) {
//   const dx = p2.x - p1.x;
//   const dz = p2.z - p1.z;
//   const distance = Math.sqrt(dx * dx + dz * dz);
//   const radian = angle * (Math.PI / 180);

//   p2.x = p1.x + Math.cos(radian) * distance;
//   p2.z = p1.z + Math.sin(radian) * distance;
// }

// const getCoordsFromVectex = (obj) => {
//   const positionAttribute = obj.geometry.attributes.position;
//   const coordinates = [];
//   for (let i = 0; i < positionAttribute.count; i++) {
//     const vertex = new THREE.Vector3();
//     vertex.fromBufferAttribute(positionAttribute, i);
//     coordinates.push(new THREE.Vector3(vertex.x, 1, -vertex.y));
//   }

//   return coordinates;
// };

// // for 3d
// const createPlaneFromPoints = (points) => {
//   const shape = new THREE.Shape();
//   /*
//     z축 기준으로 반대방향
//     why...?
//      */
//   shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
//   for (let i = 1; i < points.length; i++) {
//     shape.lineTo(points[i].x, -points[i].z);
//   }
//   shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

//   const geometry = new THREE.ShapeGeometry(shape);
//   const material = new THREE.MeshBasicMaterial({
//     color: "green",
//     side: THREE.DoubleSide,
//   });
//   const mesh = new THREE.Mesh(geometry, material);
//   mesh.rotation.x = -Math.PI / 2;
//   return mesh;
// };

// import * as THREE from "../three.module.js";
// import { roomName } from "../objectConf/objectNames.js";
// import {
//   ceilingHeight,
//   planeLength,
//   planeWidth,
// } from "../objectConf/length.js";
// import { Floor } from "./floor.js";
// import { Ceiling } from "./ceiling.js";
// import { Wall } from "./wall.js";
// import { Resizer } from "./resizer.js";

// class Room {
//   constructor({
//     x = 0,
//     y = 0,
//     z = 0,
//     width = planeWidth,
//     length = planeLength,
//   }) {
//     const room = new THREE.Group();
//     room.name = roomName;

//     const floor = new Floor({ x, y, z, width, length });
//     room.add(floor);
//     const ceil = new Ceiling({ x, z, rotationY: -Math.PI / 1 });
//     room.add(ceil);
//     const westwall = new Wall({
//       x,
//       z: z + length / 2,
//       y: ceilingHeight / 2 + y,
//       width,
//       length: ceilingHeight,
//     });
//     const eastwall = new Wall({
//       x,
//       z: z - length / 2,
//       y: ceilingHeight / 2 + y,
//       width,
//       length: ceilingHeight,
//       rotationY: -Math.PI / 1,
//     });
//     const southwall = new Wall({
//       x: x - width / 2,
//       z: z,
//       y: ceilingHeight / 2 + y,
//       width: length,
//       length: ceilingHeight,
//       rotationY: -Math.PI / 2,
//     });
//     const northwall = new Wall({
//       x: x + width / 2,
//       z: z,
//       y: ceilingHeight / 2 + y,
//       width: length,
//       length: ceilingHeight,
//       rotationY: Math.PI / 2,
//     });

//     room.add(westwall);
//     room.add(eastwall);
//     room.add(southwall);
//     room.add(northwall);

//     const resizer = new Resizer({ width, length, floor });

//     room.add(resizer);
//     return room;
//   }
// }

// export { Room };
