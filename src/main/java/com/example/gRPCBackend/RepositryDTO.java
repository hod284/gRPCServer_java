package com.example.gRPCBackend;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositryDTO {
private String license_plate;
private long bad_point;
private String driver_owner;
  public RepositryDTO( String LPlate, long BPoint, String DOwner  ) {
      license_plate = LPlate;
      bad_point= BPoint;
      driver_owner = DOwner;
  }

}
