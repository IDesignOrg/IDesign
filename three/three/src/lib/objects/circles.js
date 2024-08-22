import { floorY } from "../../three";
import { THREE } from "../loader/three";
import { circleGroupName, circleName } from "../objectConf/objectNames";
import { circlesRenderOrder, roomY } from "../objectConf/renderOrders";

export class Circles extends THREE.Group {
  constructor({ points, center }) {
    super();
    this.name = circleGroupName;
    points.forEach((point) => {
      const geometry = new THREE.CircleGeometry(5, 32);
      const material = new THREE.MeshBasicMaterial({
        color: "green",
        side: THREE.DoubleSide,
      });
      const circle = new THREE.Mesh(geometry, material);
      circle.name = circleName;
      circle.position.set(point.x, roomY, point.z);
      circle.rotation.x = -Math.PI / 2;

      this.add(circle);
      this.rednerOrder = circlesRenderOrder;
    });
  }
}
