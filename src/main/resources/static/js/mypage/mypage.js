function openTab(evt, tabName) {
	var i, tabcontent, tablinks;
	tabcontent = document.getElementsByClassName("tabcontent");
	for (i = 0; i < tabcontent.length; i++) {
		tabcontent[i].style.display = "none";
	}
	tablinks = document.getElementsByClassName("tablinks");
	for (i = 0; i < tablinks.length; i++) {
		tablinks[i].className = tablinks[i].className.replace(" active", "");
	}
	document.getElementById(tabName).style.display = "block";
	evt.currentTarget.className += " active";

	if (tabName === "myCoupons") {
		loadCoupons();
	}
	if (tabName === "orderedHistory") {
		loadOrdered();
	}
}

//주문내역 로드
function loadOrdered() {
	$.ajax({
		type: "GET",
		url: "/my/ordered",
		success: function(orderedDTO) {
			console.log("orderdDTO: ", orderedDTO);
			if (orderedDTO == null || orderedDTO.orderedEntities.length === 0) {
				thereIsNoOrdered();
			} else {
				displayOrdered(orderedDTO);
			}
		},
		error: function(xhr, status, error) {
			alert(error);
		},
	});
}

//쿠폰 로드
function loadCoupons() {
	$.ajax({
		type: "GET",
		url: "/my/coupons",
		success: function(coupons) {
			if (coupons == null || coupons.length === 0) {
				thereIsNoCoupon();
			} else {
				displayCoupons(coupons);
			}
		},
		error: function(xhr, status, error) {
			alert(error);
		},
	});
}

function thereIsNoCoupon() {
	var couponListDiv = document.getElementById("couponList");
	couponListDiv.innerHTML = "가지고 있는 쿠폰이 없습니다.";
}
function thereIsNoOrdered() {
	var orderedListDiv = document.getElementById("orderedList");
	orderedListDiv.innerHTML = "주문 내역이 비어있습니다.";
}

function displayOrdered(orderedDTO) {
	var orderedListDiv = document.getElementById("orderedList");
	orderedListDiv.innerHTML = "";

	// 주문 내역과 가게 정보를 각각 매칭하여 표시
	orderedDTO.orderedEntities.forEach(function(order) {
		// 해당 주문의 가게 정보 찾기
		var shop = orderedDTO.shopEntities.find(
			(shop) => shop.shopNo === order.shopNo
		);

		var orderItem = document.createElement("div");
		orderItem.classList.add("order-item");

		var buttonHtml = "";

		if (order.orderedState === "환불") {
			buttonHtml = `<button onclick="showRefundReasonModal('${order.merchantUId}')">환불 사유 보기</button>`;
		} else {
			buttonHtml = `<button onclick="cancelPay(this)">환불하기</button>`;
		}
		console.log("order", order);
		orderItem.innerHTML = `
		            <div class="order-details">
		                <h3>주문 번호: ${order.orderedNo}</h3>
		                <div class="shop-details">
		                    <h4>상품 정보</h4>
		                    <p><strong>상품명:</strong> ${shop ? shop.shopTitle : "정보 없음"
			}</p>
		                    <p><strong>카테고리:</strong> ${shop ? shop.shopCategory : "정보 없음"
			}</p>
		                    <p><strong>가격:</strong> ${shop ? shop.shopPrice : "정보 없음"
			} 원</p>
		                    <p><strong>수량:</strong> ${order ? order.quantity : "정보 없음"
			} 개</p>
		                    <p><strong>상품 사진:</strong></p>
		                    <img src="${shop ? shop.shopMainPhoto : "정보 없음"
			}" alt="${shop ? shop.shopTitle : "상품 이미지"}">
		                </div>
		                <div class="order-status">
		                    <p><strong>주문 상태:</strong> ${order.orderedState}</p>
		                    <p><strong>배송 상태:</strong> ${order.shipmentState}</p>
							<a
							             th:href="@{/shopReview/{shopNo}(shopNo=${order.shopNo})}"
							             class="product-link"
							           >
		                </div>
		                <hr>
						<input type="hidden" id="merchantUId" value="${order.merchantUId}"/>
						${buttonHtml}
		            </div>
		        `;

		orderedListDiv.appendChild(orderItem);
	});
}
function cancelPay(button) {
	const merchantUId = button.parentElement.querySelector('input[type="hidden"]').value;
	document.getElementById("merchantUId").value = merchantUId;
	document.getElementById("refundModal").style.display = "block";
}

// 모달 관련 코드
document.addEventListener("DOMContentLoaded", function() {
	var refundModal = document.getElementById("refundModal");

	var refundReasonModal = document.getElementById("refundReasonModal");

	var refundReasonInput = document.getElementById("refundReasonInput");
	var refundReasonDisplay = document.getElementById(
		"refundReasonDisplay"
	);
	var refundUserDisplay = document.getElementById("refundUser");

	var confirmRefundBtn = document.getElementById("confirmRefundBtn");
	var selectedMerchantUId = "";

	// 모달 닫기 기능
	var closeModalButtons = document.querySelectorAll(".close");
	closeModalButtons.forEach(function(btn) {
		btn.addEventListener("click", function() {
			refundModal.style.display = "none";
			refundReasonModal.style.display = "none";
			refundReasonInput.value = ""; // 입력 필드 초기화
			refundReasonDisplay.value = ""; // 환불 사유 초기화
			refundUserDisplay.textContent = ""; // 환불 신청자 초기화
		});
	});

	// 환불 사유 보기 모달 열기
	window.showRefundReasonModal = function(merchantUId) {
		fetch(`/refund/reason?merchantUId=${merchantUId}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error("환불 정보를 불러오는 데 실패했습니다.");
				}
				return response.json();
			})
			.then((data) => {
				refundReasonDisplay.value =
					data.refundReason || "등록된 환불 사유가 없습니다.";
				refundUserDisplay.textContent = data.refundUser || "알 수 없음";
				refundReasonModal.style.display = "block";
			})
			.catch((error) => {
				console.error("Error fetching refund reason:", error);
				alert("환불 사유를 가져오는 중 오류가 발생했습니다.");
			});
	};

	// 환불 요청 처리
	confirmRefundBtn.addEventListener("click", function() {
		var merchantUId = document.getElementById("merchantUId").value;
		console.log(
			'document.getElementById("merchantUId")',
			document.getElementById("merchantUId")
		);

		if (!merchantUId || merchantUId === "") return alert("공습경보");

		console.log(merchantUId);
		var refundReason = refundReasonInput.value.trim();
		if (refundReason === "") {
			alert("환불 사유를 입력하세요.");
			return;
		}
		refundOrder(merchantUId, refundReason);
	});

	function refundOrder(merchantUId, refundReason) {
		fetch("/refund/paymentToAdmin", {
			method: "POST",
			body: new URLSearchParams({
				merchantUId: merchantUId,
				refundReason: refundReason,
			}),
		})
			.then((response) => {
				if (response.ok) {
					alert(
						"환불 처리가 완료되었습니다. 카드 승인 취소는 영업일 기준 최대 3-5일 소요됩니다. 취소 지연시, 해당 카드사로 문의하세요."
					);
					location.reload(); // 페이지 새로고침으로 목록 갱신
				} else {
					return response.text().then((text) => {
						throw new Error(text);
					});
				}
			})
			.catch((error) => {
				console.error("Error refunding order:", error);
				alert(
					"주문 환불 중 오류가 발생했습니다. 서버 응답: " + error.message
				);
			});
	}
});

/*예전 환불 로직
  function cancelPay(button) {
	  var merchantUId = $(button).siblings('input[type="hidden"]').data('merchant-uid');
	  console.log("merchant-uid: ", merchantUId);
	  $.ajax({
		  url: '/refund/payment',
		  type: 'POST',
		  data: {
			  merchantUId: merchantUId
		  },
		  success: function (res) {
			  alert("환불 처리가 완료되었습니다. 카드 승인 취소는 영업일 기준 최대 3-5일 소요됩니다. 취소 지연시, 해당 카드사로 문의하세요.");
			  window.location.href = "/";
		  }
	  });
  }
  */
function displayCoupons(coupons) {
	var couponListDiv = document.getElementById("couponList");
	couponListDiv.innerHTML = "";

	coupons.forEach(function(coupon) {
		var couponItem = document.createElement("div");
		couponItem.classList.add("coupon-item");
		couponItem.innerHTML = `
					<p><strong>쿠폰 이름:</strong> ${coupon.couponName}</p>
					<p><strong>할인율:</strong> ${coupon.couponDiscount}%</p>
					<p><strong>사용 기한:</strong> ${coupon.couponStartAt} ~ ${coupon.couponEndAt}</p>
					<hr>
				`;

		couponListDiv.appendChild(couponItem);
	});
}

function updateUserInfo() {
	window.location.href = "/mypage/mypageUpdate";
}
/*비밓번호 변*/
function changePassword() {
	// mypageUpdatePassword.html로 이동
	window.location.href = "/mypage/mypageUpdatePassword";
}
/* 유저 비활성화 */
function deleteUserInfo() {
	// 유저 ID 가져오기 (폼 필드에서 가져옴)
	var userUNo = document.getElementById("UNo").value;

	// Ajax 요청으로 서버에 POST 요청을 보냄
	fetch("/deactivateUserin", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
		},
		body: new URLSearchParams({
			userUNo: userUNo,
		}),
	})
		.then((response) => {
			if (response.redirected) {
				// 리다이렉션이 발생하면 해당 URL로 이동
				window.location.href = response.url;
			} else if (response.ok) {
				return response.text(); // 서버로부터 메시지 받기
			} else {
				throw new Error("유저 비활성화에 실패했습니다.");
			}
		})
		.then((result) => {
			if (result) {
				alert(result); // 서버 응답 메시지를 알림으로 표시 (비활성화 실패 시)
			}
		})
		.catch((error) => {
			console.error("Error:", error);
			alert("서버 오류로 인해 유저 비활성화에 실패했습니다.");
		});
}

document.getElementById("userInfo").style.display = "block"; // 기본으로 첫 번째 탭을 보여줍니다.