// const savedObjects = ['room','desk','floor','circle']

import { MilliName, roomName } from "./lib/objectConf/objectNames";
import { furnitureObjects } from "./three";

export const saveFactory = (scene) => {
  const arr = [];
  console.log(scene);
  function dfs(object, parent) {
    if (
      object.name === roomName ||
      Object.keys(furnitureObjects).includes(object.name)
    ) {
      let children = [];
      if (object.children) {
        object.children.forEach((o) => {
          if (furnitureObjects.hasOwnProperty(o.name)) {
            children.push(o.uuid);
          }
        });
      }
      const obj = {
        type: object.name,
        parent,
        rotation: object.userData.rotation || 0,
        points: object.userData.points,
        oid: object.uuid,
        children: children.length === 0 ? null : children,
      };
      arr.push(obj);
    }
    if (object.name === roomName) {
      object.children.forEach((o) => dfs(o, object.uuid));
    }
  }

  scene.children.forEach((object) => dfs(object, null));

  return arr;
};
