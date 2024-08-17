import { MILLPerWidth } from "../three.js";
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

const getPerpendicularVector = (dx, dz) => {
  const distance = 25;
  // 변의 방향 벡터 (dx, dz)에 수직인 벡터
  // x축 우세 시, z축 방향으로 이동
  // z축 우세 시, x축 방향으로 이동
  return Math.abs(dx) > Math.abs(dz)
    ? { x: 0, z: -Math.sign(dx) * distance }
    : { x: Math.sign(dz) * distance, z: 0 };
};

// 각 변에서 ±25만큼 떨어진 점 계산 함수
export const calculateOffsetPoints = (p1, p2, distance) => {
  const dx = p2.x - p1.x;
  const dz = p2.z - p1.z;

  // 방향 벡터에 수직인 벡터 계산
  const perpendicular = getPerpendicularVector(dx, dz);

  // 새로운 점 계산
  const offsetPoint1 = {
    x: p1.x + perpendicular.x,
    y: p1.y,
    z: p1.z + perpendicular.z,
  };

  const offsetPoint2 = {
    x: p2.x + perpendicular.x,
    y: p2.y,
    z: p2.z + perpendicular.z,
  };

  return [offsetPoint1, offsetPoint2];
};

export function caclulateIntersectionPoint(x1, y1, x2, y2, x3, y3, x4, y4) {
  // 선 1: (x1, y1)과 (x2, y2) 를 지나는 직선
  const A1 = y2 - y1;
  const B1 = x1 - x2;
  const C1 = A1 * x1 + B1 * y1;

  // 선 2: (x3, y3)과 (x4, y4) 를 지나는 직선
  const A2 = y4 - y3;
  const B2 = x3 - x4;
  const C2 = A2 * x3 + B2 * y3;

  // 두 직선의 계수로부터 교차점을 계산
  const determinant = A1 * B2 - A2 * B1;

  if (determinant === 0) {
    // 평행하거나 일치하는 경우 교차점이 없음
    return null;
  }

  const x = (B2 * C1 - B1 * C2) / determinant;
  const y = (A1 * C2 - A2 * C1) / determinant;

  return { x, y };
}
