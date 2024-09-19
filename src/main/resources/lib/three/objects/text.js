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

// export class Text extends THREE.Mesh {
//   constructor({ text, angle, cameraZoom, position }) {
//     if (loadedFont) {
//       const offset = 20;
//       const textGeometry = new TextGeometry(text, {
//         font: loadedFont,
//         size: 10,
//         depth: 0,
//         curveSegments: 12,
//         bevelEnabled: true,
//         bevelThickness: 0.03,
//         bevelSize: 0.02,
//         bevelOffset: 0,
//         bevelSegments: 5,
//       });
//       textGeometry.computeBoundingBox();
//       const boundingBox = textGeometry.boundingBox;
//       const textMaterial = new THREE.MeshBasicMaterial({ color: "black" });
//       super(textGeometry, textMaterial);
//       this.name = "text";
//       this.userData = {
//         ...this.userData,
//         oid: uuid(),
//       };
//       if (boundingBox) {
//         const textWidth = boundingBox.max.x - boundingBox.min.x;
//         const textHeight = boundingBox.max.y - boundingBox.min.y;
//         this.position.set(-textWidth / 2, 0, -textHeight / 2);
//       }
//     } else {
//       console.error("Font not loaded yet");
//     }
//   }

//   updateText(text) {
//     if (loadedFont) {
//       const textGeometry = new TextGeometry(text, {
//         font: loadedFont,
//         size: 10,
//         depth: 0,
//         curveSegments: 12,
//         bevelEnabled: true,
//         bevelThickness: 0.03,
//         bevelSize: 0.02,
//         bevelOffset: 0,
//         bevelSegments: 5,
//       });

//       textGeometry.computeBoundingBox();
//       const boundingBox = textGeometry.boundingBox;

//       // 기존 지오메트리와 재질을 제거하고 새로운 것으로 교체
//       this.geometry.dispose(); // 메모리 관리를 위해 기존 지오메트리를 해제
//       this.geometry = textGeometry;

//       if (boundingBox) {
//         const textWidth = boundingBox.max.x - boundingBox.min.x;
//         const textHeight = boundingBox.max.y - boundingBox.min.y;
//         this.position.set(-textWidth / 2, 0, -textHeight / 2);
//       }
//     } else {
//       console.error("Font not loaded yet");
//     }
//   }
// }
