package com.Grupo11.Exam.controller;

import com.Grupo11.Exam.model.*;
import com.Grupo11.Exam.repository.*;
import com.Grupo11.Exam.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQLController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    // Resolver para Query: proyectosPorEmpleado
    @QueryMapping
    public List<Proyecto> proyectosPorEmpleado(@Argument String cedula) {

        System.out.println(cedula);

        return proyectoRepository.findByEmpleadoCedula(cedula);
    }

    // Resolver para Mutation: registrarProyecto
    @MutationMapping
    public Proyecto registrarProyecto(
            @Argument String nombre,
            @Argument String descripcion,
            @Argument String fechaInicio,
            @Argument String fechaFin,
            @Argument String estado,
            @Argument List<EmpleadoInput> empleados) {

        System.out.println(nombre);
        System.out.println(descripcion);
        System.out.println(fechaInicio);
        System.out.println(fechaFin);
        System.out.println(estado);
        System.out.println(empleados);

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setFechaInicio(LocalDate.parse(fechaInicio));
        proyecto.setFechaFin(LocalDate.parse(fechaFin));
        proyecto.setEstado(estado);

        List<Empleado> empleadosList = empleados.stream().map(input -> {
            Empleado empleado = new Empleado();
            empleado.setNombre(input.getNombre());
            empleado.setApellido(input.getApellido());
            empleado.setCedula(input.getCedula());
            empleado.setRol(input.getRol());
            empleado.setProyecto(proyecto);
            return empleado;
        }).collect(Collectors.toList());

        proyecto.setEmpleados(empleadosList);
        return proyectoRepository.save(proyecto);
    }
}
