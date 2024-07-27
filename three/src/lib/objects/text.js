import { THREE } from "../loader/three";
import { TextGeometry } from "three/addons/geometries/TextGeometry.js";
import { loadedFont } from "../loader/FontLoader/FontLoader";

export class Text extends THREE.Mesh {
  constructor({ text }) {
    if (loadedFont) {
      const textGeometry = new TextGeometry(text, {
        font: loadedFont,
        size: 10,
        depth: 0.2,
        curveSegments: 12,
        bevelEnabled: true,
        bevelThickness: 0.03,
        bevelSize: 0.02,
        bevelOffset: 0,
        bevelSegments: 5,
      });

      const textMaterial = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
      super(textGeometry, textMaterial);
      this.name = "text";
    } else {
      console.error("Font not loaded yet");
    }
  }
}
