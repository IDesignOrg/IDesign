import { Circles } from "./circles.js";
import { D2Floor, Shape } from "./floor.js";
import { D2Wall, D3Wall } from "./wall.js";
import { Arrow } from "./Arrow.js";
import { THREE } from "../loader/three.js";
import {
  calculateArea,
  calculateCenter,
  calculateDistance,
  calculateAngle,
  calculateOffsetPoints,
  caclulateIntersectionPoint,
  getStraightLineX,
  getStraightLineZ,
} from "../calculater.js";
import { Text } from "./text.js";
import {
  areaRenderOrder,
  arrowsRenderOrder,
  arrowsY,
  circleY,
  lineShaodwY,
  roomY,
} from "../objectConf/renderOrders.js";
import {
  chairName,
  circleGroupName,
  deskName,
  lineShadowName,
  roomName,
  wallsName,
} from "../objectConf/objectNames.js";

import { MILLPerWidth, wallHeight } from "../objectConf/length.js";

export class D3Room extends THREE.Group {
  constructor({ object }) {
    super();
    this.name = "room";
    this.userData = { ...object.userData };

    const { center, points } = this.userData;
    this.position.set(center.x, center.y, center.z);

    const floor = object.getObjectByName("floor").clone();
    this.add(floor);
    const ceiling = new D2Floor({
      points,
      height: wallHeight + 1,
      center,
      name: "ceil",
    });
    ceiling.name = "ceiling";
    this.add(ceiling);

    const walls = new D3Wall({ points, center, name: "floor" });
    walls.name = "walls";
    this.add(walls);
    object.children.forEach((obj) => {
      if (obj.name === chairName || obj.name === deskName) {
        const cloneObject = obj.clone();
        this.add(cloneObject);
      }
    });
  }
}

export class D2Room extends THREE.Group {
  constructor({ points, nodeInfo, cameraZoom }) {
    super();
    this.name = "shadow";
    if (nodeInfo) {
      this.userData = {
        ...this.userData,
        points: nodeInfo.points,
        oid: nodeInfo.oid,
        rotation: nodeInfo.rotation,
      };
      this.rotation.y = nodeInfo.rotation;
      this.createRoom({ cameraZoom });
    } else {
      this.userData = {
        ...this.userData,
        rotation: 0,
        points,
      };
      this.drawLines(points);
    }
  }

  getShadowPoints = () => {
    return this.userData.points;
  };

  createRoom = ({ cameraZoom }) => {
    this.name = roomName;
    if (this.getObjectByName("floor")) {
      this.remove(this.getObjectByName("floor"));
    }
    if (this.getObjectByName(lineShadowName)) {
      this.remove(this.getObjectByName(lineShadowName));
    }

    const points = this.userData.points;
    const center = calculateCenter(points);
    this.position.set(center.x, roomY, center.z);
    const floor = new D2Floor({ points, center });

    this.userData = {
      ...this.userData,
      center,
      // points,
    };

    const shadowLines = this.getObjectByName("shadowLine");
    if (shadowLines) {
      shadowLines.position.set(-center.x, center.y, -center.z);
      shadowLines.visible = false;
    }

    this.addArrow({ cameraZoom });
    this.add(floor);
    this.addArea();
    this.addWalls({ points, center });
    this.createDots({ points, center });
  };

  addWalls = ({ points, center }) => {
    if (this.getObjectByName(wallsName)) {
      this.remove(this.getObjectByName(wallsName));
    }
    const color = "rgb(84,84,84)";
    const walls = new D2Wall({ points, center, color });
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

  addArea = () => {
    if (this.getObjectByName("area")) {
      this.remove(this.getObjectByName("area"));
    }
    const points = this.userData.points;
    const position = new THREE.Vector3(0, 0, 0);
    const area = new Text({
      text: String(calculateArea(points) + " m2"),
      position,
      angle: 0,
    });
    area.name = "area";
    area.rotation.x = -Math.PI / 2;
    area.position.set(0, 5, 0);
    area.renderOrder = areaRenderOrder;

    this.add(area);
  };

  addArrow = ({ cameraZoom }) => {
    // 각변의 화살표와 길이
    const points = this.userData.points;
    if (this.getObjectByName("arrow")) {
      this.remove(this.getObjectByName("arrow"));
    }
    const group = new THREE.Group();
    group.name = "arrow";
    group.position.y = arrowsY;
    for (let i = 0; i < points.length; i++) {
      // const arrowGroup = new THREE.Group();
      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const width = calculateDistance(currentPoint, nextPoint);
      const angle = calculateAngle(currentPoint, nextPoint);
      const offset = 15;
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
      group.add(arrow);
      const distance = new Text({
        text: `${(width / MILLPerWidth).toFixed(2)}mm`,
        position,
        angle,
      });
      group.add(distance);
    }
    group.renderOrder = arrowsRenderOrder;
    this.add(group);
  };

  createDots = ({ center, points }) => {
    if (this.getObjectByName("circleGroup")) {
      this.remove(this.getObjectByName("circleGroup"));
    }
    const circles = new Circles({ points });
    circles.position.set(-center.x, circleY, -center.z);
    this.add(circles);
  };

  drawLines = (points) => {
    if (this.getObjectByName("shadowLine")) {
      this.remove(this.getObjectByName("shadowLine"));
    }
    this.userData.points = [...points];
    this.userData.center = center;
    const center = calculateCenter(points);
    const color = "rgb(110,100,255)";
    const shadowLine = new D2Wall({ points, center, color });
    shadowLine.name = "shadowLine";
    shadowLine.position.set(0, 0, 0);
    this.add(shadowLine);
  };

  updateLines = ({ point, cameraZoom, circleId, circleIdx }) => {
    // 드래그를 통해 방의 모양 등을 수정하는 중
    if (this.getObjectByName(lineShadowName)) {
      this.remove(this.getObjectByName(lineShadowName));
    }
    if (this.getObjectByName("arrow")) {
      this.remove(this.getObjectByName("arrow"));
    }
    if (this.getObjectByName("area")) {
      this.remove(this.getObjectByName("area"));
    }
    const { points, center } = this.userData;
    const nextIdx = (1 + circleIdx) % points.length;
    const prevIdx = (circleIdx - 1 + points.length) % points.length;

    const prevPoint = new THREE.Vector3(
      points[prevIdx].x,
      points[prevIdx].y,
      points[prevIdx].z
    );
    const nextPoint = points[nextIdx];
    const currentPoint = new THREE.Vector3(
      getStraightLineX({ point, prevPoint, nextPoint }),
      point.y,
      getStraightLineZ({ point, prevPoint, nextPoint })
    );
    // const currentPoint = point;
    console.log(currentPoint);

    const offsetDistance = 25;
    const offsetPoint_prev = calculateOffsetPoints(
      prevPoint,
      currentPoint,
      offsetDistance
    );
    const offsetPoint_next = calculateOffsetPoints(
      currentPoint,
      nextPoint,
      offsetDistance
    );

    const p = caclulateIntersectionPoint(
      offsetPoint_prev[0].x,
      offsetPoint_prev[0].z,
      offsetPoint_prev[1].x,
      offsetPoint_prev[1].z,
      offsetPoint_next[0].x,
      offsetPoint_next[0].z,
      offsetPoint_next[1].x,
      offsetPoint_next[1].z
    );
    const calculatePoint = [
      {
        x: prevPoint.x,
        y: prevPoint.y,
        z: prevPoint.z,
      },
      {
        x: offsetPoint_prev[0].x,
        y: offsetPoint_prev[0].y,
        z: offsetPoint_prev[0].z,
      },
      {
        x: p.x,
        y: 0,
        z: p.y,
      },
      offsetPoint_next[1],
      nextPoint,
      point,
    ];
    const group = new THREE.Group();
    group.name = lineShadowName;
    const shape = new Shape({ points: calculatePoint });
    const geometry = new THREE.ShapeGeometry(shape);
    const color = "rgb(110,100,255)";
    const material = new THREE.MeshBasicMaterial({ color });
    const shadow = new THREE.Mesh(geometry, material);

    group.add(shadow);
    shadow.position.y = 4;
    shadow.rotation.x = -Math.PI / 2;
    shadow.position.set(-center.x, lineShaodwY, -center.z);

    const width_1 = calculateDistance(prevPoint, currentPoint);
    const width_2 = calculateDistance(currentPoint, nextPoint);
    const angle_1 = calculateAngle(prevPoint, currentPoint);
    const angle_2 = calculateAngle(currentPoint, nextPoint);
    const offset = 15;
    const midX_1 = (currentPoint.x + prevPoint.x) / 2;
    const midX_2 = (currentPoint.x + nextPoint.x) / 2;
    const midZ_1 = (currentPoint.z + prevPoint.z) / 2;
    const midZ_2 = (currentPoint.z + nextPoint.z) / 2;

    const adjutsPosition_1 = new THREE.Vector3(
      midX_1 - offset * Math.sin(angle_1) - this.userData.center.x,
      0,
      midZ_1 + offset * Math.cos(angle_1) - this.userData.center.z
    );
    const adjutsPosition_2 = new THREE.Vector3(
      midX_2 - offset * Math.sin(angle_2) - this.userData.center.x,
      0,
      midZ_2 + offset * Math.cos(angle_2) - this.userData.center.z
    );
    const arrow_1 = new Arrow({
      width: width_1,
      angle: angle_1,
      position: adjutsPosition_1,
      cameraZoom,
    });
    const t_1 = new Text({
      text: `${(width_1 / MILLPerWidth).toFixed(2)}mm`,
      position: adjutsPosition_1,
      angle: angle_1,
    });

    arrow_1.position.y = 5;
    arrow_1.name = "arrow_1";
    group.add(t_1);
    group.add(arrow_1);
    const arrow_2 = new Arrow({
      width: width_2,
      angle: angle_2,
      position: adjutsPosition_2,
      cameraZoom,
    });
    const t_2 = new Text({
      text: `${(width_2 / MILLPerWidth).toFixed(2)}mm`,
      angle: angle_2,
      position: adjutsPosition_2,
    });

    arrow_2.position.y = 5;
    arrow_2.name = "arrow_2";
    group.add(arrow_2);
    group.add(t_2);

    this.getObjectById(circleId).position.set(
      currentPoint.x,
      point.y,
      currentPoint.z
    );
    const newPoints = this.getObjectByName(circleGroupName).children.map(
      (c) => c.position
    );
    group.position.y = lineShaodwY;
    this.userData.points = newPoints;
    this.add(group);
  };

  onMouseUp = ({ cameraZoom }) => {
    // 드래그를 통해 방의 면적 수정을 끝냄
    this.createRoom({ cameraZoom });
  };
}
