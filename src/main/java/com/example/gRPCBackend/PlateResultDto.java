package com.example.gRPCBackend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlateResultDto {
   private String lincensePlate;
   public PlateResultDto(String lPlate)
   {
       lincensePlate = lPlate;
   }
}
