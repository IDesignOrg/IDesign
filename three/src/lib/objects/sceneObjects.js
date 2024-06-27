import * as THREE from "../three.module";
import { Room } from "./Room";
import { groundName, wrapperName } from "../objectConf/objectNames";

class InitObjects {
  constructor() {
    return {
      initRoom: new Room({}),
      axesHelper: this.createAxesHelper(),
    };
  }

  createAxesHelper = () => {
    return new THREE.AxesHelper(1000);
  };
  createGround = () => {
    const initGround = new Room({
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
    // return new Floor({});
    // const a = new Floor();
    // console.log(a);
  };
}

export { InitObjects };
