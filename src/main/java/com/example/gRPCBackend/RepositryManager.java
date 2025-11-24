package com.example.gRPCBackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
public class RepositryManager {
   private JdbcTemplate JdbcTem;

   public RepositryManager(JdbcTemplate Jdbc)
   {
       JdbcTem = Jdbc;
   }

   public void PatchtheInfotoDB(RepositryDTO repo)
   {
       log.info("DB 업데이트");
        String sql = "UPDATE  Carinformation SET LicensePlate =?,BadPoint =?  WHERE  LicensePlate =?";
        JdbcTem.update(sql,repo.getLicensePlate(),repo.getBadPoint(),repo.getDriveOwner());
   }

   public void UpdatetheInfotoDB(RepositryDTO repo)
   {
       log.info("DB 새로운 정보 추가");
        String sql = "INSERT INTO Carinformation (LicensePlate,BadPoint,DrivateOwner) VALUES (?,?,?)";
        JdbcTem.update(sql,repo.getLicensePlate(),repo.getBadPoint(),repo.getDriveOwner());
   }

   public RepositryDTO SendtheInfotoClient( String LicenseplateNumber)
   {
       log.info("DB 정보 가져오기");
        // 테이블 FROM 에 들어갈 부분 아직 안말들었음 이대호 만들거니오류 날수 있음
        String sql = "SELECT *  FROM Carinformation WHERE LicensePlate = ?";
        return JdbcTem.queryForObject(sql,this::mapRow,LicenseplateNumber);
   }

   private RepositryDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
        return new RepositryDTO(
                rs.getString("LicensePlate"),
                rs.getLong("BadPoint"),
                rs.getString("DriveOwner")
        );
   }
}
