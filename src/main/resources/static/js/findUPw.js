const startFindPW = async () => {
  const UName = document.getElementById("UName").value;
  const UMail = document.getElementById("UMail").value;

  if (!UName || UName === "" || !UMail || UMail === "") return;

  try {
    let data = await fetch(`/auth/findPw?UName=${UName}&UMail=${UMail}`);
    //data = await data.json();
    console.log(data)
    if (data === "fail") return;

    alert("임시 비밀번호가 성공적으로 발급되었습니다. 이메일을 확인해주세요.");
  } catch (e) {
    return alert("공습경보", e);
  }
};

document.getElementById("find-PW-btn").addEventListener("click", startFindPW);
