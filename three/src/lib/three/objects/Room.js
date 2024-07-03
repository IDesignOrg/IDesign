import { floorName, roomName } from "../objectConf/objectNames";
import * as THREE from "../three.module.js";
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

export class D2Room {
  constructor({ points }) {
    const roomGroup = new THREE.Group();
    const mesh = new D2Floor({ points });
    // const shape = new THREE.Shape();
    // // z축의 기준으로 반대방향
    // // 왜인지는 모름...
    // shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
    // for (let i = 1; i < points.length; i++) {
    //   shape.lineTo(points[i].x, -points[i].z);
    // }
    // shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

    // const geometry = new THREE.ShapeGeometry(shape);
    // const material = new THREE.MeshBasicMaterial({
    //   color: "green",
    //   side: THREE.DoubleSide,
    // });
    // const mesh = new THREE.Mesh(geometry, material);
    mesh.name = "floor";
    mesh.rotation.x = -Math.PI / 2; // 평면을 바닥과 수평하게 회전
    roomGroup.add(mesh);
    return roomGroup;
  }
}

// for 3d
const createPlaneFromPoints = (points) => {
  const shape = new THREE.Shape();
  /*
    z축 기준으로 반대방향
    why...?
     */
  shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
  for (let i = 1; i < points.length; i++) {
    shape.lineTo(points[i].x, -points[i].z);
  }
  shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

  const geometry = new THREE.ShapeGeometry(shape);
  const material = new THREE.MeshBasicMaterial({
    color: "green",
    side: THREE.DoubleSide,
  });
  const mesh = new THREE.Mesh(geometry, material);
  mesh.rotation.x = -Math.PI / 2;
  return mesh;
};

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
