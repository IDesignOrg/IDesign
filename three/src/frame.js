export const startFrame = () => {
  const canvas = document.getElementById("canvas");
  const ctx = canvas.getContext("2d");
  const maxZoom = 5;
  const minZoom = 0.01;
  const scrollSensitivity = 5;
  const zoomSensitivity = 0.001;
  const cameraOffset = {
    x: window.innerWidth / 2,
    y: window.innerHeight / 2,
  };
  let cameraZoom = 0.1;
  let isDragging = false;
  let lastZoom = 0.1;
  ctx.fillStyle = "red";
  ctx.fillRect(-500, -500, 500, 500);

  const drawStart = () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    ctx.translate(window.innerWidth / 2, window.innerHeight / 2);
    ctx.scale(cameraZoom, cameraZoom);
    ctx.translate(
      -window.innerWidth / 2 + cameraOffset.x,
      -window.innerHeight / 2 + cameraOffset.y
    );
    ctx.fillStyle = "red";
    ctx.fillRect(-500, -500, 500, 500);

    requestAnimationFrame(drawStart);
  };

  const onScreenMove = (e) => {
    e.preventDefault();
    if (e.ctrlKey) {
      adjustZoom(-e.deltaY * zoomSensitivity, null);
    }

    cameraOffset.x = cameraOffset.x - e.deltaX * scrollSensitivity;
    cameraOffset.y = cameraOffset.y - e.deltaY * scrollSensitivity;
  };

  const adjustZoom = (zoomAmount, zoomFactor) => {
    if (isDragging) {
      if (zoomFactor) {
        cameraZoom += zoomAmount;
      } else if (zoomFactor) {
        cameraZoom = zoomFactor * lastZoom;
      }

      cameraZoom = Math.min(cameraZoom, maxZoom);
      cameraZoom = Math.max(cameraZoom, minZoom);
    }
  };

  canvas.addEventListener("wheel", onScreenMove);
  drawStart();
};
