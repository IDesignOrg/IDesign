import axios from "axios";

const getCouponBtn = document.getElementById("get-coupon");

const getCoupon = async () => {
  const couponNo = document.getElementById("couponNo").value;

  if (!couponNo) return alert("공습경보"); // 에러처리

  try {
    const data = await axios.post("/post/issue_coupon", {
      couponNo,
    });

    if (data.status === "fail") return alert("공습경보");
    alert("쿠폰 발급이 성공했음");
  } catch (e) {
    return alert("공습경보", e);
  }
};

getCouponBtn.addEventListener("click", getCoupon);
