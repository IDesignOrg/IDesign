const modal = document.getElementById("couponModal");
const selectCouponBtn = document.getElementById("selectCouponBtn");
const closeBtn = document.getElementById("close-btn");
const modalBackground = document.getElementById("modal_bg");
const couponNo = document.getElementById("couponNo");
const table = document.getElementById("coupon-table");

const SetModalToggle = () => {
  modal.classList.toggle("hidden");
};

const onCouponClick = (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;

  // 버튼에 저장된 쿠폰 번호와 이름을 가져옵니다.
  const couponId = btn.getAttribute("data-coupon-id");
  const couponName = btn.getAttribute("data-coupon-name");

  // input 필드에 쿠폰 ID를 넣고, 화면에 표시될 값은 쿠폰 이름으로 설정
  couponNo.value = couponId;
  
  // 선택한 쿠폰 이름을 별도로 화면에 표시할 수 있는 요소가 있다면 업데이트
  document.getElementById("selectedCouponName").textContent = couponName;

  // 모달창을 닫습니다.
  SetModalToggle();
};



table.addEventListener("click", onCouponClick);
selectCouponBtn.addEventListener("click", SetModalToggle);
modalBackground.addEventListener("click", SetModalToggle);
closeBtn.addEventListener("click", SetModalToggle);
