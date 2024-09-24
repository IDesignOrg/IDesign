
			function goToShop() {
				window.location.href = "/auth/shopList";
			}

			//장바구니 선택 삭제
			function removeItem(button) {
				var CNo = button.closest('.cart-item').querySelector('#cart-no').value;
				console.log('CNo의 값은', CNo);
				$.ajax({
					url: '/delete/cart',
					type: 'DELETE',
					data: {
						CNo: CNo
					},
					success: function (res) {
						alert('삭제가 완료되었습니다.');
						window.location.href = '/my/cart'; // 리다이렉트
					},
					error: function (xhr, status, error) {
						console.log("에러 발생: ", error);
					}
				});
			}

			var discountedAmount = 0;
			var couponNo = 0;
			$(document).ready(function () {
				applyCoupon();

				$('.orderBtn').on("click", function () {
					var cartSize = $('#cart-size').val();
					var buyerName = $('#recipient-name').val();
					var phone = $('#phone').val();
					var email = $('#email').val();
					var address = $('#sample4_roadAddress').val() + " " + $('#sample4_detailAddress').val();
					var postcode = $('#sample4_postcode').val();
					var merchantUId = `payment-${Math.random().toString(36).substr(2, 9)}`; // 대체 UUID
					var message = $('#message').val();

					var finalPayText = $('#finalPay').text();
					var price = parseInt(finalPayText.replace(/[^\d]/g, ''), 10);
					var amount = Math.ceil(discountedAmount);
					console.log("amount: ", amount);

					var firstProductTitle = $('.product-title').first().text();
					cartSize = parseInt(cartSize);
					var shopTitle = firstProductTitle;
					var firstProductTitle = $('.product-title').first().text();
					cartSize = parseInt(cartSize);
					var shopTitle = firstProductTitle;

					if (cartSize > 1) {
						shopTitle += " 외 " + (cartSize - 1) + "건";
					}

					var IMP = window.IMP;
					IMP.init("imp64382832");
					if (cartSize > 1) {
						shopTitle += " 외 " + (cartSize - 1) + "건";
					}

					var IMP = window.IMP;
					IMP.init("imp64382832");


					// 날짜 형식 변환 함수
					function formatDate(dateStr) {
						var date = new Date(dateStr);
						var year = date.getFullYear();
						var month = ("0" + (date.getMonth() + 1)).slice(-2);
						var day = ("0" + date.getDate()).slice(-2);
						return `${year}-${month}-${day}`;
					}

					// 사전 검증
					$.ajax({
						url: '/payment/prepare',
						type: 'POST',
						data: {
							merchant_uid: merchantUId,
							amount: price,
							validation: amount // 이 부분은 검증할 금액을 서버로 전달합니다.
						},
						success: function (response) {
							// 사전 검증 성공 후 결제 요청 진행
							IMP.request_pay({
								pg: "html5_inicis",
								pay_method: "card",
								merchant_uid: merchantUId,
								name: shopTitle,
								amount: price, // 검증된 금액을 사용
								buyer_email: email,
								buyer_name: buyerName,
								buyer_tel: phone,
								buyer_addr: address,
								buyer_postcode: postcode
							}, function (res) {
								if (res.success) {
									console.log("결제 성공: ", res);
									alert("결제가 완료되었습니다.");

									$.when(
										$.ajax({
											url: '/save/payment',
											type: 'POST',
											contentType: 'application/json',
											data: JSON.stringify({
												merchantUId: res.merchant_uid,
												cardName: res.card_name,
												name: shopTitle,
												paidAmount: res.paid_amount,
												payMethod: res.pay_method,
												paidAt: formatDate(res.paid_at),
												status: res.status,
												success: res.success
											})
										}),
										$.ajax({
											url: '/save/shipment',
											type: 'POST',
											contentType: 'application/json',
											data: JSON.stringify({
												buyerName: res.buyer_name,
												buyerEmail: res.buyer_email,
												buyerAddr: res.buyer_addr,
												buyerPostcode: res.buyer_postcode,
												buyerTel: res.buyer_tel,
												message: message
											})
										})
									).done(function (paymentRes, shipmentRes) {
										$.when(
											// 주문 내역 저장
											$.ajax({
												url: '/save/ordered',
												type: 'POST',
												data: {
													price: discountedAmount
												}
											}),
											$.ajax({
												url: '/payment/info',
												type: 'POST',
												contentType: 'application/json',
												data: JSON.stringify({
													paymentInfo: paymentRes[0],
													shipmentInfo: shipmentRes[0]
												})
											}),
											$.ajax({
												// 쿠폰 처리: 쓴 쿠폰 확인은 쓴 날짜로 함
												url: '/coupon/clear',
												type: 'PATCH',
												data: {
													couponNo: couponNo
												}
											}),
											$.ajax({
												// 장바구니 전부 삭제
												url: '/delete/all/cart',
												type: 'DELETE'
											})
										).done(function (res0, res1, res2, res3) {
											window.location.href = "/payment/info";
										}).fail(function (jqXHR, textStatus, errorThrown) {
											// 실패 시
											console.log("요청 실패: ", textStatus, errorThrown);
										});
									}).fail(function (jqXHR, textStatus, errorThrown) {
										console.log("결제 성공 후 요청 실패: ", textStatus, errorThrown);
									});
								} else {
									console.log("결제 실패: ", res);
									alert("결제에 실패했습니다. 다시 시도해주세요.");
								}
							});
						},
						error: function (xhr, status, error) {
							// 사전 검증 실패 시 처리
							console.log("사전 검증 실패: ", error);
							alert("금액이 변조되었습니다. 결제를 진행할 수 없습니다.");
						}
					});
				});
			});

			function formatCurrency(amount) {
				return new Intl.NumberFormat('ko-KR', {style: 'currency', currency: 'KRW'}).format(amount);
			}

			function applyCoupon() {
				var totalAmountText = document.getElementById('total').innerText;
				var totalAmount = parseFloat(totalAmountText.replace(/[^\d]/g, ''));
				var totalAmount = parseFloat(totalAmountText.replace(/[^\d]/g, ''));

				var couponSelect = document.getElementById('coupon-select');
				var selectedOption = couponSelect.options[couponSelect.selectedIndex];
				var selectedValue = selectedOption.value;
				couponNo = selectedOption.getAttribute('data-coupon-no');

				console.log("couponNo: ", couponNo);

				var discount = parseFloat(selectedValue);

				var couponDiscountElement = document.getElementById('couponDiscount');
				var finalPayElement = document.getElementById('finalPay');

				if (!isNaN(discount)) {
					var discountAmount = totalAmount * (discount / 100);
					discountedAmount = totalAmount - discountAmount;
					discountedAmount = totalAmount - discountAmount;

					couponDiscountElement.innerText = formatCurrency(discountAmount);
					finalPayElement.innerText = formatCurrency(discountedAmount);
				} else {
					discountedAmount = totalAmount;
					discountedAmount = totalAmount;
					couponDiscountElement.innerText = formatCurrency(0);
					finalPayElement.innerText = formatCurrency(discountedAmount);
				}
			}

			function sample4_execDaumPostcode() {
				new daum.Postcode({
					oncomplete: function (data) {
						var roadAddr = data.roadAddress;
						var extraRoadAddr = '';

						var roadAddr = data.roadAddress;
						var extraRoadAddr = '';

						if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
							extraRoadAddr += data.bname;
						}
						if (data.buildingName !== '' && data.apartment === 'Y') {
							extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
						}
						if (extraRoadAddr !== '') {
							extraRoadAddr = ' (' + extraRoadAddr + ')';
						}

						document.getElementById('sample4_postcode').value = data.zonecode;
						document.getElementById("sample4_roadAddress").value = roadAddr;
						document.getElementById("sample4_jibunAddress").value = data.jibunAddress;

						if (roadAddr !== '') {
							document.getElementById("sample4_extraAddress").value = extraRoadAddr;
						} else {
							document.getElementById("sample4_extraAddress").value = '';
						}

						var guideTextBox = document.getElementById("guide");
						if (data.autoRoadAddress) {
							var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
							guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
							guideTextBox.style.display = 'block';
						} else if (data.autoJibunAddress) {
							var expJibunAddr = data.autoJibunAddress;
							guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
							guideTextBox.style.display = 'block';
						} else {
							guideTextBox.innerHTML = '';
							guideTextBox.style.display = 'none';
						}
					}
				}).open();
			}