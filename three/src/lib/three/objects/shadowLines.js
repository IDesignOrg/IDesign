import { THREE } from "../../../three.js";

export class ShadowLines extends THREE.Line {
  constructor({ points }) {
    const material = new THREE.LineBasicMaterial({
      color: "red",
      linewidth: 4,
    });
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    super(geometry, material);
  }
}
