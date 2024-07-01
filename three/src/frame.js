import { IndexArray } from "./lib/indexArray";
import { v4 as uuidv4 } from "uuid";
export const startFrame = () => {
  document.addEventListener("DOMContentLoaded", (event) => {
    const canvas = document.getElementById("canvas");
    const ctx = canvas.getContext("2d");
    const btns = document.getElementById("frame-btns");

    const maxZoom = 5;
    const minZoom = 0.05;
    const zoomSensitivity = 0.005;
    const cameraOffset = {
      x: window.innerWidth / 2,
      y: window.innerHeight / 2,
    };
    let cameraZoom = 1; // init camera zoom
    let reqId = null;
    let isDragging = false; // is inf canvas drag and drop
    let lastMousePos = { x: 0, y: 0 };

    // let isCreating = false; // is requestFrame running ?
    let isCreating = {
      isBtnClick: false,
      isDragging: false, // making an object ?
    };
    let shadowLine = {
      // 미리보기 좌표
      startX: 0,
      startY: 0,
      endX: 0,
      endY: 0,
    };

    const rectsObjects = {
      [uuidv4()]: {
        name: "initFloor",
        coords: [
          { x: 0, y: 0 },
          { x: 0, y: 500 },
          { x: 500, y: 500 },
          { x: 500, y: 0 },
        ],
      },
    };

    const drawStart = () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;

      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.save();
      ctx.translate(canvas.width / 2, canvas.height / 2);
      ctx.scale(cameraZoom, cameraZoom);
      ctx.translate(
        -canvas.width / 2 + cameraOffset.x,
        -canvas.height / 2 + cameraOffset.y
      );
      ctx.fillStyle = "yellowgreen";

      Object.keys(rectsObjects).forEach((key) => {
        ctx.beginPath();
        const obj = rectsObjects[key];
        console.log(obj);
        ctx.fillStyle = obj.color;
        obj.coords.forEach((point, idx) => {
          if (idx === 0) {
            ctx.moveTo(point.x, point.y);
          } else {
            ctx.lineTo(point.x, point.y);
          }
        });
        ctx.closePath();
        ctx.strokeStyle = "red";
        ctx.lineWidth = 2;
        ctx.stroke();
        ctx.fill();
      });
      ctx.restore();
      reqId = requestAnimationFrame(drawStart);
    };

    const getCanvasCoordinates = (mouseX, mouseY) => {
      const canvasRect = canvas.getBoundingClientRect();
      const x = (mouseX - canvasRect.left - canvas.width / 2) / cameraZoom;
      const y = (mouseY - canvasRect.top - canvas.height / 2) / cameraZoom;
      return { x, y };
    };

    const onZoom = (e) => {
      e.preventDefault();
      adjustZoom(-e.deltaY * zoomSensitivity, e.clientX, e.clientY);
    };

    const adjustZoom = (zoomAmount, mouseX, mouseY) => {
      const newZoom = Math.min(
        maxZoom,
        Math.max(minZoom, cameraZoom + zoomAmount)
      );
      const zoomFactor = newZoom / cameraZoom;

      const canvasRect = canvas.getBoundingClientRect();
      const offsetX =
        (mouseX - canvasRect.left - canvas.width / 2) / cameraZoom;
      const offsetY =
        (mouseY - canvasRect.top - canvas.height / 2) / cameraZoom;

      cameraOffset.x -= offsetX * (zoomFactor - 1);
      cameraOffset.y -= offsetY * (zoomFactor - 1);

      cameraZoom = newZoom;
    };

    const onMouseDown = (e) => {
      if (isCreating.isBtnClick) {
        if (isCreating.isDragging) {
          //end of making an object
          rectsObjects[uuidv4()] = {
            ...rectsObjects["shadow"],
            color: "yellowgreen",
            name: "floor",
          };
          delete rectsObjects["shadow"];
          isCreating = {
            isBtnClick: false,
            isDragging: false, // making an object ?
          };
          return;
        }

        const startingWides = 10;
        const { x, y } = getCanvasCoordinates(e.clientX, e.clientY);
        shadowLine = {
          startX: x,
          startY: y,
          endX: x + 10,
          endY: y + 10,
        };
        rectsObjects["shadow"] = {
          name: "shadow",
          coords: [
            { x: x, y: y }, // start point
            { x: x + startingWides, y },
            { x: x + startingWides, y: y + startingWides }, // end point
            { x: x, y: y + startingWides },
          ],
          color: "rgba(215, 63, 63,0.5)",
        };

        isCreating = {
          ...isCreating,
          isDragging: true,
        };
        return;
      }
      isDragging = true;
      lastMousePos.x = e.clientX;
      lastMousePos.y = e.clientY;
    };

    const onMouseMove = (e) => {
      if (isCreating.isBtnClick && isCreating.isDragging) {
        const { x, y } = getCanvasCoordinates(e.clientX, e.clientY);
        const starginCoords = rectsObjects["shadow"].coords[0];

        rectsObjects["shadow"] = {
          ...rectsObjects["shadow"],
          coords: rectsObjects["shadow"].coords.map((coord, idx) => {
            if (idx === 0) {
              return coord;
            } else {
              switch (idx) {
                case 1:
                  return { x, y: starginCoords.y };

                case 2:
                  return { x, y };

                case 3:
                  return { x: starginCoords.x, y };
              }
            }
          }),
        };

        return;
      }
      if (isDragging) {
        const dx = e.clientX - lastMousePos.x;
        const dy = e.clientY - lastMousePos.y;
        cameraOffset.x += dx / cameraZoom;
        cameraOffset.y += dy / cameraZoom;
        lastMousePos.x = e.clientX;
        lastMousePos.y = e.clientY;
      }
    };

    const onMouseUp = () => {
      isDragging = false;
    };

    const createRoom = () => {};

    const onBtnClick = (e) => {
      const btn = e.target.closest("button");
      if (!btn) return;
      const { name } = btn;
      isCreating = {
        ...isCreating,
        isBtnClick: true,
      };

      switch (name) {
        case "room":
          createRoom();
          break;

        default:
          break;
      }
    };

    canvas.addEventListener("wheel", onZoom);
    canvas.addEventListener("mousedown", onMouseDown);
    canvas.addEventListener("mousemove", onMouseMove);
    canvas.addEventListener("mouseup", onMouseUp);
    window.addEventListener("keydown", (e) => {
      const { keyCode } = e;
      if (keyCode === 27) {
        isCreating = {
          isBtnClick: false,
          isDragging: false, // making an object ?
        };
      }
    });
    btns.addEventListener("click", onBtnClick);
    drawStart();
  });
};
