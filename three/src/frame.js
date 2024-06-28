document.addEventListener("DOMContentLoaded", (event) => {
  const canvas = document.getElementById("canvas");
  const ctx = canvas.getContext("2d");
  const btns = document.getElementById("frame-btns");

  const maxZoom = 5;
  const minZoom = 0.01;
  const zoomSensitivity = 0.005;
  const cameraOffset = {
    x: window.innerWidth / 2,
    y: window.innerHeight / 2,
  };
  let cameraZoom = 1;
  let reqId = null;
  let isReqRunning = true;
  let isDragging = false;
  let dragStart = { x: 0, y: 0 };
  let lastMousePos = { x: 0, y: 0 };

  const drawStart = () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    // Clear canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Save the current state
    ctx.save();

    // Apply transformations
    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.scale(cameraZoom, cameraZoom);
    ctx.translate(
      -canvas.width / 2 + cameraOffset.x,
      -canvas.height / 2 + cameraOffset.y
    );

    // Draw the red square
    ctx.fillStyle = "red";
    ctx.fillRect(-250, -250, 500, 500);
    // Restore the original state
    ctx.restore();
    if (isReqRunning) {
      reqId = requestAnimationFrame(drawStart);
    }
  };

  const onScreenMove = (e) => {
    e.preventDefault();

    // 줌인/아웃
    adjustZoom(-e.deltaY * zoomSensitivity, e.clientX, e.clientY);
  };

  const adjustZoom = (zoomAmount, mouseX, mouseY) => {
    // 줌 계산
    const newZoom = Math.min(
      maxZoom,
      Math.max(minZoom, cameraZoom + zoomAmount)
    );
    const zoomFactor = newZoom / cameraZoom;

    const canvasRect = canvas.getBoundingClientRect();
    const offsetX = (mouseX - canvasRect.left - canvas.width / 2) / cameraZoom;
    const offsetY = (mouseY - canvasRect.top - canvas.height / 2) / cameraZoom;

    cameraOffset.x -= offsetX * (zoomFactor - 1);
    cameraOffset.y -= offsetY * (zoomFactor - 1);

    cameraZoom = newZoom;
  };

  const onMouseDown = (e) => {
    // 무한 캔버스 드래그 앤 드랍
    isDragging = true;
    dragStart.x = e.clientX;
    dragStart.y = e.clientY;
    lastMousePos.x = e.clientX;
    lastMousePos.y = e.clientY;
  };

  const onMouseMove = (e) => {
    // 무한 캔버스 드래그 앤 드랍
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
    // 드래그 앤 드랍 end
    isDragging = false;
  };

  const createRoom = () => {};

  const onBtnClick = (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    const { name } = btn;
    if (!reqId) {
      reqId = requestAnimationFrame(drawStart);
      return;
    }
    cancelAnimationFrame(reqId);

    switch (name) {
      case "room":
        createRoom();
        break;

      default:
        break;
    }
  };

  canvas.addEventListener("wheel", onScreenMove);
  canvas.addEventListener("mousedown", onMouseDown);
  canvas.addEventListener("mousemove", onMouseMove);
  canvas.addEventListener("mouseup", onMouseUp);
  btns.addEventListener("click", onBtnClick);
  drawStart();
});
