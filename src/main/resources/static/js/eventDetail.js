document.addEventListener("DOMContentLoaded", function() {
  const getCouponBtn = document.getElementById("get-coupon");

  // 버튼이 제대로 선택되었는지 확인
  console.log("getCouponBtn: ", getCouponBtn);

  getCouponBtn.addEventListener("click", function() {
    console.log("쿠폰 받기 버튼 클릭됨"); // 버튼 클릭 시 로그 남기기
    var couponNo = document.getElementById("couponNo").value; // couponNo 가져오기
    console.log("couponNo는: ", couponNo);
    
    $.ajax({
      type: "POST",
      url: "/api/coupon/" + couponNo,
      contentType: "application/json",
      data: JSON.stringify({ couponNo: couponNo }), // couponNo를 JSON으로 보냄
      success: function (res) {
        alert(res);
      },
      error: function (xhr, status, error) {
        if (xhr.status === 401) {
          alert("로그인이 필요합니다.");
          // 로그인 페이지로 리다이렉트
          window.location.href = "/signin"; // 로그인 페이지 경로
        } else {
          alert(xhr.responseText);
        }
      },
    });
  });
});
