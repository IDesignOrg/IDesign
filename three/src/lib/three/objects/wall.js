import { THREE } from "../../../three.js";
import { Shape } from "./floor.js";

export const wallHeight = 50;
const wallDepth = 5;
const colors = ["red", "blue", "black", "green"];

const wallpaper = "../../../../public/img/wallpaper2.jpg";
const texture = new THREE.TextureLoader().load(wallpaper);

export class D3Wall extends THREE.Group {
  constructor({ points }) {
    const walls = super();
    walls.name = "walls";
    const shape = new THREE.Shape();
    for (let i = 0; i < points.length; i++) {
      const wallGroup = super();

      const currentPoint = points[i];
      const nextPoint = points[(i + 1) % points.length];
      const angle = calculateAngle(currentPoint, nextPoint);
      const width = calculateDistance(currentPoint, nextPoint);

      // const geometry = new THREE.BoxGeometry(width, wallHeight, wallDepth);
      // const materials = [
      //   new THREE.MeshBasicMaterial({
      //     color: "red",
      //     // side: THREE.BackSide
      //   }), // 위
      //   new THREE.MeshBasicMaterial({
      //     color: "blue",
      //     // side: THREE.BackSide
      //   }), // 아래
      //   new THREE.MeshBasicMaterial({
      //     color: "green",
      //     // side: THREE.FrontSide
      //   }), // 좌
      //   new THREE.MeshBasicMaterial({
      //     color: "yellow",
      //     // side: THREE.FrontSide,
      //     // visible: false,
      //   }), // 우
      //   new THREE.MeshBasicMaterial({
      //     color: "pink",
      //     // side: THREE.FrontSide
      //   }), // 앞
      //   new THREE.MeshBasicMaterial({
      //     color: "black",
      //     // visible: false,
      //     // side: THREE.BackSide,
      //   }), // 뒤
      // ];
      // const wall = new THREE.Mesh(geometry, materials);
      // wall.rotation.y = -angle;
      // const halfWallDepth = wallDepth / 2;
      // const offsetX = halfWallDepth * Math.sin(angle);
      // const offsetZ = halfWallDepth * Math.cos(angle);
      // wall.position.set(
      //   (currentPoint.x + nextPoint.x) / 2 + offsetX,
      //   wallHeight / 2,
      //   (currentPoint.z + nextPoint.z) / 2 - offsetZ
      // );

      // //
      // //
      // //
      // //
      // //
      // // wallGroup.add(wall);
      // //
      // //
      // //
      // //
      // //
      // // const left = getNewCoordinates(currentPoint, angle, wallDepth);
      // // const right = getNewCoordinates(nextPoint, angle, wallDepth);

      // // const newPoints = [

      // // ]
      // // const new_points = [
      // //   left,
      // //   right,
      // //   {
      // //     ...currentPoint,
      // //   },
      // //   { ...nextPoint },
      // // ];

      //
      //
      //
      //
      //
      // const shape = new Shape({ points: new_points });
      // const geometry = new THREE.ShapeGeometry(shape);

      // const mesh = new THREE.Mesh(geometry, material);
      // mesh.position.set(
      //   (currentPoint.x + nextPoint.x) / 2,
      //   wallHeight / 2,
      //   (currentPoint.z + nextPoint.z) / 2
      // );
      // mesh.rotation.y = -angle;
      // mesh.name = "wall";
      // walls.add(mesh);

      const wallGeo = new THREE.PlaneGeometry(width, wallHeight);
      const material = new THREE.MeshBasicMaterial({
        // color: colors[i % points.length],
        map: texture,
        side: THREE.FrontSide,
      });

      // const example = new THREE.Mesh()
      const wall = new THREE.Mesh(wallGeo, material);
      wall.position.set(
        (currentPoint.x + nextPoint.x) / 2,
        wallHeight / 2,
        (currentPoint.z + nextPoint.z) / 2
      );
      wall.rotation.y = -angle;
      // wallGroup.add(cube);
      // walls.add(wallGroup);
      walls.add(wall);
    }

    walls.position.y = 2;
    return walls;
  }
}

function getNewCoordinates(currentPoint, angle, distance) {
  const angleOffset = Math.PI / 4; // 45도 각도
  const newX = currentPoint.x + Math.cos(angle + angleOffset) * distance;
  const newZ = currentPoint.z + Math.sin(angle + angleOffset) * distance;
  return { x: newX, z: newZ };
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

export const calculateAngle = (p1, p2) => {
  return Math.atan2(p2.z - p1.z, p2.x - p1.x);
};

const calculateDistance = (p1, p2) => {
  const dx = p2.x - p1.x;

  const dz = p2.z - p1.z;
  return Math.sqrt(dx * dx + dz * dz);
};
