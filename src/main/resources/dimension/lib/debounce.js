// export const debounce = (func, delay) => {
//   let timer;

//   return () => {
//     if (timer) {
//       clearTimeout(timer);
//     }

//     timer = setTimeout(() => {
//       func();
//     }, delay);
//   };
// };

export function debounce(func, delay) {
  let timer;
  return function () {
    const args = arguments;
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(this, args);
    }, delay);
  };
}
