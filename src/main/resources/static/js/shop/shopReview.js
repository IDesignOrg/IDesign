const imgUploadBtn = document.getElementById("description-image-upload");

imgUploadBtn.addEventListener("dragstart", function (event) {
  if (event.target.tagName === "IMG") {
    event.dataTransfer.setData("text/plain", event.target.src);
  }
});

imgUploadBtn.addEventListener("dragover", function (event) {
  event.preventDefault();
});

imgUploadBtn.addEventListener("drop", function (event) {
  event.preventDefault();
  const data = event.dataTransfer.getData("text/plain");
  const target = event.target;
  if (target.tagName === "IMG") {
    const draggedImg = document.querySelector(`img[src="${data}"]`);
    const targetSrc = target.src;
    target.src = draggedImg.src;
    draggedImg.src = targetSrc;
  }
});
