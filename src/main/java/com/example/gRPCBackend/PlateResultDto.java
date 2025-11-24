package com.example.gRPCBackend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlateResultDto {
   private String LicensePlate;
   public PlateResultDto(String lPlate)
   {
       LicensePlate= lPlate;
   }
}
