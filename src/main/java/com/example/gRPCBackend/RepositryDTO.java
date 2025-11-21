package com.example.gRPCBackend;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositryDTO {
private String LicensePlate;
private long BadPoint;
private String DriveOwner;
  public RepositryDTO( String LPlate, long BPoint, String DOwner  ) {
      LicensePlate = LPlate;
      BadPoint = BPoint;
      DriveOwner = DOwner;
  }

}
