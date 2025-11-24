package com.example.gRPCBackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        String sql = "UPDATE  carinformation SET driver_owner =?,bad_point =?  WHERE  license_plate =?";
        JdbcTem.update(sql,repo.getDriver_owner(),repo.getBad_point(),repo.getLicense_plate());
   }

   public void UpdatetheInfotoDB(RepositryDTO repo)
   {
       log.info("DB 새로운 정보 추가");
        String sql = "INSERT INTO carinformation (license_plate,bad_point,driver_owner) VALUES (?,?,?)";
        JdbcTem.update(sql,repo.getLicense_plate(),repo.getBad_point(),repo.getDriver_owner());
   }
    public void ClearDB()
    {
        log.info("DB 초기화");
        JdbcTem.update("TRUNCATE TABLE carinformation");
    }
    public List<String> getListDB()
    {
        log.info("DB 리스트 받기");
        String sql = "SELECT license_plate FROM carinformation";
        return JdbcTem.query(
                sql,
                (rs, rowNum)->rs.getString("license_plate")
        );
    }
   public RepositryDTO SendtheInfotoClient( String LicenseplateNumber)
   {
       log.info("DB 정보 가져오기");
        // 테이블 FROM 에 들어갈 부분 아직 안말들었음 이대호 만들거니오류 날수 있음
        String sql = "SELECT *  FROM carinformation WHERE license_plate  = ?";
        return JdbcTem.queryForObject(sql,this::mapRow,LicenseplateNumber);
   }

   private RepositryDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
        return new RepositryDTO(
                rs.getString("license_plate"),
                rs.getLong("bad_point"),
                rs.getString("driver_owner")
        );
   }
}
