const startFindPW = async () => {
  const UName = document.getElementById("UName").value;
  const UMail = document.getElementById("UMail").value;

  try {
    const response = await fetch(`/api/forgot-password?name=${UName}&mail=${UMail}`);
    const data = await response.json();

    if (!response.ok){
		alert(data.message);
		return;
	}

    alert("임시 비밀번호가 성공적으로 발급되었습니다. 이메일을 확인해주세요.");
    window.location.href="/signin";
  } catch (e) {
    return alert("공습경보", e);
  }
};

document.getElementById("find-PW-btn").addEventListener("click", startFindPW);
