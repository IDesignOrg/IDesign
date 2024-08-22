import { THREE } from "../loader/three.js";
import { Shape } from "./floor.js";

export class ShadowLines extends THREE.Line {
  constructor({ points }) {
    const material = new THREE.LineBasicMaterial({
      color: "red",
    });
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    super(geometry, material);
  }
}

// export class ShadowLines extends Group {
//   constructor({ points }) {
//     super();

//     for (let i = 0; i < points.length; i++){
//       const current = points[i];
//       const next = points[(i + 1) % points.length];

//     }
//   }
// }
