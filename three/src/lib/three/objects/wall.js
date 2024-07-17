import { THREE } from "../../../three.js";

const wallHeight = 50;
const wallDepth = 5;
const colors = ["red", "blue", "black", "green"];

const wallpaper = "../../../../public/img/wallpaper2.jpg";
const texture = new THREE.TextureLoader().load(wallpaper);

export class D3Wall extends THREE.Group {
  constructor({ points }) {
    const walls = super();
    walls.name = "walls";
    console.log(points);
    for (let i = 0; i < points.length; i++) {
      const wallGroup = super();

      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];

      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint);
      const wall = new THREE.PlaneGeometry(width, wallHeight);
      const material = new THREE.MeshBasicMaterial({
        // color: colors[i % points.length],
        map: texture,
        side: THREE.FrontSide,
      });
      const cube = new THREE.Mesh(wall, material);
      cube.position.set(
        (currentPoint.x + nextPoint.x) / 2,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2
      );
      cube.rotation.y = -angle;
      wallGroup.add(cube);

      // const wallSidesMaterial = new THREE.MeshBasicMaterial({
      //   color: "rgb(55,55,55)",
      //   side: THREE.DoubleSide,
      // });
      // const wallSideGeometry = new THREE.PlaneGeometry(wallDepth, wallHeight);
      // const wallLeft = new THREE.Mesh(wallSideGeometry, wallSidesMaterial);
      // wallLeft.position.set(currentPoint.x, wallHeight / 2, nextPoint.z);
      // wallLeft.rotation.y = Math.PI / 1;
      // wallGroup.add(wallLeft);
      walls.add(wallGroup);
    }

    walls.position.y = 2;
    return walls;
  }
}

export class D2Wall extends THREE.Group {
  constructor({ points }) {
    const walls = super();
    walls.name = "walls";
    const material = new THREE.LineBasicMaterial({
      color: 0x0000ff,
      linewidth: 5,
    });
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    const line = new THREE.Line(geometry, material);
    line.name = "walls";
    return line;
  }
}

const calculateAngle = (p1, p2) => {
  return Math.atan2(p2.z - p1.z, p2.x - p1.x);
};

const calculateDistance = (p1, p2) => {
  const dx = p2.x - p1.x;

  const dz = p2.z - p1.z;
  return Math.sqrt(dx * dx + dz * dz);
};
