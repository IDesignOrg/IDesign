import * as THREE from "../three.module.js";
import { roomName } from "../objectConf/objectNames.js";
import {
  ceilingHeight,
  planeLength,
  planeWidth,
} from "../objectConf/length.js";
import { Floor } from "./floor.js";
import { Ceiling } from "./ceiling.js";
import { Wall } from "./wall.js";

class Room {
  constructor({
    x = 0,
    y = 0,
    z = 0,
    width = planeWidth,
    length = planeLength,
  }) {
    const room = new THREE.Group();
    room.name = roomName;

    const floor = new Floor({ x, y, z, width, length });
    room.add(floor);
    const ceil = new Ceiling({ x, z });
    room.add(ceil);
    const westwall = new Wall({
      x,
      z: z + length / 2,
      y: ceilingHeight / 2 + y,
      width,
      length: ceilingHeight,
      color: "red",
    });
    const eastwall = new Wall({
      x,
      z: z - length / 2,
      y: ceilingHeight / 2 + y,
      width,
      length: ceilingHeight,
      rotationY: -Math.PI / 1,
      color: "blue",
    });
    const southwall = new Wall({
      x: x - width / 2,
      z: z,
      y: ceilingHeight / 2 + y,
      width: length,
      length: ceilingHeight,
      rotationY: -Math.PI / 2,
      color: "green",
    });
    const northwall = new Wall({
      x: x + width / 2,
      z: z,
      y: ceilingHeight / 2 + y,
      width: length,
      length: ceilingHeight,
      rotationY: Math.PI / 2,
      color: "yellow",
    });

    room.add(westwall);
    room.add(eastwall);
    room.add(southwall);
    room.add(northwall);

    return room;
  }
}

export { Room };
