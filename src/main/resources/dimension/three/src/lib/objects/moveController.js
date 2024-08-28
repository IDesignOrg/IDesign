import { THREE } from "../loader/three";
import {
  moveConrollerName,
  moveControllerChildrenName,
} from "../objectConf/objectNames";
import { moveControllerY } from "../objectConf/renderOrders";

export class MoveController extends THREE.Group {
  constructor() {
    super();
    this.isDragging = false;
    this.visible = false;
    this.name = moveConrollerName;

    this.createCircle();
    this.createArrow();
    this.renderOrder = 3;
    this.position.y = moveControllerY;
  }

  setPosition = (center) => {
    this.position.set(center.x, moveControllerY, center.z);
  };

  createArrow = () => {
    for (let i = 0; i < 2; i++) {
      const shape = new THREE.Shape();
      const arrowWidth = 4;
      const arrowHeight = 4;
      const shaftWidth = 20;
      const shaftHeight = 3;
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
        color: "black",
        side: THREE.DoubleSide,
      });
      const mesh = new THREE.Mesh(geometry, material);
      mesh.name = moveControllerChildrenName;
      mesh.rotation.x = -Math.PI / 2;
      if (i === 1) {
        mesh.rotation.z = Math.PI / 2;
      }
      // mesh.renderOrder = 1;
      mesh.position.y = 0.1;
      this.add(mesh);
    }
  };

  createCircle = () => {
    const geometry = new THREE.CircleGeometry(20, 32);
    const material = new THREE.MeshBasicMaterial({ color: "red" });
    const circle = new THREE.Mesh(geometry, material);
    circle.rotation.x = -Math.PI / 2;
    circle.position.y = 0;
    // circle.renderOrder = 0;
    circle.name = moveControllerChildrenName;
    this.add(circle);
  };

  onMouseDown = () => {
    this.isDragging = true;
  };

  onMouseMove = ({ room, points, rotationController }) => {
    rotationController.position.x = points.x;
    rotationController.position.z = points.z;
    room.setPosition(points);
    this.position.x = points.x;
    this.position.z = points.z;
  };

  onMouseUp = () => {
    this.isDragging = false;
  };
}
