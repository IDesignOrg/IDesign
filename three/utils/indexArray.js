export function IndexArray(arr) {
  // for get arr[-idx]
  return new Proxy(arr, {
    get(target, property, receiver) {
      if (property < 0) {
        return target.at(property);
      } else {
        return target[property];
      }
    },

    set(target, property, receiver) {
      if (property < 0) {
        target[target.length + property] = receiver;
        return true;
      } else {
        target[property] = receiver;
        return true;
      }
    },
  });
}
