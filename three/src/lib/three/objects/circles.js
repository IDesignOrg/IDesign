import { floorY } from "../../../main.js";
import { THREE } from "../../../three.js";

export class Circles {
  constructor({ points, center }) {
    const circleGroup = new THREE.Group();
    circleGroup.name = "circleGroup";
    points.forEach((point) => {
      const geometry = new THREE.CircleGeometry(2.5, 32);
      const material = new THREE.MeshBasicMaterial({
        color: "red",
        side: THREE.DoubleSide,
      });
      const circle = new THREE.Mesh(geometry, material);
      circle.name = "circle";
      circle.position.set(point.x, floorY + 1, point.z);
      circle.rotation.x = -Math.PI / 2;

      circleGroup.add(circle);
      circleGroup.position.set(-center.x, center.y, -center.z);
    });

    return circleGroup;
  }
}
