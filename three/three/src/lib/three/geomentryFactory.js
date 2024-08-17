import { D2Room, D3Room } from "../objects/Room";
import { Circles } from "../objects/circles";

export const D3Shapes = ({ object, scene }) => {
  console.log(object);
  const room = new D3Room({ object });

  return room;
};

export const D2Shapes = ({ object, cameraZoom }) => {
  // 아마 room
  const points = object.userData.points;
  const room = new D2Room({ points });
  room.createRoom({ cameraZoom });
  room.userData = {
    ...object.userData,
  };

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
