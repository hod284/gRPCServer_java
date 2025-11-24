let selectMode: number =0;
let selectFile: File | null = null;


   const ModeRadio = document.querySelectorAll<HTMLInputElement>('input[name="mode"]');
   const ImageInput = document.getElementById("imageInput") as HTMLInputElement;
   const previewImg = document.getElementById("preview") as HTMLInputElement;
   const Sendbtn = document.getElementById("sendBtn") as HTMLButtonElement;
    const rv = document.getElementById("ResultView") as HTMLElement;
   ModeRadio.forEach( radio =>{
       radio.addEventListener("change",()=>{
           if(radio.checked)
           {
               selectMode = parseInt(radio.value,10);
               console.log("선택한 모드",selectMode);
           }
       })
   });

   ImageInput.addEventListener("change",() =>{
       const files = ImageInput.files;
       if(!files || files.length === 0)
       {
           selectFile = null;
           previewImg.src = "";
           previewImg.style.display = "none";
           return;
       }
       selectFile = files[0];
       const render = new FileReader();
       render.onload =()=>{
           previewImg.src = render.result as string;
           previewImg.style.display ="block";
       }
       render.readAsDataURL(selectFile);
    });

    Sendbtn.addEventListener("click",async ()=>{
       if(!selectFile) {
           alert("이미지를 먼저 선택하세요");
           return;
       }
       const formdata = new FormData();
       formdata.append("image", selectFile);
       formdata.append("mode", selectMode.toString());

       try
       {
           // 같은 도메인이라서 주소를 안집어 넣고 그냥 URL만으로 충분하다
           const res =await fetch("api/DiskImage", {
               method : "POST",
               body: formdata
           });
           if(!res.ok)
           {
               throw new Error(await res.text());
           }
           const data = await res.json();
           console.log("파이썬 인식 결과:", data);
           const pn = document.getElementById("platenumber");
           pn.textContent = `번호판 : ${data.licensePlate}`;
       }
       catch (e)
       {
           console.log(e);
           alert("전송 실패"+e);
       }
   });

   const toggleButtons = document.querySelectorAll<HTMLInputElement>(".sql-toggle-btn");

   toggleButtons.forEach(btn =>{
       // dataset.target으로 어떤 버튼을 눌렀는지 알수 있음
       const targetid = btn.dataset.target;

       if(!targetid)
       {
           console.log(targetid+"없습니다");
           return;
       }
       const pannel = document.getElementById(targetid) as HTMLDivElement;
       btn.addEventListener( "click",()=>{
           const hidden = pannel.style.display === "none" || pannel.style.display === "";
           pannel.style.display= hidden?"block": "none";
       });
   });

   const sqlSendButtons_get = document.getElementById("sql-send-btn_get") as HTMLButtonElement;
   sqlSendButtons_get.addEventListener("click", async () =>{
       const input = document.getElementById("sql-number1") as HTMLInputElement;
       const rv = document.getElementById("ResultView") as HTMLElement;
       if(!input.value == null)
       {
           alert("번호를 입력하세요");
           return;
       }
       try
       {
           rv.innerHTML="";
           const res =await fetch(`/api/${input.value}`);
           const data = await res.json();
           console.log("받은 데이터:", data);
           rv.innerHTML = `
                <h2>번호판: ${data.license_plate}</h2>
                <h2>벌점 :${data.bad_point}</h2>
                <h2>운전자: ${data.driver_owner}</h2>
            `;
       }
       catch (e)
       {
          console.log(e);
          alert("get요청 실패 데이터가 없음");
       }
   });

const sqlSendButtons_Removeall = document.getElementById("sql-send-btn_Removeall") as HTMLButtonElement;
sqlSendButtons_Removeall.addEventListener("click", async () =>{
    try
    {
        const res =await fetch(`/api/Removeall`,{
            method :"DELETE"
        });
        if(!res.ok)
        {
            throw new Error(await res.text());
        }
        else
        {
            alert("db초기화완료");
        }
    }
    catch (e)
    {
        console.log(e);
        alert("초기화요청 실패");
    }
});
const sqlSendButtonslist= document.getElementById("sql-send-btnlist") as HTMLButtonElement;
sqlSendButtonslist.addEventListener("click", async () =>{
    try
    {
        const res =await fetch(`/api/listall`);
        const data = await res.json();
        if(data.length>0) {
            data.forEach(plates => {
                const li = document.createElement("li") as HTMLElement;
                li.textContent = plates;
                const rv = document.getElementById("ResultView") as HTMLElement;
                rv.appendChild(li);
            });
        }
        else
        {
            rv.innerHTML = `<h2>리스트 없음 </h2>>`;
        }
        if(!res.ok)
        {
            throw new Error(await res.text());
        }
        else
        {
            alert("리스트 가져오기 완료");
        }
    }
    catch (e)
    {
        console.log(e);
        alert("리스트 실패");
    }
});
    const sqlSendButtons_Post = document.getElementById("sql-send-btn_post") as HTMLButtonElement;
    sqlSendButtons_Post.addEventListener("click", async () =>{
        const input1 = document.getElementById("sql-number21") as HTMLInputElement;
        const input2 = document.getElementById("sql-number22") as HTMLInputElement;
        const input3 = document.getElementById("sql-number23") as HTMLInputElement;
        if(input1.value === null||input1.value.trim() === "")
        {
            alert("번호를 입력하세요");
            return;
        }
        if(input2.value === null||input2.value.trim() === "")
        {
            alert("점수를 입력하세요");
            return;
        }
        if(input3.value === null||input3.value.trim() === "")
        {
            alert("운전자명을 입력하세요");
            return;
        }
        try
        {
            const body = {
                license_plate : input1.value.trim(),
                bad_point: input2.value.trim(),
                driver_owner: input3.value.trim()
            };
            console.log(body);
            const res =await fetch(`/api/CreateData`, {
                method : "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });
            if(!res.ok)
            {
                throw new Error(await res.text());
            }
            else
            {
                alert("post완료");
            }
        }
        catch (e)
        {
            console.log(e);
            alert("post요청 실패");
        }
    });

    const sqlSendButtons_patch = document.getElementById("sql-send-btn_patch") as HTMLButtonElement;
    sqlSendButtons_patch.addEventListener("click", async () =>{
        const input1 = document.getElementById("sql-number31") as HTMLInputElement;
        const input2 = document.getElementById("sql-number32") as HTMLInputElement;
        const input3 = document.getElementById("sql-number33") as HTMLInputElement;
        if(input1.value === null||input1.value.trim() === "")
        {
            alert("번호를 입력하세요");
            return;
        }
        if(input2.value === null||input2.value.trim() === "")
        {
            alert("점수를 입력하세요");
            return;
        }
        if(input3.value === null||input3.value.trim() === "")
        {
            alert("운전자명을 입력하세요");
            return;
        }
        try
        {
            const body = {
                license_plate : input1.value.trim(),
                bad_point: input2.value.trim(),
                driver_owner: input3.value.trim()
            };
            console.log(body);
            const res =await fetch(`/api/PatchDate`, {
                method : "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });
            if(!res.ok)
            {
                throw new Error(await res.text());
            }
            else
            {
                alert("patch완료");
            }
        }
        catch (e)
        {
            console.log(e);
            alert("patch요청 실패");
        }
    });





