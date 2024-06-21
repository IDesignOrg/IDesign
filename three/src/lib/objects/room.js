import * as THREE from "../three.module.js";
import { ceilingName, floorName, wallName } from "./objectNames.js";
import { floorColors } from "./colors.js";
class Plane {
  constructor({
    x = 0,
    y = 0,
    z = 0,
    width = 20,
    length = 20,
    height = 10,
    floorColor = floorColors,
    wallColor = 0x9acd32, // yellowgreen 색상 (RGB(154,205,50))
    ceilingColor = 0x0000ff, // blue 색상 (RGB(0,0,255))
  }) {
    const group = new THREE.Group();
    group.name = "room";
    // 바닥 생성
    const floorGeometry = new THREE.PlaneGeometry(width, length);
    const floorMaterial = new THREE.MeshBasicMaterial({
      color: floorColor,
      side: THREE.DoubleSide,
    });
    const floor = new THREE.Mesh(floorGeometry, floorMaterial);
    floor.name = floorName;
    floor.rotation.x = -Math.PI / 2;
    floor.position.set(x, y, z);
    group.add(floor);

    // 벽 생성 함수
    const createWall = (width, height, color, position, rotation) => {
      const wallGeometry = new THREE.PlaneGeometry(width, height);
      const wallMaterial = new THREE.MeshBasicMaterial({
        color,
        side: THREE.DoubleSide,
      });
      const wall = new THREE.Mesh(wallGeometry, wallMaterial);
      wall.name = wallName;
      wall.position.copy(position);
      wall.rotation.copy(rotation);
      return wall;
    };

    // 동쪽 벽 (z + length / 2)
    group.add(
      createWall(
        width,
        height,
        wallColor,
        new THREE.Vector3(x, y + height / 2, z + length / 2),
        new THREE.Euler(0, 0, 0)
      )
    );
    // 서쪽 벽 (z - length / 2)
    group.add(
      createWall(
        width,
        height,
        wallColor,
        new THREE.Vector3(x, y + height / 2, z - length / 2),
        new THREE.Euler(0, Math.PI, 0)
      )
    );
    // 남쪽 벽 (x - width / 2)
    group.add(
      createWall(
        length,
        height,
        wallColor,
        new THREE.Vector3(x - width / 2, y + height / 2, z),
        new THREE.Euler(0, -Math.PI / 2, 0)
      )
    );
    // 북쪽 벽 (x + width / 2)
    group.add(
      createWall(
        length,
        height,
        wallColor,
        new THREE.Vector3(x + width / 2, y + height / 2, z),
        new THREE.Euler(0, Math.PI / 2, 0)
      )
    );

    // 천장
    const ceilingGeometry = new THREE.PlaneGeometry(width, length);
    const ceilingMaterial = new THREE.MeshBasicMaterial({
      color: ceilingColor,
      side: THREE.DoubleSide,
    });
    const ceiling = new THREE.Mesh(ceilingGeometry, ceilingMaterial);
    ceiling.name = ceilingName;
    ceiling.rotation.x = Math.PI / 2;
    ceiling.position.set(x, y + height, z);
    group.add(ceiling);

    return group;
  }
}

export { Plane };
