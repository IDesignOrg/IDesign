import { THREE } from "../../../three.js";
import { ceilingColor } from "../objectConf/colors.js";
import { planeLength, planeWidth, wallHeight } from "../objectConf/length.js";
import { ceilingName } from "../objectConf/objectNames.js";
class Ceiling {
  constructor({
    x,
    y = wallHeight,
    z,
    width = planeWidth,
    length = planeLength,
    color = ceilingColor,
    rotationY,
  }) {
    const ceilingGeometry = new THREE.PlaneGeometry(width, length);
    const ceilingMaterial = new THREE.MeshBasicMaterial({
      color: color,
      side: THREE.BackSide,
    });
    const ceiling = new THREE.Mesh(ceilingGeometry, ceilingMaterial);
    ceiling.name = ceilingName;
    ceiling.rotation.x = Math.PI / 2;
    ceiling.rotation.y = rotationY;
    // ceiling.rotation.z = Math.PI / 4;
    ceiling.position.set(x, y, z);

    return ceiling;
  }
}

export { Ceiling };
