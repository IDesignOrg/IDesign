import { THREE } from "../../../three";
// import { calculateCenter, getCoordsFromVectex } from "../../calculater";

export const RotationControllerY = 3;

export class RotationController extends THREE.Group {
  constructor() {
    super();
    this.name = "rotationController";
    this.createBackground();
  }

  createBackground = () => {
    const innerRadius = 10;
    const outerRadius = 20;
    const thetaSegments = 30;
    const phiSegments = 8;
    const thetaStart = 0;
    const thetaLength = Math.PI * 2;
    const geometry = new THREE.RingGeometry(
      innerRadius,
      outerRadius,
      thetaSegments,
      phiSegments,
      thetaStart,
      thetaLength
    );
    const material = new THREE.MeshBasicMaterial({
      color: "blue",
      side: THREE.DoubleSide,
    });
    const mesh = new THREE.Mesh(geometry, material);
    mesh.rotation.x = -Math.PI / 2;
    mesh.name = "controllerBackground";
    this.add(mesh);
  };
}
