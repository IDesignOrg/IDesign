import * as THREE from "../three.module.js";

export class Circles {
  constructor({ points }) {
    const circleGroup = new THREE.Group();
    circleGroup.visible = false;
    circleGroup.name = "circleGroup";
    points.forEach((point) => {
      const geometry = new THREE.CircleGeometry(2.5, 32);
      const material = new THREE.MeshBasicMaterial({
        color: "red",
        side: THREE.DoubleSide,
      });
      const circle = new THREE.Mesh(geometry, material);
      circle.name = "circle";
      circle.position.set(point.x, 1, point.z);
      circle.rotation.x = -Math.PI / 2;
      circleGroup.add(circle);
    });

    return circleGroup;
  }
}
