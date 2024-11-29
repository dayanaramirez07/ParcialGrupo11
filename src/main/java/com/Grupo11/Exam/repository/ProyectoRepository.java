package com.Grupo11.Exam.repository;

import com.Grupo11.Exam.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    @Query(" SELECT p FROM Proyecto p JOIN p.empleados e WHERE e.cedula = :cedula")
    List<Proyecto> findByEmpleadoCedula(@Param("cedula") String cedula);
}