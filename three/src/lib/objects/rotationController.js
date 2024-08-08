import { maxZoom, minZoom } from "../../main";
import { THREE } from "../loader/three";

export const RotationControllerY = 3;

export class RotationController extends THREE.Group {
  constructor({ cameraZoom = 250 }) {
    super();
    this.name = "rotationController";
    this.isDragging = false;
    this.visible = false;
    this.init(cameraZoom);
  }

  init = (cameraZoom) => {
    const innerRadius = 30;
    const outerRadius = 20;
    const thetaSegments = 30;
    const phiSegments = 8;
    const thetaStart = 1;
    const thetaLength = Math.PI * 2;

    // controllerBackground
    const RingGeometry = new THREE.RingGeometry(
      innerRadius,
      outerRadius,
      thetaSegments,
      phiSegments,
      thetaStart,
      thetaLength
    );
    const ringMaterial = new THREE.MeshBasicMaterial({
      color: "blue",
      side: THREE.DoubleSide,
      transparent: true,
      opacity: 0.5,
      depthWrite: false,
      depthTest: false,
    });
    const ring = new THREE.Mesh(RingGeometry, ringMaterial);
    ring.name = "controllerBackground";
    ring.rotation.x = -Math.PI / 2;
    ring.renderOrder = 1;
    // this.setScale({ object: ring, cameraZoom });
    this.add(ring);

    // controllerRing
    const thetaLengthRinged = Math.PI / 3;
    const RingedGeometry = new THREE.RingGeometry(
      innerRadius,
      outerRadius,
      thetaSegments,
      phiSegments,
      thetaStart,
      thetaLengthRinged
    );
    const ringedMaterial = new THREE.MeshBasicMaterial({
      color: "red",
      side: THREE.DoubleSide,
      depthWrite: false,
      depthTest: false,
    });
    const controllerRing = new THREE.Mesh(RingedGeometry, ringedMaterial);
    controllerRing.name = "controllerRing";
    controllerRing.rotation.x = -Math.PI / 2;
    controllerRing.renderOrder = 2;
    // this.setScale({ object: controllerRing, cameraZoom });
    this.add(controllerRing);
  };

  onMouseUp = () => {
    this.isDragging = false;
  };

  onMouseDown = () => {
    this.isDragging = true;
  };

  onMouseMove = ({ points, room }) => {
    const dx = points.x - this.position.x;
    const dz = points.z - this.position.z;

    // 각도 계산 (atan2 사용)
    const angle = Math.atan2(dz, dx) + Math.PI / 2; // rotation.x가 -Math.PI / 2이기 때문에 보정
    if (this.isDragging) {
      this.rotation({ angle, room });
      // return;
    }
    const controllerRing = this.getObjectByName("controllerRing");

    // 마우스 위치 (points)에서 ring의 중심까지의 벡터 계산

    // controllerRing의 rotation.z 업데이트
    controllerRing.rotation.z = -angle;
    document.body.style.cursor = "pointer";
  };
  setPosition = (center) => {
    this.position.set(center.x, center.y, center.z);
  };

  rotation = ({ angle, room }) => {
    room.userData.rotation = angle;
    room.rotation.y = angle;
    // room.remove(room.getObjectByName("area"));
  };

  setScale = ({ object, scaler }) => {
    object.scale.set(scaler, scaler, scaler);
  };
}
