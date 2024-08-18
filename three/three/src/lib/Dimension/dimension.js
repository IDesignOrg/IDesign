import { D3Room } from "../objects/Room";

export const create3DRoom = (D2Room) => {
  //   return;
  const newRoom = new D3Room({ object: D2Room });
  return newRoom;
};
