const thumbnailInput = document.getElementById("product-main-image");
const thumbnailPreview = document.getElementById("thumbnail-preview");
const titleInput = document.getElementById("product-title");
const productPrice = document.getElementById("prices");
const categoryBox = document.getElementById("select");
const addOptionBtn = document.getElementById("add-option-btn");
const optionWrapper = document.getElementById("option-wrapper");
const submitProductImageWrapper = document.getElementById(
  "submit-product-image"
);
const productsImagesInput = document.getElementById("product-files");
const productImagesWrapper = document.getElementById("product-images-wrapper");
const price = document.getElementById("price-input");

const submitProductBtn = document.getElementById("submit-product");
// const option
// const optionsContainer = document.getElementById("product-options");

let thumbnail = null;
let productImages = [];
// let title = "";
let category = "furniture";

const formattingCommas = (num) => {
  if (typeof num !== "number") {
    throw new Error("Price is Must be NUMBER");
  }

  return num.toLocaleString();
};

const verificationPrice = (price) => {
  // price => string;
  if (price === "") {
    return "";
  }
  const replaceCommaValue = Number(price.replaceAll(",", ""));
  if (Number.isNaN(replaceCommaValue) || replaceCommaValue < 0) {
    // price is NaN
    return null;
  }

  return formattingCommas(replaceCommaValue);
};

const verifyPriceIsNaN = (e) => {
  const input = e.target.closest('input[type="text"]');
  const { value, name } = input;
  if (!input || !name.includes("price")) return;
  const verificatedPrice = verificationPrice(value);
  if (!verificatedPrice) {
    input.value = formattingCommas(
      Number(value.replaceAll(",", "").slice(0, -1))
    );
    return;
  }

  input.value = verificatedPrice;
};

// const onTitleChange = (e) => {
//   title = e.target.value;
// };

const onFileUpload = (e) => {
  if (e.target.id === "product-files") {
    console.log(e.target, "zzz");
    //상품 이미지
    const { files } = e.target;
    Array.from(files).forEach((file) => {
      productImages.push(file);
      const src = URL.createObjectURL(file);
      const imageWrapper = new ProductImage(src);
      productImagesWrapper.insertBefore(
        imageWrapper.wrapper,
        submitProductImageWrapper
      );
    });
    return;
  }
  //thumbnail
  const file = e.target.files[0];
  thumbnailPreview.style.display = "inline-block";

  const img = thumbnailPreview.querySelector("img");
  img.src = URL.createObjectURL(file);
  thumbnail = file;
};

// const onCategoryChange = (e) => {
//   category = e.target.value;
// };

const onAddOption = () => {
  // 옵션추가 버튼
  // const idx = document
  const idx = document.getElementsByClassName("option").length;

  const newOption = new NewOption(idx);
  optionWrapper.appendChild(newOption.container);
};

const onAddSubOption = (e) => {
  //값 추가
  const btn = e.target.closest("button");
  //   console.log(e.target);
  if (!btn) return;

  const classNames = btn.className.split("-");
  const idx = classNames[classNames.length - 1];
  const wrapper = document.getElementsByClassName("option")[idx];
  if (!wrapper) return;
  const addOptionButtonWrapper =
    wrapper.getElementsByClassName("add-value-wrapper");
  const oldButton = addOptionButtonWrapper[addOptionButtonWrapper.length - 1];
  oldButton.remove();
  const newValueDiv = new newOptionValue(idx);
  wrapper.appendChild(newValueDiv.wrapper);
};

function dataURLtoBlob(dataURL) {
  const byteString = atob(dataURL.split(",")[1]);
  const mimeString = dataURL.split(",")[0].split(":")[1].split(";")[0];
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: mimeString });
}

const onSubmitProduct = async () => {
  const obj = [];
  const title = titleInput.value;
  const category = categoryBox.value;
  const options = document.getElementsByClassName("option");
  // console.log(options);
  // const reqThumbnail = dataURLtoBlob(thumbnail)

  const formData = new FormData();
  formData.append("thumbnail", thumbnail);
  productImages.forEach((img) => {
    formData.append("srcImage", img);
  });

  Array.from(options).forEach((option, idx) => {
    const optionSrc = option.getElementsByClassName("option-src");
    const title = option.getElementsByClassName("option-title")[0].value;
    const arr = {
      optionName: title,
      options: [],
    };
    Array.from(optionSrc).forEach((div, idx2) => {
      const name = div.getElementsByClassName("optionName")[0].value;
      const price = Number(
        div.getElementsByClassName("prices-input")[0].value.replaceAll(",", "")
      );
      arr["options"].push({
        name,
        price,
      });
    });
    obj.push(arr);
  });
  const reqObjectData = {
    title,
    category,
    price: price.value,
    option: obj,
    thumbnail: "z",
  };

  formData.append(
    "productData",
    new Blob([JSON.stringify(reqObjectData)], {
      type: "application/json",
    })
  );
  try {
    const resData = await fetch("/shopWrite", {
      method: "POST",
      body: formData,
    });
    const response = await resData.json();

    if (response.ok) {
      // 성공적으로 처리된 경우, 원하는 페이지로 리다이렉트
      window.location.href = "/shopList"; // 원하는 리다이렉트 경로
    } else {
      alert("상품 등록 중 오류가 발생했습니다.");
    }
  } catch (error) {
    console.error("상품 등록 오류:", error);
    alert("상품 등록 중 오류가 발생했습니다.");
  }
};

thumbnailInput.addEventListener("change", onFileUpload);
productsImagesInput.addEventListener("change", onFileUpload);
// titleInput.addEventListener("input", onTitleChange);
productPrice.addEventListener("input", verifyPriceIsNaN);
// categoryBox.addEventListener("change", onCategoryChange);
addOptionBtn.addEventListener("click", onAddOption);
optionWrapper.addEventListener("input", verifyPriceIsNaN);
optionWrapper.addEventListener("click", onAddSubOption);
submitProductBtn.addEventListener("click", onSubmitProduct);

class newOptionValue {
  //값 추가 눌렀을 때
  constructor(idx = 0) {
    this.wrapper = document.createElement("div");
    this.wrapper.className = "option-src section-wrapper";

    const optionValueDiv = document.createElement("div");

    const subTitle = document.createElement("div");
    subTitle.className = "sub-title";
    subTitle.textContent = "이름";
    optionValueDiv.appendChild(subTitle);

    const valueDiv = document.createElement("div");
    const optionValueInput = document.createElement("input");
    optionValueInput.type = "text";
    optionValueInput.className = "optionName";
    optionValueInput.name = "optionValue-";

    valueDiv.appendChild(optionValueInput);
    optionValueDiv.appendChild(valueDiv);

    this.wrapper.appendChild(optionValueDiv);

    //
    const priceDiv = document.createElement("div");

    const priceTitle = document.createElement("div");
    priceTitle.className = "sub-title";
    priceTitle.textContent = "가격";
    priceDiv.appendChild(priceTitle);

    const priceInputDiv = document.createElement("div");
    priceInputDiv.className = "prices-input-wrapper";
    const priceInput = document.createElement("input");
    priceInput.type = "text";
    priceInput.name = "optionPrice- price";
    priceInput.className = "prices-input";

    priceInputDiv.appendChild(priceInput);
    priceDiv.appendChild(priceInputDiv);
    this.wrapper.appendChild(priceDiv);

    const newButton = document.createElement("button");
    const buttonId = `option-value-add-btn-${idx}`;
    newButton.className = `option-v-b-${idx}`;
    newButton.id = buttonId;
    newButton.class = `option-v-b-${idx}`;
    const label = document.createElement("label");
    label.setAttribute("for", buttonId);
    label.textContent = "값 추가하기 +";

    const btnWrapper = document.createElement("div");
    btnWrapper.className = "add-value-wrapper";
    btnWrapper.appendChild(newButton);
    btnWrapper.appendChild(label);

    this.wrapper.appendChild(btnWrapper);
  }
}

class NewOption {
  // 옵션 추가 눌렀을 때
  constructor(idx) {
    this.container = document.createElement("div");
    this.container.className = "section-wrapper option";

    this.addTitle();
    this.addOptionSrc(idx);
  }

  addOptionSrc = (idx) => {
    const v = new newOptionValue(idx);
    this.container.appendChild(v.wrapper);
  };

  addTitle = () => {
    const titleDiv = document.createElement("div");
    titleDiv.className = "sub-title";
    titleDiv.textContent = "옵션이름";

    const optionValuesDiv = document.createElement("div");
    optionValuesDiv.className = "option-values";

    const optionNameInput = document.createElement("input");
    optionNameInput.type = "text";
    optionNameInput.name = "optionName";
    optionNameInput.className = "option-title";

    optionValuesDiv.appendChild(optionNameInput);

    this.container.appendChild(titleDiv);
    this.container.appendChild(optionValuesDiv);
  };
}

class ProductImage {
  constructor(src) {
    this.wrapper = document.createElement("div");
    this.wrapper.className = "product-src-image";

    const imageTag = document.createElement("img");
    imageTag.src = src;

    this.wrapper.appendChild(imageTag);
  }
}
