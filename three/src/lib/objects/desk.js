import * as THREE from "../../three.module.js";

class Desk {
  constructor({
    x,
    z,
    object,
    width = 4,
    height = 1.5,
    depth = 2,
    color = 0x8b4513,
  }) {
    this.delete = this.deleteObj();
    const legWidth = 0.1;
    const deskGroup = new THREE.Group();
    deskGroup.name = "Desk";

    // 상판
    const tableTopGeometry = new THREE.BoxGeometry(width, legWidth, depth);
    const tableTopMaterial = new THREE.MeshBasicMaterial({ color });
    const tableTop = new THREE.Mesh(tableTopGeometry, tableTopMaterial);
    tableTop.position.y = height;

    // 다리
    const legX = width / 2 - legWidth / 2;
    const legY = height / 2;
    const legZ = depth / 2 - legWidth;
    console.log(legZ);
    const legGeometry = new THREE.BoxGeometry(legWidth, height, legWidth);
    const legMaterial = new THREE.MeshBasicMaterial({ color });
    const leg1 = new THREE.Mesh(legGeometry, legMaterial);
    const leg2 = new THREE.Mesh(legGeometry, legMaterial);
    const leg3 = new THREE.Mesh(legGeometry, legMaterial);
    const leg4 = new THREE.Mesh(legGeometry, legMaterial);

    leg1.position.set(-legX, legY, -legZ);
    leg2.position.set(legX, legY, -legZ);
    leg3.position.set(-legX, legY, legZ);
    leg4.position.set(legX, legY, legZ);

    deskGroup.add(tableTop);
    deskGroup.add(leg1);
    deskGroup.add(leg2);
    deskGroup.add(leg3);
    deskGroup.add(leg4);

    // object 내에 생성위치 계산
    const bbox = new THREE.Box3().setFromObject(object);
    const minX = bbox.min.x + width / 2;
    const maxX = bbox.max.x - width / 2;
    const minZ = bbox.min.z + width / 2;
    const maxZ = bbox.max.z - width / 2;
    deskGroup.position.set(
      Math.max(minX, Math.min(maxX, x)),
      0,
      Math.max(minZ, Math.min(maxZ, z))
    );

    return deskGroup;
  }

  deleteObj = () => {
    console.log("zz");
  };
}

export { Desk };
