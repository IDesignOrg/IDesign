import axios from "axios";

const startFindID = async () => {
  const UMail = document.getElementById("UMail").value;
  if (!UMail || UMail === "") return;

  try {
    let data = await fetch(`/auth/find/user/id?UMail=${UMail}`);
    data = await data.json();

    if (data.status === "fail") return alert("오류가 발생했습니다.");
    alert("아이디는 ", data.data.ID);
    window.location.href = "/auth/login";
  } catch (e) {
    return alert("오류가 발생했습니다.", e);
  }
};

document.getElementById("find-id-btn").addEventListener("click", startFindID);
