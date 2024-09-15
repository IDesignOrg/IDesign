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
  const couponId = e.target.getAttribute("th:data-coupon-id");
  const couponName = e.target.getAttribute("th:text");
  couponNo.value = couponName;
  SetModalToggle();
  //   const coupounId = e.target.getAttribute("data-coupon-name");
};

table.addEventListener("click", onCouponClick);
selectCouponBtn.addEventListener("click", SetModalToggle);
modalBackground.addEventListener("click", SetModalToggle);
closeBtn.addEventListener("click", SetModalToggle);
