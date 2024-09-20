const data = {
  status: "success",
  data: {
    project_id: "1725094276775",
    dataEntities: {
      "b4fb623d-0db6-4c47-89a5-a1be0d87867e": {
        oid: "b4fb623d-0db6-4c47-89a5-a1be0d87867e",
        type: "room",
        rotation: 0.0,
        parent: null,
        points: [
          {
            x: -169.859871414789,
            y: 1.01,
            z: -267.9402507802365,
          },
          {
            x: 255.33748355587934,
            y: 1.01,
            z: -267.9402507802365,
          },
          {
            x: 255.33748355587934,
            y: 1.01,
            z: 240.5432496761527,
          },
          {
            x: -169.859871414789,
            y: 1.01,
            z: 240.5432496761527,
          },
        ],
        children: [],
      },
    },
    projectSrc: {
      title: "테스트1",
      src: "테스트1입니다.",
    },
  },
};

export const getDummy = () => {
  return new Promise((res) => {
    setTimeout(() => {
      res({
        status: "success",
        data,
      });
    }, 500);
  });
};
