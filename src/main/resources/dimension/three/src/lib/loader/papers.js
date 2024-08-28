import { THREE } from "./three.js";

const ceilingPaper = "/dimension/public/img/ceiling.jpg";
const floorPaper = "/dimension/public/img/floor.jpg";
const wallpaper = "/dimension/public/img/wallpaper.jpeg";

const floorTexture = new THREE.TextureLoader().load(floorPaper);
const wallTexture = new THREE.TextureLoader().load(wallpaper);
const ceilingTexture = new THREE.TextureLoader().load(ceilingPaper);

export const wallMaterial = new THREE.MeshBasicMaterial({
  map: wallTexture,
});
export const ceilingMaterial = new THREE.MeshBasicMaterial({
  map: ceilingTexture,
  side: THREE.BackSide,
});

export const floorMaterial = new THREE.MeshBasicMaterial({
  map: floorTexture,
  side: THREE.FrontSide,
});
