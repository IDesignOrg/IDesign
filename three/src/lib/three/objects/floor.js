import { THREE } from "../../../three.js";

export class D2Floor {
  constructor({ points, height }) {
    const shape = new Shape({ points });
    const geometry = new THREE.ShapeGeometry(shape);
    let material;
    material = new THREE.MeshLambertMaterial({
      side: height ? THREE.BackSide : THREE.FrontSide,
    });
    const mesh = new THREE.Mesh(geometry, material);
    mesh.name = "floor";
    mesh.renderOrder = 1;
    mesh.rotation.x = -Math.PI / 2;
    mesh.position.y = 2;
    if (height) {
      mesh.position.y = height;
    }
    return mesh;
  }
}

export class Shape extends THREE.Shape {
  constructor({ points }) {
    const shape = super();
    shape.moveTo(points[0].x, -points[0].z); // z축을 y축으로 사용하여 평면 도형 정의
    for (let i = 1; i < points.length; i++) {
      shape.lineTo(points[i].x, -points[i].z);
    }
    shape.lineTo(points[0].x, -points[0].z); // 마지막 점을 처음 점에 연결

    return shape;
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
