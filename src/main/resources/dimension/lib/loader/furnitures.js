import { GLTFLoader } from "./GLTFLoader";

const loader = new GLTFLoader();

export const createChair = () => {
  return new Promise((res, rej) => {
    loader.load(
      "/dimension/public/gltf/chair/chair.gltf",
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
