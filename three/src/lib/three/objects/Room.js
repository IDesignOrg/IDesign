import { material } from "../../../main.js";
import { THREE } from "../../../three.js";
import {
  calculateCenter,
  getCoordsFromVectex,
  getStraightLineX,
  getStraightLineZ,
} from "../../calculater.js";
import { floorName, roomName } from "../objectConf/objectNames";
import { Circles } from "./circles.js";
import { D2Floor, Shape } from "./floor.js";
import { D2Wall, D3Wall, calculateAngle, wallHeight } from "./wall.js";

export class D3Room extends THREE.Group {
  constructor({ object }) {
    const room = super();
    room.name = "room";
    const floor = object.children.find((obj) => obj.name === "floor");
    if (!floor) return;
    const points = getCoordsFromVectex(floor);
    const newFloor = new D2Floor({ points });
    // room.add(new wallDepth({ points }));
    newFloor.material = material;
    room.add(newFloor);
    const walls = new D3Wall({ points });
    room.add(walls);
    const ceiling = new D2Floor({ points, height: 50 });
    room.add(ceiling);
    const chair = object.getObjectByName("chair");
    if (chair) {
      room.add(chair);
    }
    const center = calculateCenter(points);
    const ambientLight = new THREE.AmbientLight(0xf0f0f0, 1.2); // Ambient light
    ambientLight.name = "light1";
    ambientLight.position.set(center.x, 50, center.z);
    room.add(ambientLight);
    return room;
  }
}

class wallDepth extends THREE.Group {
  constructor({ points }) {
    const group = super();
    const height = wallHeight;
    const wallLength = 5; // 벽의 길이
    for (let i = 0; i < points.length; i++) {
      const currentPoint = points[i];
      const angle = calculateAngle(
        currentPoint,
        points[(i + 1) % points.length]
      );
      const newCoordinates = getNewCoordinates(currentPoint, angle, wallLength);

      // Plane 생성
      const wallGeometry = new THREE.PlaneGeometry(wallLength, height);
      const wallMaterial = new THREE.MeshBasicMaterial({
        color: 0xaaaaaa,
        side: THREE.BackSide,
      });

      const wall = new THREE.Mesh(wallGeometry, wallMaterial);

      // Plane의 위치 설정 (currentPoint에서 바깥쪽으로 wallLength/2만큼 이동)
      const centerX =
        currentPoint.x - Math.cos(angle + Math.PI / 4) * (wallLength / 2);
      const centerZ =
        currentPoint.z - Math.sin(angle + Math.PI / 4) * (wallLength / 2);

      wall.position.set(centerX, height / 2, centerZ);

      // Plane의 회전 설정
      wall.rotation.y = -(angle + Math.PI / 4);

      group.add(wall);
    }
    return group;
  }
}

function getNewCoordinates(currentPoint, angle, distance) {
  const angleOffset = Math.PI / 4; // 45도 각도
  const newX = currentPoint.x + Math.cos(angle + angleOffset) * distance;
  const newZ = currentPoint.z + Math.sin(angle + angleOffset) * distance;
  return { x: newX, z: newZ };
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
  constructor({ points, name }) {
    console.log(name);
    const roomGroup = super();
    roomGroup.updateFloor = this.updateFloor;
    const mesh = new D2Floor({ points, name });
    const walls = new D2Wall({ points });

    const shape = new Shape({ points });
    const geometry = new THREE.ShapeGeometry(shape);
    var outlineMaterial = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
    var outlineMesh = new THREE.Mesh(geometry, outlineMaterial);
    outlineMesh.name = "floor-outline";
    outlineMesh.rotation.x = -Math.PI / 2;
    outlineMesh.scale.multiplyScalar(1.05);
    roomGroup.add(outlineMesh);
    roomGroup.add(walls);
    roomGroup.add(mesh);
    return roomGroup;
  }

  updateWalls = ({ points }) => {
    const newWalls = new D2Wall({ points });
    this.remove(this.getObjectByName("walls"));
    this.add(newWalls);
    0;
  };
  updateFloorOutline = ({ points }) => {
    this.remove(this.getObjectByName("floor-outline"));
    const shape = new Shape({ points });
    const geometry = new THREE.ShapeGeometry(shape);
    var outlineMaterial = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
    var outlineMesh = new THREE.Mesh(geometry, outlineMaterial);
    outlineMesh.name = "floor-outline";
    outlineMesh.rotation.x = -Math.PI / 2;
    outlineMesh.position.y = 1;
    // outlineMesh.scale.multiplyScalar(1);

    this.add(outlineMesh);
  };
  drawShadow = ({ points }) => {
    const newFloor = new D2Floor({ points, name: "shadow" });
    const newCircleGroup = new Circles({ points });
    this.updateFloorOutline({ points });
    // this.updateWalls({ points });
    this.remove(this.getObjectByName("circleGroup"));
    this.remove(this.getObjectByName("floor"));
    this.add(newCircleGroup);
    this.add(newFloor);
  };

  updateFloor = ({ points, circleIdx }) => {
    const floor = this.getObjectByName("floor");
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
    this.updateFloorOutline({ points: originPoints });
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
