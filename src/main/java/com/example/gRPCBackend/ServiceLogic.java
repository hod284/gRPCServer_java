package com.example.gRPCBackend;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plate.PlateRecognizerGrpc;
import plate.PlateRecognizerOuterClass;

@Slf4j
@Service
public class ServiceLogic {
  private final PlateRecognizerGrpc.PlateRecognizerBlockingStub BStub;
  private RepositryManager RepoManager;
  public   ServiceLogic(RepositryManager RManager ) {
      ManagedChannel channel = ManagedChannelBuilder
              .forAddress("localhost", 50051)  // 도커면 서비스 이름:포트
              .usePlaintext()
              .build();

      BStub = PlateRecognizerGrpc.newBlockingStub(channel);
      RepoManager = RManager;
  }

    public PlateRecognizerOuterClass.PlateResponse Recongize(byte[] bytes, String modestr)
    {
        PlateRecognizerOuterClass.RecognizeMode mode = PlateRecognizerOuterClass.RecognizeMode.valueOf("MODE_" + modestr.toUpperCase());
        log.info("Recongize mode " + modestr);
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
}
