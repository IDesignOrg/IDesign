import { THREE } from "../../../three.js";
import {
  getCoordsFromVectex,
  getStraightLineX,
  getStraightLineZ,
} from "../../calculater.js";
import { floorName, roomName } from "../objectConf/objectNames";
import { Circles } from "./circles.js";
import { D2Floor } from "./floor.js";
import { D2Wall, D3Wall } from "./wall.js";

export class D3Room extends THREE.Group {
  constructor({ object }) {
    const room = super();
    room.name = "room";
    const floor = object.children.find((obj) => obj.name === "floor");
    if (!floor) return;
    const points = getCoordsFromVectex(floor);
    const newFloor = new D2Floor({ points });
    room.add(newFloor);
    const walls = new D3Wall({ points });
    room.add(walls);

    return room;
  }
}

// export class D3Room extends THREE.Group {
//   constructor({ points, name = roomName }) {
//     const room = super();
//     room.name = name;

//     const floor = createPlaneFromPoints(points);
//     floor.name = floorName;
//     room.add(floor);

//     const walls = new Wall({ points });
//     room.add(walls);

//     return room;
//   }
// }

export class D2Room extends THREE.Group {
  constructor({ points }) {
    const roomGroup = super();
    roomGroup.updateFloor = this.updateFloor;
    const mesh = new D2Floor({ points });
    const walls = new D2Wall({ points });
    roomGroup.add(walls);
    roomGroup.add(mesh);
    return roomGroup;
  }

  updateWalls = ({ points }) => {
    const newWalls = new D2Wall({ points });
    this.remove(this.getObjectByName("walls"));
    this.add(newWalls);
  };

  drawShadow = ({ points }) => {
    const newFloor = new D2Floor({ points });
    const newCircleGroup = new Circles({ points });
    this.updateWalls({ points });
    this.remove(this.getObjectByName("circleGroup"));
    this.remove(this.getObjectByName("floor"));
    this.add(newCircleGroup);
    this.add(newFloor);
  };

  updateFloor = ({ points, circleIdx }) => {
    const floor = this.children.find((obj) => obj.name === "floor");
    const originPoints = getCoordsFromVectex(floor);
    originPoints[circleIdx] = new THREE.Vector3(
      getStraightLineX({ originPoints, points, index: circleIdx }),
      0,
      getStraightLineZ({ originPoints, points, index: circleIdx })
    );
    const mesh = new D2Floor({ points: originPoints });
    mesh.position.y = 1;
    mesh.rotation.x = -Math.PI / 2;
    const newCircleGroup = new Circles({ points: originPoints });
    newCircleGroup.visible = true;
    this.updateWalls({ points });
    this.remove(this.getObjectByName("circleGroup"));
    this.remove(floor);
    this.add(newCircleGroup);
    this.add(mesh);
  };
}

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
