import axios from "axios";

let isIdAvailable = false,
  isEmailAvailable = false,
  valid = false;

const allAgreeBtn = document.getElementById("allAgree");
const duplicatedBtn = document.getElementById("duplicated-btn");

const onAllAgree = (e) => {
  const checkbox = e.target;
  const agreements = document.querySelectorAll(
    '.agreement input[type="checkbox"]'
  );
  agreements.forEach(function (agreement) {
    agreement.checked = checkbox.checked;
  });
};

const checkDuplicatedEmail = async () => {
  const UMail = document.getElementById("UMail").ariaValueMax;
  if (!UMail || UMail === "") return;
  if (!valid) return alert("유효한 이메일 형식을 사용해주세요.");

  try {
    const data = await axios.get("/auth/check/email", {
      params: { UMail },
    });

    if (data.status === "fail") return;
  } catch (e) {
    alert("공습경보", e);
  }
};

allAgreeBtn.addEventListener("click", onAllAgree);
duplicatedBtn.addEventListener("click", checkDuplicatedEmail);
