export const throttle = (func, delay) => {
  let timer;

  return () => {
    if (!timer) {
      timer = setTimeout(() => {
        timer = null;
        func();
      }, delay);
    }
  };
};
