import { Circles } from "./circles.js";
import { D2Floor } from "./floor.js";
import { ShadowLines } from "./shadowLines.js";
import { D2Wall, D3Wall } from "./wall.js";
import { Arrow } from "./Arrow.js";
import { THREE } from "../loader/three.js";
import {
  calculateArea,
  calculateCenter,
  calculateDistance,
  calculateAngle,
} from "../calculater.js";
import { Text } from "./text.js";
import { RotationController } from "./rotationController.js";
import {
  areaRenderOrder,
  arrowsRenderOrder,
  arrowsY,
  roomY,
} from "../objectConf/renderOrders.js";
import { wallsName } from "../objectConf/objectNames.js";

export class D3Room extends THREE.Group {
  constructor({ object }) {
    super();
    this.name = "room";
    this.userData = { ...object.userData };

    const { center, points } = this.userData;
    this.position.set(center.x, center.y, center.z);

    const floor = object.getObjectByName("floor").clone();
    this.add(floor);
    const ceiling = new D2Floor({ points, height: 50, center });
    ceiling.name = "ceiling";
    this.add(ceiling);

    const walls = new D3Wall({ points, center });
    walls.name = "walls";
    this.add(walls);

    object.children.forEach((obj) => {
      if (obj.name === "chair" || obj.name === "desk") {
        // ...create object
      }
    });
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
    return this.userData.points;
  };

  createRoom = ({ cameraZoom }) => {
    if (this.getObjectByName("arrow")) {
      this.remove(this.getObjectByName("arrow"));
    }

    if (this.getObjectByName("floor")) {
      this.remove(this.getObjectByName("floor"));
    }

    if (this.getObjectByName("area")) {
      this.remove(this.getObjectByName("area"));
    }

    const points = this.userData.points;

    const center = calculateCenter(points);
    this.addArea();
    this.position.set(center.x, roomY, center.z);
    const floor = new D2Floor({ points, center });
    this.userData = {
      ...this.userData,
      center,
      points,
      rotation: 0,
    };

    const shadowLines = this.getObjectByName("shadowLines");
    shadowLines.position.set(-center.x, center.y, -center.z);
    shadowLines.visible = false;

    const rotationController = new RotationController({ cameraZoom });
    rotationController.visible = false;
    this.addArrow({ cameraZoom });
    this.add(floor);
    this.add(rotationController);
    this.addWalls({ points, center });
    this.createDots({ points, center });

    this.getObjectByName("floor").updateMatrix();
  };

  // addzz = () => {
  //   this.add()
  // }

  addWalls = ({ points, center }) => {
    if (this.getObjectByName(wallsName)) {
      this.remove(this.getObjectByName(wallsName));
    }
    const parentPosition = new THREE.Vector3();
    this.getWorldPosition(parentPosition);

    const walls = new D2Wall({ points, parentPosition, center });
    this.add(walls);
  };

  setPosition = (center) => {
    const prevCenter = this.userData.center;
    const offset = {
      x: center.x - prevCenter.x,
      y: center.y,
      z: center.z - prevCenter.z,
    };
    const newPoints = this.userData.points.map((point) => {
      return {
        x: point.x + offset.x,
        y: prevCenter.y,
        z: point.z + offset.z,
      };
    });
    this.position.set(center.x, roomY, center.z);
    this.userData = {
      ...this.userData,
      center,
      points: newPoints,
    };
  };

  addArea = (angle) => {
    if (this.getObjectByName("area")) {
      this.remove(this.getObjectByName("area"));
    }
    const points = this.userData.points;

    const area = new Text({ text: String(calculateArea(points) + " m2") });
    area.name = "area";
    area.rotation.x = -Math.PI / 2;
    area.position.y = roomY + 0.5;
    area.renderOrder = areaRenderOrder;

    if (angle) {
    }
    this.add(area);
  };

  addArrow = ({ cameraZoom }) => {
    const points = this.getShadowPoints();
    const group = new THREE.Group();
    group.name = "arrow";
    for (let i = 0; i < points.length; i++) {
      // const arrowGroup = new THREE.Group();
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
      // arrowGroup.add(arrow);
      group.add(arrow);
    }
    group.renderOrder = arrowsRenderOrder;
    group.position.y = arrowsY + 5;
    this.add(group);
  };

  createDots = ({ center, points }) => {
    if (this.getObjectByName("circleGroup")) {
      this.remove(this.getObjectByName("circleGroup"));
    }
    const walls = this.getObjectByName(wallsName);
    const newPoints = [];
    walls.children.forEach((wall) => {
      // console.log(wall);
    });
    const circles = new Circles({ points });
    circles.position.set(-center.x, 5, -center.z);
    this.add(circles);
  };

  drawLines = (points) => {
    this.userData.points = [...points];
    points.push(new THREE.Vector3(points[0].x, points[0].y, points[0].z));
    const shadow = this.getObjectByName("shadowLines");
    if (shadow) {
      this.remove(shadow);
    }
    const line = new ShadowLines({ points });
    line.name = "shadowLines";
    line.position.set(0, 0, 0);
    this.add(line);
    // console.log(this);
  };

  updateLines = ({ point, circleIdx }) => {
    const circleGroup = this.getObjectByName("circleGroup");
    circleGroup.children[circleIdx].position.set(point.x, point.y, point.z);
    const newPoints = circleGroup.children.map((c) => c.position);
    this.userData.points = [...newPoints];
    const center = this.userData.center;
    if (this.getObjectByName("shadowLines")) {
      this.remove(this.getObjectByName("shadowLines"));
    }
    newPoints.push({ ...newPoints[0] });
    const line = new ShadowLines({ points: newPoints });

    // line.position.copy(this.position);
    line.position.set(-center.x, point.y, -center.z);
    // line.position.set(0, 0, 0);
    line.name = "shadowLines";
    this.add(line);
  };

  onMouseUp = ({ cameraZoom }) => {
    this.createRoom({ cameraZoom });
  };
}
