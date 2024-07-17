import { GLTFLoader } from "../three/GLTFLoader";
import { FBXLoader } from "three/examples/jsm/loaders/FBXLoader";
const loader = new GLTFLoader();
const fbxLoader = new FBXLoader(); // 인스턴스 생성해주고

export const chairCreator = () => {
  return new Promise((res, rej) => {
    loader.load(
      "./gltf/chair/chair.gltf",
      (gltf) => {
        console.log(gltf);
        res(gltf.scene);
      },
      undefined,
      (error) => {
        rej(error);
      }
    );
  });
};

//fbx를 로더해보자
export const createDesk = () => {
  const fbxURL = "/gltf/desk/desk2.fbx";
  return new Promise((res) => {
    fbxLoader.load(fbxURL, function (object) {
      object.scale.set(0.5, 0.5, 0.5);
      res(object);
    });
  });
};
