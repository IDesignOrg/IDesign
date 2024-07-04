import * as THREE from "../three.module.js";

export class D2Floor {
  constructor({ points }) {
    const shape = new THREE.Shape();
    // z축의 기준으로 반대방향
    // 왜인지는 모름...
    shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
    for (let i = 1; i < points.length; i++) {
      shape.lineTo(points[i].x, -points[i].z);
    }
    shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

    const geometry = new THREE.ShapeGeometry(shape);
    const material = new THREE.MeshBasicMaterial({
      color: "green",
      side: THREE.DoubleSide,
    });

    return new THREE.Mesh(geometry, material);
  }
}

// import { floorName } from "../objectConf/objectNames.js";
// import { floorColors } from "../objectConf/colors.js";
// import { planeLength, planeWidth } from "../objectConf/length.js";

// class Floor {
//   constructor({
//     x = 0,
//     y = 0,
//     z = 0,
//     width = planeWidth,
//     length = planeLength,
//     floorColor = floorColors,
//   }) {
//     const floorGeometry = new THREE.PlaneGeometry(width, length);
//     const floorMaterial = new THREE.MeshBasicMaterial({
//       color: floorColor,
//       side: THREE.DoubleSide,
//     });
//     const floor = new THREE.Mesh(floorGeometry, floorMaterial);
//     floor.name = floorName;
//     floor.rotation.x = Math.PI / 2;
//     floor.position.set(x, y, z);

//     return floor;
//   }
// }

// export { Floor };
