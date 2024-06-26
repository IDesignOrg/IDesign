import * as THREE from "../three.module";
import { Floor } from "./floor";
import { groundName, wrapperName } from "./objectNames";

class InitObjects {
  constructor() {
    return {
      initGround: this.createGround(),
      initFloor: this.createInitFloor(),
      axeshelper: this.createAxesHelper(),
    };
  }

  createAxesHelper = () => {
    return new THREE.AxesHelper(1000);
  };
  createGround = () => {
    const initGround = new Floor({
      x: 0,
      y: -1,
      z: 0,
      width: 1000,
      length: 1000,
      floorColor: "white",
    });
    initGround.name = groundName;
    return initGround;
  };

  createInitFloor = () => {
    return new Floor({});
    // const a = new Floor();
    // console.log(a);
  };
}

export { InitObjects };
