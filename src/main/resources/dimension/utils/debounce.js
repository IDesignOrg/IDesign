export const debounce = (func, delay) => {
  let timer;

  return () => {
    if (timer) {
      clearTimeout(timer);
    }

    timer = setTimeout(() => {
      func();
    }, delay);
  };
};
