import { MILLPerWidth } from "../main.js";
import { THREE } from "./loader/three.js";

function sum(arr) {
  return arr.reduce(
    (accumulator, currentValue) => accumulator + currentValue,
    0
  );
}

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

export function calculateArea(points) {
  // const multipliWithNextZ = points.reduce((acc, current) => {
  //   return acc + cu
  // })
  const multipliWithNextZ = points.map((point, idx, arr) => {
    return point.x.toFixed(2) * arr[(idx + 1) % arr.length].z.toFixed(2);
  });
  const multipliWithNextX = points.map((point, idx, arr) => {
    return point.z.toFixed(2) * arr[(idx + 1) % arr.length].x.toFixed(2);
  });

  const area = Math.floor(
    Math.floor(
      (sum(multipliWithNextX).toFixed(2) - sum(multipliWithNextZ).toFixed(2)) /
        2
    ) *
      MILLPerWidth *
      0.001
  );

  return Math.abs(area);
}

export function calculateCenter(points) {
  let sumX = 0,
    sumY = 0,
    sumZ = 0;
  points.forEach((point) => {
    sumX += point.x;
    sumY += point.y;
    sumZ += point.z;
  });
  const count = points.length;
  return {
    x: sumX / count,
    y: sumY / count,
    z: sumZ / count,
  };
}

export const getClickedCircleIndex = ({ background, floor, cg, object }) => {
  // console.log("ewqewqewqeq");
  const children = cg.children;
  const position = object.position;
  for (let i = 0; i < children.length; i++) {
    if (
      Math.abs(children[i].position.x - position.x) <= 5 &&
      Math.abs(children[i].position.z - position.z) <= 5
    ) {
      return i;
    }
  }
  return null;
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

export const calculateAngle = (p1, p2) => {
  return Math.atan2(p2.z - p1.z, p2.x - p1.x);
};

export const calculateDistance = (p1, p2) => {
  const dx = p2.x - p1.x;

  const dz = p2.z - p1.z;
  return Math.sqrt(dx * dx + dz * dz);
};
