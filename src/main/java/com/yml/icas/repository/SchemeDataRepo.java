package com.yml.icas.repository;

import com.yml.icas.model.SchemeData;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchemeDataRepo extends JpaRepository<SchemeData, Integer> {
    @Query("Select s from SchemeData s where s.idText=:idText")
    SchemeData findByIdText(@Param("idText") String idText);

    @Query("Select s from SchemeData s where s.category=:category order by s.id")
    List<SchemeData> getByCategory(@Param("category") String category);

    //List<SchemeData> findAllBy
}
