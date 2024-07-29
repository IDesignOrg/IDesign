import { THREE } from "../loader/three";
import { TextGeometry } from "three/addons/geometries/TextGeometry.js";
import { loadedFont } from "../loader/FontLoader/FontLoader";

export class Text extends THREE.Mesh {
  constructor({ text, angle, cameraZoom, position }) {
    if (loadedFont) {
      const offset = 20;
      const textGeometry = new TextGeometry(text, {
        font: loadedFont,
        size: 10,
        depth: 0,
        curveSegments: 12,
        bevelEnabled: true,
        bevelThickness: 0.03,
        bevelSize: 0.02,
        bevelOffset: 0,
        bevelSegments: 5,
      });
      //   textGeometry.computeBoundingSphere();
      textGeometry.computeBoundingBox();
      const boundingBox = textGeometry.boundingBox;
      const textMaterial = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
      super(textGeometry, textMaterial);
      this.name = "text";

      // Center the text geometry
      if (boundingBox) {
        const textWidth = boundingBox.max.x - boundingBox.min.x;
        const textHeight = boundingBox.max.y - boundingBox.min.y;
        this.position.set(-textWidth / 2, 0, -textHeight / 2);
      }
    } else {
      console.error("Font not loaded yet");
    }
  }
}
