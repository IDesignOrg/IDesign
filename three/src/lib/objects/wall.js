import { THREE } from "../loader/three.js";
import { calculateAngle, calculateDistance } from "../calculater.js";
import { wallMaterial } from "../../main";
import { wallsName } from "../objectConf/objectNames";
import { roomY } from "../objectConf/renderOrders.js";

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

export class D2Wall extends THREE.Group {
  constructor({ points, parentPosition }) {
    super();
    this.name = "D2Wall";
    this.wallWidth = 10; // 벽의 넓이

    // 벽의 색상 배열 (각 벽마다 다르게 설정할 수 있음)
    const colors = ["red", "blue", "green", "yellow"];

    points.forEach((point, idx) => {
      const currentPoint = new THREE.Vector3(point.x, 0, point.z);
      const nextPoint = new THREE.Vector3(
        points[(idx + 1) % points.length].x,
        0,
        points[(idx + 1) % points.length].z
      );

      const direction = this.getDirection(currentPoint, nextPoint);
      const distance = currentPoint.distanceTo(nextPoint);

      return;
      const geometry = new THREE.PlaneGeometry(this.wallWidth, distance);
      const material = new THREE.MeshBasicMaterial({
        color: colors[idx % colors.length],
        side: THREE.DoubleSide,
      });
      const plane = new THREE.Mesh(geometry, material);

      const position = this.getPosition({
        currentPoint,
        nextPoint,
        parentPosition,
      });

      plane.position.set(position.x, roomY + 1, position.z);
      plane.rotation.x = -Math.PI / 2; // 벽이 수평으로 배치되도록
      plane.rotation.z = -this.getAngle(currentPoint, nextPoint) + Math.PI / 2;

      this.add(plane);
    });
  }

  // 두 점 사이의 방향 벡터를 계산
  getDirection(p1, p2) {
    return new THREE.Vector3().subVectors(p2, p1).normalize();
  }

  // 벽의 오프셋을 계산
  getOffset(direction, width) {
    return new THREE.Vector3(-direction.z, 0, direction.x)
      .normalize()
      .multiplyScalar(width / 2);
  }

  // 벽의 위치를 계산
  getPosition({ currentPoint, nextPoint, parentPosition }) {
    const center = new THREE.Vector3(
      (currentPoint.x + nextPoint.x) / 2,
      0,
      (currentPoint.z + nextPoint.z) / 2
    );

    // parentPosition을 기준으로 벽의 위치를 계산
    const point = center.clone().sub(parentPosition);
    const direction = this.getDirection(currentPoint, nextPoint);
    const offset = this.getOffset(direction, this.wallWidth);

    return point.add(offset);
  }

  // 두 점 사이의 각도 계산
  getAngle(p1, p2) {
    const direction = this.getDirection(p1, p2);
    return Math.atan2(direction.z, direction.x);
  }
}
