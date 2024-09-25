const startFindID = async () => {
	const UMail = document.getElementById("UMail").value;

	try {
		const response = await fetch(`/api/forgot-id?mail=${UMail}`);

		const res = await response.json();
		if (!response.ok) {
			alert(res.message);
			return;
		}

		alert(`아이디는 ${res.data}`);
		window.location.href = "/signin";
	} catch (e) {

		console.error("에러 발생:", e);
		return alert("오류가 발생했습니다.");
	}
};

document.getElementById("find-id-btn").addEventListener("click", startFindID);
