import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";

const loader = new GLTFLoader();

export const createChair = () => {
  return new Promise((res, rej) => {
    loader.load(
      "gltf/chair/chair.gltf",
      (gltf) => {
        res(gltf.scene);
      },
      undefined,
      (error) => {
        rej(error);
      }
    );
  });
};

export const loadFurnitures = Promise.allSettled([createChair()]);
