const startFindID = async () => {
	const UMail = document.getElementById("UMail").value;
	if (!UMail || UMail === "") return;

	try {
		let response = await fetch(`/auth/find/user/id?UMail=${UMail}`);

		if (!response.ok) {
			throw new Error("네트워크 응답에 문제가 있습니다.");
		}

		let data = await response.text();
		alert(`아이디는 ${data}`);
		window.location.href = "/auth/login";
	} catch (e) {

		console.error("에러 발생:", e);
		return alert("오류가 발생했습니다.");
	}
};

document.getElementById("find-id-btn").addEventListener("click", startFindID);
