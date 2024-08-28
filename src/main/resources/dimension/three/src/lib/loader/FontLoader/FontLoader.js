import { FontLoader } from "three/examples/jsm/loaders/FontLoader.js";
import { TextGeometry } from "three/addons/geometries/TextGeometry.js";

export let loadedFont = null;

const loadFont = () => {
  const loader = new FontLoader();
  return new Promise((resolve, reject) => {
    console.log("resolve!!");
    loader.load(
      "https://threejs.org/examples/fonts/helvetiker_regular.typeface.json",
      (font) => {
        loadedFont = font;
        resolve(font);
      },
      undefined,
      (err) => {
        reject(err);
      }
    );
  });
};
loadFont();

// // 폰트를 로드합니다.
// loadFont().then((font) => {
//   // 모든 Arrow 인스턴스를 여기에서 생성할 수 있습니다.
// });
