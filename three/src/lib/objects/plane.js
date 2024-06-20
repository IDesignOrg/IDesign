class Plane {
  constructor({ x, y, width = 20, length = 20, color = 0xcccccc }) {
    const planeGeometry = new THREE.PlaneGeometry(CNT, CNT);
    const planeMaterial = new THREE.MeshBasicMaterial({
      color: 0xcccccc,
      side: THREE.DoubleSide,
    });
    const plane = new THREE.Mesh(planeGeometry, planeMaterial);
    plane.name = "plane";
  }
}
