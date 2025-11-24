package com.example.gRPCBackend;


import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import plate.PlateRecognizerOuterClass;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebController {

    private  ServiceLogic Slogic;

    public   WebController(ServiceLogic Serlogic) {
        Slogic = Serlogic;
    }


    @GetMapping("/{Lplate}")
    public ResponseEntity<RepositryDTO> GetDriverInformation(@PathVariable("Lplate") String Licenseplate){
        RepositryDTO  re = Slogic.FindInformation(Licenseplate);
        log.info(re.toString());
         if(re == null){
             return ResponseEntity.notFound().build();
         }
        return   ResponseEntity.ok(re);
    }

    @PostMapping("/CreateData")
    public ResponseEntity<RepositryDTO> CreateData(@RequestBody RepositryDTO repo){
        Slogic.UpdateDate(repo);
        log.info("생성 요청" );
        // 웹한테 성공했다고 보내주는 것
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/PatchDate")
    public ResponseEntity<RepositryDTO> UpdateData(@RequestBody RepositryDTO repo)
    {
        Slogic.PatchData(repo);
        log.info("수정 요청" );
        // 웹한테 성공했다고 보내주는 것
        return   ResponseEntity.ok().build();
    }
    @DeleteMapping("/Removeall")
    public ResponseEntity<Void> RemoveAllData()
    {
        Slogic.ClearData();
        log.info("DB 초기화 요청");
        return   ResponseEntity.ok().build();
    }
    @GetMapping("/listall")
    public ResponseEntity<List<String>> PlateAllData()
    {
        List<String> re =  Slogic.listData();
        log.info("번호판 모든 데이터 요청");
        return   ResponseEntity.ok(re);
    }
    @PostMapping("/DiskImage")
    public PlateResultDto SendThImage(@RequestParam("image")MultipartFile image , @RequestParam("mode") String Mode) throws IOException
    {
        byte[] bytes = image.getBytes();
        int mode = Integer.parseInt(Mode);
        //.proto파일에 에 있는 메세지중에 보내는 클래스를 부름
        //PlateRecognizerOuterClass는 .proto파일을 빌드하면 생겨나는 클래스로 당황하지 마세요
        PlateRecognizerOuterClass.PlateResponse response = Slogic.Recongize(bytes,mode);
        log.info(response.toString());
        log.info("gRPC요청" );
        return new PlateResultDto(
                response.getPlateNumber()
        );
    }
}
