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
  });
}
