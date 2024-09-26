document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM fully loaded and parsed");
  var modal = document.getElementById("myModal");
  modal.style.display = "none"; // 페이지 로드 후 모달 강제로 숨기기
  document.body.classList.remove("modal-open"); // 모달로 인한 스크롤 방지 제거

  // 별점 채우기
  const starContainers = document.querySelectorAll(".review-rating .stars");
  console.log(starContainers);
  starContainers.forEach(function (starContainer) {
    const rating = parseFloat(starContainer.getAttribute("data-rating")); // 예: 3.5
    const stars = starContainer.querySelectorAll(".star");

    stars.forEach((star, index) => {
      if (index < Math.floor(rating)) {
        star.classList.add("filled");
      }
      if (index === Math.floor(rating) && rating % 1 !== 0) {
        star.classList.add("half-filled");
      }
    });
  });
});

function openModal(element) {
  var modal = document.getElementById("myModal");
  var modalImg = document.getElementById("img01");
  modal.style.display = "flex"; // 모달을 열 때 flex로 변경

  modalImg.src = element.src;
  console.log("openModal called with element: ", element);
  // 모달이 열릴 때 배경 스크롤 막기
  document.body.classList.add("modal-open");
}

function closeModal(event) {
  var modal = document.getElementById("myModal");
  if (event.target === modal || event.target.classList.contains("close")) {
    modal.style.display = "none"; // 모달을 숨김

    // 모달이 닫힐 때 배경 스크롤 허용
    document.body.classList.remove("modal-open");
  }
}

function deleteReview(reviewId) {
  if (confirm("정말로 이 리뷰를 삭제하시겠습니까?")) {
    $.ajax({
      type: "DELETE",
      url: "/shopReview/" + reviewId,
      success: function (response) {
        alert(response);
        location.reload();
      },
      error: function (xhr, status, error) {
        alert("리뷰 삭제에 실패했습니다.");
      },
    });
  }
}

$(document).ready(function () {
  $("#cartForm").on("submit", function (event) {
    event.preventDefault();

    $.ajax({
      type: "POST",
      url: "/auth/cart",
      data: $(this).serialize(),
      success: function (res) {
        if (confirm(res)) {
          window.location.href = "/my/cart";
        }
      },
      error: function (xhr, status, error) {
        if (xhr.status == 401) {
          var msg = xhr.responseText;
          if (confirm(msg)) {
            window.location.href = "/signin";
          }
        }
      },
    });
  });
});
