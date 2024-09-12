import axios from "axios";
import { interectionObserver } from "../../utils/observer";
import { dummy } from "./dummy";

const side = document.getElementById("side");
const sidebarArrow = document.getElementById("arrow-box");
const searchInput = document.getElementById("searching-input");
const sortBox = document.getElementById("selected");
const selectedSort = sortBox.getElementsByTagName("span")[0];
const options = document.getElementById("select-options");
const container = document.getElementById("container");
const trashBtn = document.getElementById("trash");
const createProjectBtn = document.getElementById("create-project");
const observer = document.getElementById("observer");
const projectDescriptionContainer = document.getElementById("pdc");
const projectDescriptionBG = document.getElementById("pdbg");
const submitProject = document.getElementById("create-p-btn");
const textArea = document.getElementById("description-des");
const optionHeight = 200;

let isLaoding = false;
let hasMoreProjects = true;
let isTrashBtnClick = false;

const projects = [];
const remove_projects = {};

const removeAllProject = () => {
  const projects = Array.from(container.getElementsByClassName("project"));
  projects.forEach((box) => {
    if (box.classList.contains("received")) {
      container.removeChild(box);
    }
  });
};

const ProjectBox = (project) => {
  const box = document.createElement("div");
  const a = document.createElement("a");

  const { project_id, thumnail, title, regDt, src } = project;
  // wook 눌렀을 때 이동
  //wrapper
  a.href = `http://localhost:8080/three/${project_id}`;
  a.id = project_id;
  a.className = "project-wrapper";
  box.style.position = "relative";
  box.appendChild(a);
  box.className = "project received";

  //img wrapper
  const renovate_img = document.createElement("div");
  renovate_img.className = "renovate-img";
  const backgroundImage = document.createElement("img");
  backgroundImage.src = thumnail;
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

const getDummyData = () => {
  return new Promise((res) => {
    setTimeout(() => {
      res(dummy);
    }, 1000);
  });
};

const getProjects = async () => {
  if (!hasMoreProjects || isLaoding) return;
  isLaoding = true;

  const filter = searchInput.value;
  const sort = selectedSort.id;

  //wook
  //밑에 주소 바꿔주셈
  let flag = 0;
  let data;
  try {
    data = await axios.get("/get/projects", {
      params: { filter, sort, flag },
    });
    // const receivedProjects = data.data.projects;
    // projects.push(...receivedProjects);
    // receivedProjects.forEach((project) => {
    //   const projectBox = ProjectBox(project);
    //   container.insertBefore(projectBox, observer);
    // });
  } catch (err) {
    console.log("error occured!", err);
    data = await getDummyData();
  }
  // console.log(data);
  const receivedProjects = data.data.projects;
  projects.push(...receivedProjects);
  receivedProjects.forEach((project) => {
    const projectBox = ProjectBox(project);
    container.insertBefore(projectBox, observer);
  });
  isLaoding = false;

  // /*
  /*
    
	//data
	{
	  status:'ssecess' or fail,
	  response:200 errocode,
	  data:{
		  projects:[],
		  flag_num:0 => 0~11, 1 => 12 ~ 23
	  }
   }
	  const 
	 
	  hasMoreProjects = data.data.hasMoreProjects;
	  */
};

const onFilterChange = () => {
  removeAllProject();
  getProjects();
};

const onChangeSort = (e) => {
  const li = e.target.closest("li");
  if (!li) return;

  selectedSort.innerHTML = li.innerHTML;
  selectedSort.id = li.id;

  removeAllProject();
  getProjects();
};

const onSortClick = (e) => {
  e.stopPropagation();
  if (options.style.display === "block") {
    options.style.display = "none";
    return;
  }
  const offset = 15;
  const heightRect = sortBox.getClientRects()[0];
  const bottom = window.innerHeight - heightRect.bottom;
  if (bottom > optionHeight) {
    options.style.bottom = 0;
    options.style.top = sortBox.offsetHeight + offset + "px";
  } else {
    options.style.top = 0;
    options.style.bottom = sortBox.offsetHeight + offset + "px";
  }
  options.style.display = "block";
};

const onChangeSideBar = () => {
  const left = sidebarArrow.getElementsByClassName("left")[0];
  const right = sidebarArrow.getElementsByClassName("right")[0];
  if (side.classList.contains("spread")) {
    left.style.display = "none";
    right.style.display = "block";
    side.classList.remove("spread");
    side.classList.add("fold");
  } else {
    right.style.display = "none";
    left.style.display = "block";
    side.classList.remove("fold");
    side.classList.add("spread");
  }
};

const reqRemoveProjects = async () => {
  // wook
  // 프로젝트 삭제
  // ex) localhost:3000/remove_projects?project_ids=[1,2,3,4]
  const data = await axios.post("", {
    project_ids: Object.keys(remove_projects),
  });
  //
};

const onTrashClick = () => {
  isTrashBtnClick = !isTrashBtnClick;
  const nodes = Array.from(document.getElementsByClassName("delete_bg"));
  if (isTrashBtnClick) {
    nodes.forEach((node) => (node.style.display = "block"));
  } else {
    reqRemoveProjects();
    nodes.forEach((node) => (node.style.display = "none"));
  }
};

const onProjectClick = (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;
  const project_id = btn.name;
  const input = btn.getElementsByTagName("input")[0];
  input.checked = !input.checked;

  if (input.checked) {
    remove_projects[project_id] = project_id;
  } else {
    delete remove_projects[project_id];
  }
};

const onClickWindow = () => {
  if (options.style.display === "block") {
    options.style.display = "none";
    return;
  }
};

const onCreateProject = () => {
  let s = "block";
  if (projectDescriptionContainer.style.display === "block") {
    s = "none";
  }
  projectDescriptionContainer.style.display = s;
};
const onSubmitProject = () => {
  const title = document.getElementById("description-title").value;

  const src = textArea.value;

  const obj = { title, src };
  localStorage.setItem("pdes", JSON.stringify(obj));
  window.location.href = `${window.location.origin}/three/design`;
};
interectionObserver(observer, getProjects);

container.addEventListener("click", onProjectClick);
submitProject.addEventListener("click", onSubmitProject);
projectDescriptionBG.addEventListener("click", onCreateProject);
createProjectBtn.addEventListener("click", onCreateProject);
trashBtn.addEventListener("click", onTrashClick);
sortBox.addEventListener("click", onSortClick);
options.addEventListener("click", onChangeSort);
sidebarArrow.addEventListener("click", onChangeSideBar);
window.addEventListener("click", onClickWindow);
window.removeEventListener("beforeunload", onClickWindow);
searchInput.addEventListener("input", debounce(onFilterChange, 200));
function debounce(func, delay) {
  let timer;
  return function () {
    const args = arguments;
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(this, args);
    }, delay);
  };
}
