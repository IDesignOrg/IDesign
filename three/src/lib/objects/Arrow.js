// import { FontLoader } from "../loader/FontLoader/FontLoader.js";
// import { loadedFont } from "../loader/FontLoader/FontLoader";
import { THREE } from "../loader/three";
import { Text } from "./text";
// import { TextGeometry } from "three/addons/geometries/TextGeometry.js";
export class Arrow {
  constructor({ width, angle, position }) {
    const group = new THREE.Group();
    const shape = new THREE.Shape();
    const arrowWidth = 5;
    const arrowHeight = 10;
    const shaftWidth = width - arrowHeight * 2;
    const shaftHeight = 5;

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
      color: 0x00ff00,
      side: THREE.DoubleSide,
    });
    const mesh = new THREE.Mesh(geometry, material);
    group.position.set(position.x, 10, position.z);
    group.rotation.x = -Math.PI / 2;
    group.rotation.z = -angle;

    const text = new Text({ text: `${width}mm` });

    // let measure = new THREE.Vector3();
    // let box = text.getSize(measure);
    // console.log(text);
    group.add(text);
    group.add(mesh);

    return group;
  }
}
