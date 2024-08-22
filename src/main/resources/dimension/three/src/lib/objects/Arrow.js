// import { FontLoader } from "../loader/FontLoader/FontLoader.js";
// import { loadedFont } from "../loader/FontLoader/FontLoader";
import { MILLPerWidth } from "../../three";
import { THREE } from "../loader/three";
import { MilliName } from "../objectConf/objectNames";
import { arrowsRenderOrder, arrowsY } from "../objectConf/renderOrders";
import { Text } from "./text";
// import { TextGeometry } from "three/addons/geometries/TextGeometry.js";
export class Arrow {
  constructor({ width, angle, position, cameraZoom }) {
    const group = new THREE.Group();
    group.name = MilliName;

    const text = new Text({
      text: `${(width / MILLPerWidth).toFixed(2)}mm`,
      angle,
      cameraZoom,
      position,
    });
    const shape = new THREE.Shape();
    const arrowWidth = 2;
    const arrowHeight = 2;
    const shaftWidth = width - arrowHeight * 5;
    const shaftHeight = 1;

    // 좌우 화살표 그리기
    shape.moveTo(-shaftWidth / 2, shaftHeight / 2);
    shape.lineTo(-shaftWidth / 2, arrowHeight);
    shape.lineTo(-shaftWidth / 2 - arrowWidth, 0);
    shape.lineTo(-shaftWidth / 2, -arrowHeight);
    shape.lineTo(-shaftWidth / 2, -shaftHeight / 2);

    shape.lineTo(shaftWidth / 2, -shaftHeight / 2);
    shape.lineTo(shaftWidth / 2, -arrowHeight);
    shape.lineTo(shaftWidth / 2 + arrowWidth, 0);
    shape.lineTo(shaftWidth / 2, arrowHeight);
    shape.lineTo(shaftWidth / 2, shaftHeight / 2);

    shape.lineTo(-shaftWidth / 2, shaftHeight / 2);

    const geometry = new THREE.ShapeGeometry(shape);
    const material = new THREE.MeshBasicMaterial({
      color: "rgb(134, 134, 134)",
      side: THREE.DoubleSide,
    });
    const mesh = new THREE.Mesh(geometry, material);
    group.position.set(position.x, 0, position.z);
    group.rotation.x = -Math.PI / 2;
    group.rotation.z = -angle;

    group.add(text);
    group.add(mesh);

    return group;
  }
}