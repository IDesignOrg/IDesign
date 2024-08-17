import { floorMaterial } from "../../three";
import { THREE } from "../loader/three";
import { floorName } from "../objectConf/objectNames";
import { floorY } from "../objectConf/renderOrders";

export class D2Floor {
  constructor({ points, height, center }) {
    const shape = new Shape({ points });
    const geometry = new THREE.ShapeGeometry(shape);
    geometry.computeBoundingBox();
    const boundingBox = geometry.boundingBox;
    const offset = boundingBox.min;
    const range = boundingBox.max.clone().sub(offset);
    const uvAttribute = geometry.attributes.uv;

    for (let i = 0; i < uvAttribute.count; i++) {
      const x = (uvAttribute.getX(i) - offset.x) / range.x;
      const y = (uvAttribute.getY(i) - offset.y) / range.y;
      uvAttribute.setXY(i, x, y);
    }
    uvAttribute.needsUpdate = true;
    // Mesh 생성
    const material = floorMaterial;
    const mesh = new THREE.Mesh(geometry, material);
    mesh.name = floorName;
    mesh.renderOrder = 1;
    mesh.rotation.x = -Math.PI / 2;
    mesh.position.set(-center.x, floorY, -center.z);
    if (height) {
      mesh.position.y = height;
    }
    return mesh;
  }
}

export class Shape extends THREE.Shape {
  constructor({ points }) {
    const shape = super();
    shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
    for (let i = 1; i < points.length; i++) {
      shape.lineTo(points[i].x, -points[i].z);
    }
    shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

    return shape;
  }
}
