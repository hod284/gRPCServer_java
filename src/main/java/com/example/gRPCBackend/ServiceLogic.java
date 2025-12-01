package com.example.gRPCBackend;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plate.PlateRecognizerGrpc;
import plate.PlateRecognizerOuterClass;

import java.util.List;

@Slf4j
@Service
public class ServiceLogic {
  private final PlateRecognizerGrpc.PlateRecognizerBlockingStub BStub;
  private RepositryManager RepoManager;
  // 도커 컴포즈 파일의 환경 테크에 있는 GRPC_HOST라는 아이피가 있으면 사용하고 없으면 그냥 Localhost사용하겠다
  private String host = System.getenv().getOrDefault("GRPC_HOST", "localhost");
  private int port = Integer.parseInt(System.getenv().getOrDefault("GRPC_PORT", "50051"));
  public   ServiceLogic(RepositryManager RManager ) {
      ManagedChannel channel = ManagedChannelBuilder
              .forAddress(host,port)  // 도커면 서비스 이름:포트
              .usePlaintext()
              .build();

      BStub = PlateRecognizerGrpc.newBlockingStub(channel);
      RepoManager = RManager;
  }

    public PlateRecognizerOuterClass.PlateResponse Recongize(byte[] bytes, int Mode)
    {
        PlateRecognizerOuterClass.RecognizeMode mode = PlateRecognizerOuterClass.RecognizeMode.forNumber(Mode);
        log.info("Recongize mode " + mode);
        PlateRecognizerOuterClass.PlateRequest request = PlateRecognizerOuterClass.PlateRequest.newBuilder()
                .setImage(ByteString.copyFrom(bytes))
                .setMode(mode)
                .build();
        log.info("Recongize request " + request.toString());
        return BStub.recognize(request);  // ← 여기서 실제 gRPC 호출
    }
    @Transactional
    public void UpdateDate(RepositryDTO repo)
    {
        RepoManager.UpdatetheInfotoDB(repo);
    }
    @Transactional
    public void PatchData(RepositryDTO repo)
    {
        RepoManager.PatchtheInfotoDB(repo);
    }
    @Transactional
    public RepositryDTO FindInformation(String Licenseplate)
    {
        return RepoManager.SendtheInfotoClient(Licenseplate);
    }
    @Transactional
    public void ClearData()
    {
         RepoManager.ClearDB();
    }
    @Transactional
    public List<String> listData()
    {
       return  RepoManager.getListDB();
    }
}
