var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
let selectMode = 0;
let selectFile = null;
const ModeRadio = document.querySelectorAll('input[name="mode"]');
const ImageInput = document.getElementById("imageInput");
const previewImg = document.getElementById("preview");
const Sendbtn = document.getElementById("sendBtn");
const rv = document.getElementById("ResultView");
ModeRadio.forEach(radio => {
    radio.addEventListener("change", () => {
        if (radio.checked) {
            selectMode = parseInt(radio.value, 10);
            console.log("선택한 모드", selectMode);
        }
    });
});
ImageInput.addEventListener("change", () => {
    const files = ImageInput.files;
    if (!files || files.length === 0) {
        selectFile = null;
        previewImg.src = "";
        previewImg.style.display = "none";
        return;
    }
    selectFile = files[0];
    const render = new FileReader();
    render.onload = () => {
        previewImg.src = render.result;
        previewImg.style.display = "block";
    };
    render.readAsDataURL(selectFile);
});
Sendbtn.addEventListener("click", () => __awaiter(this, void 0, void 0, function* () {
    if (!selectFile) {
        alert("이미지를 먼저 선택하세요");
        return;
    }
    const formdata = new FormData();
    formdata.append("image", selectFile);
    formdata.append("mode", selectMode.toString());
    try {
        // 같은 도메인이라서 주소를 안집어 넣고 그냥 URL만으로 충분하다
        const res = yield fetch("api/DiskImage", {
            method: "POST",
            body: formdata
        });
        if (!res.ok) {
            throw new Error(yield res.text());
        }
        const data = yield res.json();
        console.log("파이썬 인식 결과:", data);
        const pn = document.getElementById("platenumber");
        pn.textContent = `번호판 : ${data.licensePlate}`;
    }
    catch (e) {
        console.log(e);
        alert("전송 실패" + e);
    }
}));
const toggleButtons = document.querySelectorAll(".sql-toggle-btn");
toggleButtons.forEach(btn => {
    // dataset.target으로 어떤 버튼을 눌렀는지 알수 있음
    const targetid = btn.dataset.target;
    if (!targetid) {
        console.log(targetid + "없습니다");
        return;
    }
    const pannel = document.getElementById(targetid);
    btn.addEventListener("click", () => {
        const hidden = pannel.style.display === "none" || pannel.style.display === "";
        pannel.style.display = hidden ? "block" : "none";
    });
});
const sqlSendButtons_get = document.getElementById("sql-send-btn_get");
sqlSendButtons_get.addEventListener("click", () => __awaiter(this, void 0, void 0, function* () {
    const input = document.getElementById("sql-number1");
    const rv = document.getElementById("ResultView");
    if (!input.value == null) {
        alert("번호를 입력하세요");
        return;
    }
    try {
        rv.innerHTML = "";
        const res = yield fetch(`/api/${input.value}`);
        const data = yield res.json();
        console.log("받은 데이터:", data);
        rv.innerHTML = `
                <h2>번호판: ${data.license_plate}</h2>
                <h2>벌점 :${data.bad_point}</h2>
                <h2>운전자: ${data.driver_owner}</h2>
            `;
    }
    catch (e) {
        console.log(e);
        alert("get요청 실패");
    }
}));
const sqlSendButtons_Removeall = document.getElementById("sql-send-btn_Removeall");
sqlSendButtons_Removeall.addEventListener("click", () => __awaiter(this, void 0, void 0, function* () {
    try {
        const res = yield fetch(`/api/Removeall`);
        if (!res.ok) {
            throw new Error(yield res.text());
        }
        else {
            alert("db초기화완료");
        }
    }
    catch (e) {
        console.log(e);
        alert("초기화요청 실패");
    }
}));
const sqlSendButtons_Post = document.getElementById("sql-send-btn_post");
sqlSendButtons_Post.addEventListener("click", () => __awaiter(this, void 0, void 0, function* () {
    const input1 = document.getElementById("sql-number21");
    const input2 = document.getElementById("sql-number22");
    const input3 = document.getElementById("sql-number23");
    if (input1.value === null || input1.value.trim() === "") {
        alert("번호를 입력하세요");
        return;
    }
    if (input2.value === null || input2.value.trim() === "") {
        alert("점수를 입력하세요");
        return;
    }
    if (input3.value === null || input3.value.trim() === "") {
        alert("운전자명을 입력하세요");
        return;
    }
    try {
        const body = {
            license_plate: input1.value.trim(),
            bad_point: input2.value.trim(),
            driver_owner: input3.value.trim()
        };
        console.log(body);
        const res = yield fetch(`/api/CreateData`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (!res.ok) {
            throw new Error(yield res.text());
        }
        else {
            alert("post완료");
        }
    }
    catch (e) {
        console.log(e);
        alert("post요청 실패");
    }
}));
const sqlSendButtons_patch = document.getElementById("sql-send-btn_patch");
sqlSendButtons_patch.addEventListener("click", () => __awaiter(this, void 0, void 0, function* () {
    const input1 = document.getElementById("sql-number31");
    const input2 = document.getElementById("sql-number32");
    const input3 = document.getElementById("sql-number33");
    if (input1.value === null || input1.value.trim() === "") {
        alert("번호를 입력하세요");
        return;
    }
    if (input2.value === null || input2.value.trim() === "") {
        alert("점수를 입력하세요");
        return;
    }
    if (input3.value === null || input3.value.trim() === "") {
        alert("운전자명을 입력하세요");
        return;
    }
    try {
        const body = {
            license_plate: input1.value.trim(),
            bad_point: input2.value.trim(),
            driver_owner: input3.value.trim()
        };
        console.log(body);
        const res = yield fetch(`/api/PatchDate`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (!res.ok) {
            throw new Error(yield res.text());
        }
        else {
            alert("patch완료");
        }
    }
    catch (e) {
        console.log(e);
        alert("patch요청 실패");
    }
}));
//# sourceMappingURL=main.js.map