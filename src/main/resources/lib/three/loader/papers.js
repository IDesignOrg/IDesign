import { THREE } from "./three.js";

const ceilingPaper = "/public/img/ceiling.jpg";
const floorPaper = "/public/img/floor.jpg";
const wallpaper = "/public/img/wallpaper.jpeg";

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
