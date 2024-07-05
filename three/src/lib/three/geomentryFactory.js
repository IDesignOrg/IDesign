import { THREE } from "../../three.js";
// import { scene } from "../../main.js";
import { D2Room, D3Room } from "./objects/Room.js";
import { Circles } from "./objects/circles.js";

export const D3Shapes = ({ object, scene }) => {
  // object is "room" Group... maybe..
  const room = new D3Room({ object });

  return room;
};

export const create2DObject = ({ name, points, objectName }) => {
  let room = new D2Room({ points });
  room.name = name;
  const circleGroup = new Circles({ points });
  circleGroup.name = "circleGroup";
  room.add(circleGroup);
  return room;
};
