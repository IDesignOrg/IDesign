import {
  RotationController,
  RotationControllerY,
  material,
} from "../../../main.js";
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
import { ShadowLines } from "./shadowLines.js";
import { D2Wall, D3Wall, calculateAngle, wallHeight } from "./wall.js";
import { uuidv4 } from "../../uuid.js";

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

export class D2Room extends THREE.Group {
  lines = null;
  clickedPoints = null;

  constructor({ points }) {
    super();
    this.name = "shadow";
    this.clickedPoints = points[0];
    this.drawLines(points);
    this.userData = {};
  }

  getShadowPoints = () => {
    const shadow = this.getObjectByName("shadowLines");
    const points = [];
    const positions = shadow.geometry.attributes.position.array;
    for (let i = 0; i < positions.length; i += 3) {
      points.push(
        new THREE.Vector3(positions[i], positions[i + 1], positions[i + 2])
      );
    }
    points.pop();
    return points;
  };

  createRoom = ({ cameraZoom }) => {
    if (this.getObjectByName("floor")) {
      this.remove(this.getObjectByName("floor"));
    }
    const box = new THREE.Box3();
    box.setFromCenterAndSize(
      new THREE.Vector3(1, 1, 1),
      new THREE.Vector3(2, 1, 3)
    );

    // const helper = new THREE.Box3Helper(box, 0xffff00);
    // this.add(helper);
    const points = this.getShadowPoints();
    // console.log("createRoom points = ", points);
    const center = calculateCenter(points);
    this.position.set(center.x, center.y, center.z);
    const floor = new D2Floor({ points, center });
    this.userData = {
      ...this.userData,
      center,
      points,
    };
    if (!this.getObjectByName("circleGroup")) {
      this.createDots({ center });
    }
    const shadowLines = this.getObjectByName("shadowLines");
    // console.log(shadowLines);
    shadowLines.position.set(-center.x, center.y, -center.z);
    this.add(floor);
    const rotationController = new RotationController({ cameraZoom });
    rotationController.visible = false;
    // rotationController.position.set(0, 10, 0);
    this.add(rotationController);
  };

  positioningChildren = () => {
    return;
    let center = calculateCenter(this.getShadowPoints());
    center = {
      x: -center.x,
      y: center.y,
      z: -center.z,
    };
    console.log("center = ", center);
    this.children.forEach((child) => {
      if (child.name === "rotationController") {
        child.position.set(center.x, center.y, center.z);
        console.log(child.position);
      } else {
        child.position.set(center.x, center.y, center.z);
      }
    });
  };

  createDots = ({ center }) => {
    const points = this.getShadowPoints();
    const circleGroup = new Circles({ center, points });
    circleGroup.name = "circleGroup";
    this.add(circleGroup);
  };

  drawLines = (points) => {
    points.push(new THREE.Vector3(points[0].x, points[0].y, points[0].z));
    const shadow = this.getObjectByName("shadowLines");
    if (shadow) {
      this.remove(shadow);
    }
    const line = new ShadowLines({ points });
    line.name = "shadowLines";
    this.add(line);
    // console.log(this);
  };

  updateLines = ({ points, circleIdx }) => {
    const circleGroup = this.getObjectByName("circleGroup");
    const originPoints = this.getShadowPoints();
    const x = getStraightLineX({ originPoints, points, index: circleIdx });
    const z = getStraightLineZ({ originPoints, points, index: circleIdx });
    circleGroup.children[circleIdx].position.set(x, points.y, z);
    let newPoints = circleGroup.children.map((circle) => circle.position);

    this.drawLines(newPoints);
  };

  onClick = () => {
    const controller = this.getObjectByName("rotationController");
    controller.visible = true;
  };

  onMouseUp = ({ cameraZoom }) => {
    this.createRoom({ cameraZoom });
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
