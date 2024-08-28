import { GLTFLoader } from "/dimension/three/src/lib/loader/GLTFLoader";

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

// //fbx를 로더해보자
// export const createDesk = () => {
//   const fbxURL = "/gltf/desk/desk2.fbx";
//   return new Promise((res) => {
//     fbxLoader.load(fbxURL, function (object) {
//       object.scale.set(0.5, 0.5, 0.5);
//       res(object);
//     });
//   });
// };
