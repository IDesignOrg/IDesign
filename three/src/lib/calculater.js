import { THREE } from "../three.js";

export const getCoordsFromVectex = (obj) => {
  const positionAttribute = obj.geometry.attributes.position;
  const coordinates = [];
  for (let i = 0; i < positionAttribute.count; i++) {
    const vertex = new THREE.Vector3();
    vertex.fromBufferAttribute(positionAttribute, i);
    coordinates.push({ x: vertex.x, y: 0, z: -vertex.y });
  }

  return coordinates;
};

export const calculateCenter = (points) => {
  let centerX = 0;
  let centerZ = 0;

  points.forEach((point) => {
    centerX += point.x;
    centerZ += point.z;
  });

  centerX /= points.length;
  centerZ /= points.length;

  return new THREE.Vector3(centerX, 0, centerZ);
};

export const getClickedCircleIndex = ({ background, floor }) => {
  let idx = null;
  const originPoints = getCoordsFromVectex(floor);
  const { point } = background;
  for (let i = 0; i < originPoints.length; i++) {
    if (
      Math.abs(originPoints[i].x - point.x) <= 5 &&
      Math.abs(originPoints[i].z - point.z) <= 5
    ) {
      idx = i;
      break;
    }
  }
  return idx;
};

export const getStraightLineZ = ({ originPoints, points, index }) => {
  const prevIndex = (index - 1 + originPoints.length) % originPoints.length;
  const nextIndex = (index + 1) % originPoints.length;
  const tolerance = 10;
  if (Math.abs(originPoints[prevIndex].z - points.z) <= tolerance) {
    return originPoints[prevIndex].z;
  }
  if (Math.abs(originPoints[nextIndex].z - points.z) <= tolerance) {
    return originPoints[nextIndex].z;
  }
  return points.z;
};

export const getStraightLineX = ({ originPoints, points, index }) => {
  const prevIndex = (index - 1 + originPoints.length) % originPoints.length;
  const nextIndex = (index + 1) % originPoints.length;
  const tolerance = 10;

  if (Math.abs(originPoints[prevIndex].x - points.x) <= tolerance) {
    return originPoints[prevIndex].x;
  }

  if (Math.abs(originPoints[nextIndex].x - points.x) <= tolerance) {
    return originPoints[nextIndex].x;
  }

  return points.x;
};