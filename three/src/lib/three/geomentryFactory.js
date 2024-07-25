import { THREE } from "../../three.js";
import { getCoordsFromVectex } from "../calculater.js";
// import { scene } from "../../main.js";
import { D2Room, D3Room } from "./objects/Room.js";
import { Circles } from "./objects/circles.js";

export const D3Shapes = ({ object, scene }) => {
  const room = new D3Room({ object });

  return room;
};

export const D2Shapes = ({ object, cameraZooom }) => {
  // 아마 room
  const floor = object.getObjectByName("floor");
  const points = getCoordsFromVectex(floor);
  const room = new D2Room({ points });

  return room;
};

export const create2DObject = ({ name, points, cameraZooom }) => {
  let room = new D2Room({ points, name });

  room.name = name;
  const circleGroup = new Circles({ points });
  circleGroup.name = "circleGroup";
  room.add(circleGroup);
  return room;
};
