import * as THREE from "../three.module";
import { Floor } from "./floor";

class InitObjects {
  constructor() {
    return {
      initCube: this.createInitCube(),
      initFloor: this.createInitFloor(),
      axeshelper: this.createAxesHelper(),
    };
  }

  createAxesHelper = () => {
    return new THREE.AxesHelper(1000);
  };
  createInitCube = () => {
    const geometry = new THREE.BoxGeometry(1000, 1000, 1000);
    const material = new THREE.MeshBasicMaterial({ color: "white" });
    const cube = new THREE.Mesh(geometry, material);
    cube.position.set(0, 0, 0);
    return cube;
  };

  createInitFloor = () => {
    return new Floor({});
    // const a = new Floor();
    // console.log(a);
  };
}

export { InitObjects };
