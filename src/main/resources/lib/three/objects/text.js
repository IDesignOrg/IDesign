import { THREE } from "../loader/three";
import { Text as TroikaText } from "troika-three-text";
import { distanceTextY } from "../objectConf/renderOrders";

export class Text extends TroikaText {
  constructor({
    text,
    angle,
    position = new THREE.Vector3(0, 0, 0),
    color = "red",
  }) {
    super();
    this.text = text;
    this.position.set(position.x, distanceTextY, position.z);
    this.rotation.x = -Math.PI / 2;
    this.rotation.z = -angle;
    this.fontSize = 15;
    this.fontStyle = "italic";
    this.color = color;
    this.anchorX = "center";
    this.anchorY = "middle";
    this.sync();
  }
}
