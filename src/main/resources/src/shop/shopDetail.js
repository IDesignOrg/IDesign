// const modal = document.getElementById("myModal"),
//   starContainer = document.getElementById("stars-container"),
//   photosWrapper = document.getElementById("photos-wrapper");
// //   starContainers = document.querySelectorAll(".review-rating .stars");

// function onClickImage(e) {
//   //   console.log(e);
//   //   const modal = document.getElementById("myModal");
//   const modalImg = document.getElementById("img01");
//   const img = e.target.closest("img");
//   if (!img) return;
//   modal.style.display = "flex";
//   modalImg.src = img.src;
//   document.body.classList.add("modal-open");
// }

// function onCloseModal() {
//   modal.style.display = "none";
// }

// function init() {
//   const rating = parseFloat(starContainer.getAttribute("data-rating")); // 예: 3.5
//   const stars = document.querySelectorAll(".star");

//   for (let i = 0; i < rating; i++) {
//     const currentStar = stars[i];
//     let newClass = "filled";
//     if (rating - i === 0.5) {
//       newClass = "half-filled";
//     }
//     currentStar.classList.add(newClass);
//   }
// }

// init();

// photosWrapper.addEventListener("click", onClickImage);
// modal.addEventListener("click", onCloseModal);
