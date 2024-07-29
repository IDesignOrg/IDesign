import { RotationController, material } from "../../main.js";

import { Circles } from "./circles.js";
import { D2Floor } from "./floor.js";
import { ShadowLines } from "./shadowLines.js";
import { D3Wall } from "./wall.js";
import { Arrow } from "./Arrow.js";
import { THREE } from "../loader/three.js";
import {
  calculateArea,
  calculateCenter,
  calculateDistance,
  getStraightLineX,
  getStraightLineZ,
  calculateAngle,
} from "../calculater.js";
import { Text } from "./text.js";

export class D3Room extends THREE.Group {
  constructor({ object }) {
    const room = super();
    room.userData = {
      ...object.userData,
    };
    const center = room.userData.center;
    console.log(center);
    room.position.set(center.x, center.y, center.z);
    room.name = "room";
    const floor = object.children.find((obj) => obj.name === "floor");
    if (!floor) return;
    const points = room.userData.points;
    // const points = getCoordsFromVectex(floor);
    const newFloor = new D2Floor({ points, center });
    // room.add(new wallDepth({ points }));
    newFloor.material = material;
    room.add(newFloor);
    const walls = new D3Wall({ points, center });
    room.add(walls);
    const ceiling = new D2Floor({ points, height: 50, center });
    room.add(ceiling);
    const chair = object.getObjectByName("chair");
    if (chair) {
      room.add(chair);
    }
    // const center = calculateCenter(points);
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

    // const helper = new THREE.Box3Helper(box, 0xffff00);
    // this.add(helper);
    const points = this.getShadowPoints();
    const area = new Text({ text: String(calculateArea(points) + " m2") });
    area.rotation.x = -Math.PI / 2;
    area.position.y = 10;
    this.add(area);

    // console.log("createRoom points = ", points);
    const center = calculateCenter(points);

    this.position.set(center.x, center.y, center.z);
    const floor = new D2Floor({ points, center });
    this.userData = {
      ...this.userData,
      center,
      area,
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
    this.addArrow({ cameraZoom });
    // rotationController.position.set(0, 10, 0);
    this.add(rotationController);
  };

  addArrow = ({ cameraZoom }) => {
    const points = this.getShadowPoints();

    for (let i = 0; i < points.length; i++) {
      const arrowGroup = new THREE.Group();
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const width = calculateDistance(currentPoint, nextPoint);
      const angle = calculateAngle(currentPoint, nextPoint);
      const offset = 10;
      const midX = (currentPoint.x + nextPoint.x) / 2;
      const midZ = (currentPoint.z + nextPoint.z) / 2;
      const adjustedPosition = new THREE.Vector3(
        midX - offset * Math.sin(angle),
        currentPoint.y,
        midZ + offset * Math.cos(angle)
      );

      const position = new THREE.Vector3(
        adjustedPosition.x - this.position.x,
        adjustedPosition.y,
        adjustedPosition.z - this.position.z
      );

      const arrow = new Arrow({ width, angle, position, cameraZoom });
      arrowGroup.add(arrow);
      this.add(arrowGroup);
    }
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

  onMouseUp = ({ cameraZoom }) => {
    this.createRoom({ cameraZoom });
  };
}
