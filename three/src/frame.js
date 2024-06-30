import { IndexArray } from "./lib/indexArray";
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

    let isCreating = false; // is requestFrame running ?
    let shadowLine = {
      // 미리보기 좌표
      startX: 0,
      startY: 0,
      endX: 0,
      endY: 0,
    };

    const rectsArr = new IndexArray([
      [
        { x: 0, y: 0 },
        { x: 0, y: 500 },
        { x: 500, y: 500 },
        { x: 500, y: 0 },
      ],
      [
        { x: 0, y: 0 },
        { x: 0, y: -500 },
        { x: -500, y: -500 },
        { x: -500, y: 0 },
      ],
    ]);

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
      ctx.fillStyle = "red";
      rectsArr.forEach((points) => {
        ctx.beginPath();
        points.forEach((point, idx) => {
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

    const screenToCanvasCoords = (mouseX, mouseY) => {
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
      if (isCreating) {
        const startingWides = 1000;
        const { x, y } = screenToCanvasCoords(e.clientX, e.clientY);
        shadowLine = {
          startX: x,
          startY: y,
          endX: x + 10,
          endY: y + 10,
        };

        console.log(x, y, cameraOffset.x, cameraOffset.y);
        // console.log(cameraOffset.x, e.deltaX, zoomSensitivity);
        // console.log(cameraOffset.y - e.deltaY * zoomSensitivity);
        // console.log(
        //   x,
        //   y,
        //   canvas.width / 2 + cameraOffset.x,
        //   canvas.height / 2 + cameraOffset.y
        // );
        rectsArr.push([
          { x: x, y: y }, // start point
          { x: x + startingWides, y },
          { x: x + startingWides, y: y + startingWides }, // end point
          { x: x, y: y + startingWides },
        ]);
        return;
      }
      isDragging = true;
      lastMousePos.x = e.clientX;
      lastMousePos.y = e.clientY;
    };

    const onMouseMove = (e) => {
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
      isCreating = !isCreating;

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
    btns.addEventListener("click", onBtnClick);
    drawStart();
  });
};
