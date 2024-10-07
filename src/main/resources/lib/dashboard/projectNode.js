const defaultImg =
  "https://storage.googleapis.com/idesign_dev/static/KakaoTalk_Photo_2024-09-19-17-31-16.jpeg";

export const projectNode = (project) => {
  const box = document.createElement("div");

  const a = document.createElement("a");

  const { project_id, thumbnail, title, regDt, src } = project;
  box.id = project_id;
  // wook 눌렀을 때 이동
  a.href = `${window.location.origin}/three?project_id=${project_id}`;
  a.className = "project-wrapper";
  box.style.position = "relative";
  box.appendChild(a);

  box.className = "project received";

  //img wrapper
  const renovate_img = document.createElement("div");
  renovate_img.className = "renovate-img";
  const backgroundImage = document.createElement("img");
  backgroundImage.loading = "lazy";
  backgroundImage.src = thumbnail === "none" ? defaultImg : thumbnail;
  renovate_img.append(backgroundImage);
  a.appendChild(renovate_img);

  // 프로젝트 설명란
  const project_src = document.createElement("div");
  project_src.className = "project-src";

  const projectName = document.createElement("div");
  projectName.innerText = title;
  project_src.appendChild(projectName);

  const regDt_div = document.createElement("div");
  const formattedDate = new Intl.DateTimeFormat("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false, // 24시간 형식
  }).format(new Date(regDt));

  regDt_div.innerText = formattedDate;
  project_src.appendChild(regDt_div);

  const srcDiv = document.createElement("div");
  srcDiv.className = "srcDiv";
  srcDiv.innerText = src;
  project_src.appendChild(srcDiv);

  //삭제하기 눌렀을 때 나타남
  const delete_div = document.createElement("button");
  delete_div.className = "delete_bg";
  delete_div.name = project_id;

  const delete_background = document.createElement("div");
  delete_background.className = "delete_background";
  delete_div.appendChild(delete_background);

  const selectInput = document.createElement("input");
  selectInput.checked = false;
  selectInput.type = "checkbox";
  selectInput.className = "select_input";
  selectInput.id = `${project.project_id}_checkBox`;
  delete_div.appendChild(selectInput);

  const label = document.createElement("label");
  label.htmlFor = `${project_id}_checkBox`;
  label.className = "radio-label";
  delete_div.appendChild(label);

  a.appendChild(project_src);
  box.appendChild(delete_div);
  return box;
};
